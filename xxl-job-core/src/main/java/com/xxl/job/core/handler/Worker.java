package com.xxl.job.core.handler;

import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.constant.JobHandleStatus;
import com.xxl.job.core.log.LogCallBack;
import com.xxl.job.core.util.CallBack;
import com.xxl.job.core.util.HttpUtil;
import com.xxl.job.core.util.JacksonUtil;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * handler thread
 *
 * @author xuxueli 2016-1-16 19:52:47
 */
public class Worker extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(Worker.class);

    private final IJobHandler handler;
    private final LinkedBlockingQueue<Map<String, String>> jobQueue;
    private final ConcurrentHashSet<String> logIdSet;        // avoid repeat trigger for the same TRIGGER_LOG_ID
    private final static ThreadLocal<String> contextHolder = new ThreadLocal<>();
    private final AtomicBoolean toStop = new AtomicBoolean(false);
    private final AtomicInteger i = new AtomicInteger(1);

    public Worker(IJobHandler handler) {
        this.handler = handler;
        jobQueue = new LinkedBlockingQueue<>();
        logIdSet = new ConcurrentHashSet<>();
    }

    public IJobHandler getHandler() {
        return handler;
    }

    public void toStop() {
        /**
         * Thread.interrupt只支持终止线程的阻塞状态(wait、join、sleep)，
         * 在阻塞出抛出InterruptedException异常,但是并不会终止运行的线程本身；
         * 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式；
         */
        this.toStop.set(true);
    }

    public void pushJob(Map<String, String> jobInfo) {
        if (jobInfo.get(HandlerParamEnum.LOG_ID.name()) != null && !logIdSet.contains(jobInfo.get(HandlerParamEnum.LOG_ID.name()))) {
            jobQueue.offer(jobInfo);
        }
    }


    @Override
    public void run() {
        while (!toStop.get()) {
            try {
                Map<String, String> handlerData = jobQueue.poll();
                if (handlerData != null) {
                    i.set(0);
                    String log_address = handlerData.get(HandlerParamEnum.LOG_ADDRESS.name());
                    String log_id = handlerData.get(HandlerParamEnum.LOG_ID.name());
                    String handler_params = handlerData.get(HandlerParamEnum.EXECUTOR_PARAMS.name());
                    logIdSet.remove(log_id);

                    // parse param
                    String[] handlerParams;
                    if (handler_params != null && handler_params.trim().length() > 0) {
                        handlerParams = handler_params.split(",");
                    } else {
                        handlerParams = new String[0];
                    }

                    // handle job
                    JobHandleStatus _status = JobHandleStatus.FAIL;
                    String _msg;
                    String _returnInfo = null;
                    try {
                        contextHolder.set(log_id);
                        logger.info(">>>>>>>>>>> xxl-job handle start.");
                        CallBack callBack = handler.execute(handlerParams);
                        _status = JobHandleStatus.valueOf(callBack);
                        _msg = callBack.getMsg();
                        _returnInfo = JacksonUtil.writeValueAsString(callBack.getData());
                    } catch (Exception e) {
                        logger.info("Worker Exception:", e);
                        StringWriter out = new StringWriter();
                        e.printStackTrace(new PrintWriter(out));
                        _msg = out.toString();
                    }
                    logger.info(">>>>>>>>>>> xxl-job handle end, handlerParams:{}, _status:{}, _msg:{}",
                            new Object[]{handlerParams, _status, _msg});

                    // callback handler info
                    if (!toStop.get()) {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("log_id", log_id);
                        params.put("status", _status.name());
                        params.put("msg", _msg);
                        params.put("returnInfo", _returnInfo);
                        LogCallBack.pushLog(HttpUtil.addressToUrl(log_address), params);
                    } else {
                        HashMap<String, String> params = new HashMap<>();
                        params.put("log_id", log_id);
                        params.put("status", JobHandleStatus.FAIL.name());
                        params.put("msg", "人工手动终止[业务运行中，被强制终止]");
                        params.put("returnInfo", _returnInfo);
                        LogCallBack.pushLog(HttpUtil.addressToUrl(log_address), params);
                    }
                } else {
                    i.getAndIncrement();
                    logIdSet.clear();
                    try {
                        TimeUnit.MILLISECONDS.sleep(i.get() * 100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i.get() > 5) {
                        i.set(0);
                    }
                }
            } catch (Exception e) {
                logger.info("Worker Exception:", e);
            }
        }

        // callback trigger request in queue
        while (jobQueue != null && jobQueue.size() > 0) {
            Map<String, String> handlerData = jobQueue.poll();
            if (handlerData != null) {
                String log_address = handlerData.get(HandlerParamEnum.LOG_ADDRESS.name());
                String log_id = handlerData.get(HandlerParamEnum.LOG_ID.name());

                HashMap<String, String> params = new HashMap<>();
                params.put("log_id", log_id);
                params.put("status", JobHandleStatus.FAIL.name());
                params.put("msg", "人工手动终止[任务尚未执行，在调度队列中被终止]");
                LogCallBack.pushLog(HttpUtil.addressToUrl(log_address), params);
            }
        }

        logger.info(">>>>>>>>>>>> xxl-job handlerThrad stoped, hashCode:{}", Thread.currentThread());
    }

    public static String getLogId() {
        return contextHolder.get();
    }
}
