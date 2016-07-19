package com.xxl.job.executor.monitor;

/**
 * Created by feiluo on 7/19/2016.
 */
public class PoolOverview {


    public PoolOverview(long currentActive, long completedJobCount, long jobSize, long workerSize) {
        this.currentActive = currentActive;
        this.completedJobCount = completedJobCount;
        this.jobSize = jobSize;
        this.workerSize = workerSize;
    }

    // 当前激活的
    private long currentActive;
    // 当前所有的job数
    private long jobSize;
    // 实际线程数
    private long workerSize;
    // 已完成数量
    private long completedJobCount;

    public long getCurrentActive() {
        return currentActive;
    }

    public long getCompletedJobCount() {
        return completedJobCount;
    }

    public long getJobSize() {
        return jobSize;
    }

    public long getWorkerSize() {
        return workerSize;
    }

}
