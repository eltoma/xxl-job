package com.xxl.job.executor.monitor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xxl.job.core.handler.WorkderRunInfo;
import com.xxl.job.core.handler.Worker;
import com.xxl.job.core.handler.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by feiluo on 7/19/2016.
 */
@Service
public class WorkerMonitorService {

    @Autowired
    private WorkerRepository workerRepository;


    /**
     * 获取所有Handler Name
     *
     * @return
     */
    public Set<String> getAllJobHandlerName() {
        return workerRepository.getHandlerNames();
    }

    /**
     * 线程池概要
     *
     * @return
     */
    public PoolOverview overview(String jobHandlerName) {
        Worker worker = workerRepository.getWorker(jobHandlerName);
        ThreadPoolExecutor executor = worker.getExecutorService();
        return new PoolOverview(executor.getActiveCount(), executor.getCompletedTaskCount(), executor.getTaskCount(), executor.getPoolSize());
    }

    /**
     * 线程池中的有记录的任务信息
     *
     * @param jobHandlerName
     * @return
     */
    public List<JobStatus> listExsistTaskInfo(String jobHandlerName) {
        Worker worker = workerRepository.getWorker(jobHandlerName);
        Map<String, WorkderRunInfo> callBackFutureMap = worker.getCallBackFutureMap();
        List<JobStatus> jobStatuses = Lists.newArrayList();
        WorkderRunInfo value;
        for (Map.Entry<String, WorkderRunInfo> entry : callBackFutureMap.entrySet()) {
            value = entry.getValue();
            JobStatus jobStatus = new JobStatus(jobHandlerName, entry.getKey());
            jobStatus.setCancel(value.isCancelled());
            jobStatus.setDone(value.isDone());
            jobStatus.setJobInfo(value.getJobInfo());
            jobStatus.setCallTime(value.getCallTime());
            jobStatuses.add(jobStatus);
        }
        return jobStatuses;
    }

    /**
     * 线程池中的在运行的任务信息
     *
     * @param jobHandlerName
     * @return
     */
    public List<JobStatus> listActiveTaskInfo(String jobHandlerName) {
        Worker worker = workerRepository.getWorker(jobHandlerName);
        Map<String, WorkderRunInfo> callBackFutureMap = worker.getCallBackFutureMap();
        List<JobStatus> jobStatuses = Lists.newArrayList();
        WorkderRunInfo value;
        for (Map.Entry<String, WorkderRunInfo> entry : callBackFutureMap.entrySet()) {
            value = entry.getValue();
            if (value.isDone() || value.isCancelled()) {
                continue;
            }
            JobStatus jobStatus = new JobStatus(jobHandlerName, entry.getKey());
            jobStatus.setCancel(value.isCancelled());
            jobStatus.setDone(value.isDone());
            jobStatus.setJobInfo(value.getJobInfo());
            jobStatus.setCallTime(value.getCallTime());
            jobStatuses.add(jobStatus);
        }
        return jobStatuses;
    }

    /**
     * 获取执行信息
     *
     * @param jobId
     * @return
     */
    public Map<String, String> getJobInfo(String jobHandlerName, String jobId) {
        Worker worker = workerRepository.getWorker(jobHandlerName);
        WorkderRunInfo workderRunInfo = worker.getCallBackFutureMap().get(jobId);
        if (workderRunInfo == null) {
            return Collections.EMPTY_MAP;
        }
        return workderRunInfo.getJobInfo();
    }

}
