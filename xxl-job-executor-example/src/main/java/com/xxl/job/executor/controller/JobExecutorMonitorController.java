package com.xxl.job.executor.controller;

import com.xxl.job.executor.monitor.JobStatus;
import com.xxl.job.executor.monitor.PoolOverview;
import com.xxl.job.executor.monitor.WorkerMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by feiluo on 7/19/2016.
 */
@RestController
@RequestMapping("executor/monitor")
public class JobExecutorMonitorController {

    @Autowired
    private WorkerMonitorService workerMonitorService;

    /**
     * 线程池概要
     *
     * @return
     */
    @RequestMapping("overview/jobHandlerNames")
    public Set<String> getAllJobHandlerName() {
        return workerMonitorService.getAllJobHandlerName();
    }

    /**
     * 线程池概要
     *
     * @return
     */
    @RequestMapping("overview/{jobHandlerName}")
    public PoolOverview overview(@PathVariable String jobHandlerName) {
        return workerMonitorService.overview(jobHandlerName);
    }

    /**
     * 线程池中的有记录的任务信息
     *
     * @param jobHandlerName
     * @return
     */
    @RequestMapping("listTaskInfoAllExsist/{jobHandlerName}")
    public List<JobStatus> listExsistsTaskInfo(@PathVariable String jobHandlerName) {
        return workerMonitorService.listExsistTaskInfo(jobHandlerName);
    }

    /**
     * 线程池中的在运行的任务信息
     *
     * @param jobHandlerName
     * @return
     */
    @RequestMapping("listTaskInfoIsActive/{jobHandlerName}")
    public List<JobStatus> listActive(@PathVariable String jobHandlerName) {
        return workerMonitorService.listActiveTaskInfo(jobHandlerName);
    }

    /**
     * 获取执行信息
     *
     * @param jobId
     * @return
     */
    @RequestMapping("getJobInfo/{jobHandlerName}/{jobId}")
    public Map<String, String> getJobInfo(@PathVariable String jobHandlerName, @PathVariable String jobId) {
        return workerMonitorService.getJobInfo(jobHandlerName, jobId);
    }
}
