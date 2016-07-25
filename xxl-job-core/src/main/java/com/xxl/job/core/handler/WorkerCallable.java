package com.xxl.job.core.handler;

import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.constant.JobHandleStatus;
import com.xxl.job.core.log.LogCallBack;
import com.xxl.job.core.util.CallBack;
import com.xxl.job.core.util.HttpUtil;
import com.xxl.job.core.util.JacksonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;


/**
 * 执行内容
 *
 * @author xuxueli 2016-1-16 19:52:47
 */
public class WorkerCallable implements Callable<CallBack> {
    private static final Logger logger = LoggerFactory.getLogger(WorkerCallable.class);

    private final IJobHandler handler;
    private final static ThreadLocal<String> logId = new ThreadLocal<>();

    private Map<String, String> jobInfo;

    public WorkerCallable(IJobHandler handler, Map<String, String> jobInfo) {
        this.handler = handler;
        this.jobInfo = jobInfo;
    }

    @Override
    public CallBack call() {
        logId.set(getInfo(HandlerParamEnum.LOG_ID));
        String handler_params = getInfo(HandlerParamEnum.EXECUTOR_PARAMS);
        // parse param
        String[] handlerParams;
        if (handler_params != null && handler_params.trim().length() > 0) {
            handlerParams = handler_params.split(",");
        } else {
            handlerParams = new String[0];
        }
        CallBack callBack;
        try {
            logger.info(">>>>>>>>>>> xxl-job handle start.");
            callBack = handler.execute(handlerParams);
        } catch (Exception e) {
            logger.info("Worker Exception:", e);
            callBack = CallBack.fail("worker run error!");
        }
        pushReuslt(callBack);
        logger.info(">>>>>>>>>>> xxl-job handle end, handlerParams:{}, _status:{}, _msg:{}",
                new Object[]{handlerParams, callBack.getStatus(), callBack.getMsg()});
        return callBack;
    }

    /**
     * 推送到监控
     *
     * @param callBack
     */
    public void pushReuslt(CallBack callBack) {
        String log_address = getInfo(HandlerParamEnum.LOG_ADDRESS);
        HashMap<String, String> params = new HashMap<>();
        params.put("log_id", getInfo(HandlerParamEnum.LOG_ID));
        params.put("status", JobHandleStatus.valueOf(callBack).name());
        params.put("msg", callBack.getMsg());
        params.put("returnInfo", JacksonUtil.writeValueAsString(callBack.getData()));
        LogCallBack.pushLog(HttpUtil.addressToUrl(log_address), params);
    }

    /**
     * 读取传递的参数
     *
     * @param name
     * @return
     */
    public String getInfo(HandlerParamEnum name) {
        return jobInfo.get(name.name());
    }


    /**
     * 获取LogId
     *
     * @return
     */
    public static String getLogId() {
        return logId.get();
    }

    /**
     * 设置LogId
     *
     * @param logIdStr
     * @return
     */
    public static String setLogId(String logIdStr) {
        logId.set(logIdStr);
        return logIdStr;
    }

}
