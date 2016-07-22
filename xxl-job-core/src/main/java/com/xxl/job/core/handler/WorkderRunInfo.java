package com.xxl.job.core.handler;

import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.util.CallBack;
import com.xxl.job.core.util.HttpUtil;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by feiluo on 7/13/2016.
 */
public class WorkderRunInfo implements Future<CallBack> {


    private Date callTime;
    private String logId;
    private String callBackUrl;
    private String jobHandlerName;
    private IJobKillHook jobKillHook;
    private Map<String, String> jobInfo;
    private Future<CallBack> callBackFuture;

    public WorkderRunInfo(String jobHandlerName, Map<String, String> jobInfo, Future<CallBack> callBackFuture) {
        this.jobInfo = jobInfo;
        this.callBackFuture = callBackFuture;
        this.logId = jobInfo.get(HandlerParamEnum.LOG_ID.name());
        this.callBackUrl = HttpUtil.addressToUrl(jobInfo.get(HandlerParamEnum.LOG_ADDRESS.name()));
        this.callTime = new Date();
    }

    public Date getCallTime() {
        return callTime;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public Map<String, String> getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(Map<String, String> jobInfo) {
        this.jobInfo = jobInfo;
    }

    public Future<CallBack> getCallBackFuture() {
        return callBackFuture;
    }

    public void setCallBackFuture(Future<CallBack> callBackFuture) {
        this.callBackFuture = callBackFuture;
    }

    public IJobKillHook getJobKillHook() {
        return jobKillHook;
    }

    public void setJobKillHook(IJobKillHook jobKillHook) {
        this.jobKillHook = jobKillHook;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return callBackFuture.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return callBackFuture.isCancelled();
    }

    @Override
    public boolean isDone() {
        return callBackFuture.isDone();
    }

    @Override
    public CallBack get() throws InterruptedException, ExecutionException {
        return callBackFuture.get();
    }

    @Override
    public CallBack get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return callBackFuture.get(timeout, unit);
    }
}
