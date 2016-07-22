package com.xxl.job.core.handler;

import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.constant.JobHandleStatus;
import com.xxl.job.core.handler.annotation.JobHanderRepository;
import com.xxl.job.core.log.LogCallBack;
import com.xxl.job.core.util.CallBack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by feiluo on 7/13/2016.
 */
public class Worker {

    private String jobHandlerName;
    private IJobHandler jobHandler;
    private Class<?> jobClass;
    private ThreadPoolExecutor executorService;
    private static Map<String, WorkderRunInfo> callBackFutureMap = new ConcurrentHashMap<>();

    public Worker(String jobHandlerName, IJobHandler jobHandler, Class<?> jobClass) {
        this.jobHandlerName = jobHandlerName;
        this.jobHandler = jobHandler;
        this.jobClass = jobClass;
        init();
    }

    /**
     * 初始化线程池
     */
    public void init() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        JobHanderRepository jobHanderRepository = jobClass.getAnnotation(JobHanderRepository.class);
        if (jobHanderRepository == null) {
            this.executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        } else {
            this.executorService = new ThreadPoolExecutor(jobHanderRepository.min(), jobHanderRepository.max(), 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        }
    }

    /**
     * 提交任务
     *
     * @param jobInfo
     */
    public void submit(Map<String, String> jobInfo) {
        Future<CallBack> callBackFuture = executorService.submit(new WorkerCallable(jobHandler, jobInfo));
        String logId = jobInfo.get(HandlerParamEnum.LOG_ID.name());
        callBackFutureMap.put(logId, new WorkderRunInfo(jobHandlerName, jobInfo, callBackFuture));
        removeDone();
    }


    /**
     * 杀死job
     *
     * @param logId
     */
    public void killJob(String logId) {
        WorkderRunInfo callBackFuture = callBackFutureMap.get(logId);
        if (callBackFuture == null) {
            return;
        }
        if (!(callBackFuture.isDone() || callBackFuture.isCancelled())) {
            if (callBackFuture.getJobKillHook() != null) {
                callBackFuture.getJobKillHook().destroy();
            }
            callBackFuture.cancel(true);
            HashMap<String, String> params = new HashMap<>();
            params.put("log_id", logId);
            params.put("status", JobHandleStatus.FAIL.name());
            params.put("msg", "人工手动终止[业务运行中，被强制终止]");
            LogCallBack.pushLog(callBackFuture.getCallBackUrl(), params);
        }
        callBackFutureMap.remove(logId);
    }

    /**
     * 移除所有完成的
     */
    public void removeDone() {
        Set<String> keys = callBackFutureMap.keySet();
        for (String key : keys) {
            Future<CallBack> callBackFuture = callBackFutureMap.get(key);
            if (callBackFuture != null && (callBackFuture.isDone() || callBackFuture.isCancelled())) {
                callBackFutureMap.remove(key);
            }
        }
    }


    /**
     * 注册job kill 动作
     *
     * @param jobId
     * @param jobKillHook
     */
    public static void registerKillAction(String jobId, IJobKillHook jobKillHook) {
        WorkderRunInfo workderRunInfo = callBackFutureMap.get(jobId);
        if (workderRunInfo != null) {
            workderRunInfo.setJobKillHook(jobKillHook);
        }
    }

    public String getJobHandlerName() {
        return jobHandlerName;
    }

    public IJobHandler getJobHandler() {
        return jobHandler;
    }

    public Class<?> getJobClass() {
        return jobClass;
    }

    public ThreadPoolExecutor getExecutorService() {
        return executorService;
    }

    public Map<String, WorkderRunInfo> getCallBackFutureMap() {
        return callBackFutureMap;
    }
}
