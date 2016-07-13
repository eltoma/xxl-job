package com.xxl.job.core.handler;

import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.constant.JobHandleStatus;
import com.xxl.job.core.log.LogCallBack;
import com.xxl.job.core.util.CallBack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Created by feiluo on 7/13/2016.
 */
public class Worker {

    private String jobName;
    private IJobHandler jobHandler;
    private Class<?> jobClass;
    private ExecutorService executorService;
    private Map<String, WorkderRunInfo> callBackFutureMap = new ConcurrentHashMap<>();

    public Worker(String jobName, IJobHandler jobHandler, Class<?> jobClass, ExecutorService executorService) {
        this.jobName = jobName;
        this.jobHandler = jobHandler;
        this.jobClass = jobClass;
        this.executorService = executorService;
    }

    /**
     * 提交任务
     *
     * @param jobInfo
     */
    public void submit(Map<String, String> jobInfo) {
        Future<CallBack> callBackFuture = executorService.submit(new WorkerCallable(jobHandler, jobInfo));
        String logId = jobInfo.get(HandlerParamEnum.LOG_ID.name());
        callBackFutureMap.put(logId, new WorkderRunInfo(jobInfo, callBackFuture));
        // 移除所有完成的
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
            callBackFuture.cancel(true);
            HashMap<String, String> params = new HashMap<>();
            params.put("log_id", logId);
            params.put("status", JobHandleStatus.FAIL.name());
            params.put("msg", "人工手动终止[业务运行中，被强制终止]");
            LogCallBack.pushLog(callBackFuture.getCallBackUrl(), params);
        }
        callBackFutureMap.remove(logId);
    }

    public void removeDone() {
        Set<String> keys = callBackFutureMap.keySet();
        for (String key : keys) {
            Future<CallBack> callBackFuture = callBackFutureMap.get(key);
            if (callBackFuture != null && (callBackFuture.isDone() || callBackFuture.isCancelled())) {
                callBackFutureMap.remove(key);
            }
        }
    }


}
