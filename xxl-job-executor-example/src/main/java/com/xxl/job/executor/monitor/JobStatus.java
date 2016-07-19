package com.xxl.job.executor.monitor;

import java.util.Date;
import java.util.Map;

/**
 * Created by feiluo on 7/19/2016.
 */
public class JobStatus {

    public JobStatus(String jobNameHander, String jobId) {
        this.jobNameHander = jobNameHander;
        this.jobId = jobId;
    }

    private String jobNameHander;
    private String jobId;
    private boolean isDone;
    private boolean isCancel;
    private Date callTime;
    private Map<String, String> jobInfo;

    public String getJobNameHander() {
        return jobNameHander;
    }

    public void setJobNameHander(String jobNameHander) {
        this.jobNameHander = jobNameHander;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public boolean isCancel() {
        return isCancel;
    }

    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }

    public Map<String, String> getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(Map<String, String> jobInfo) {
        this.jobInfo = jobInfo;
    }

    public Date getCallTime() {
        return callTime;
    }

    public void setCallTime(Date callTime) {
        this.callTime = callTime;
    }
}
