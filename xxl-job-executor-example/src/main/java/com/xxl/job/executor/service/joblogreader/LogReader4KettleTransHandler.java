package com.xxl.job.executor.service.joblogreader;

import com.xxl.job.core.log.annotation.LogReader;
import com.xxl.job.core.log.annotation.LogType;
import com.xxl.job.executor.loader.dao.*;
import com.xxl.job.executor.loader.dao.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * read log for kettle trans handler
 * <p>
 * Created by feiluo on 6/24/2016.
 */
@LogReader(forJobHandler = "KettleTransHandler")
public class LogReader4KettleTransHandler {

    @Autowired
    private LogKettleTranMapper logKettleTranMapper;
    @Autowired
    private LogKettleTranRunMapper logKettleTranRunMapper;
    @Autowired
    private LogKettleTranStepMapper logKettleTranStepMapper;
    @Autowired
    private LogKettleTranMetricsMapper logKettleTranMetricsMapper;
    @Autowired
    private LogKettleTranChannelMapper logKettleTranChannelMapper;

    @LogType("tran_log")
    public List<LogKettleTran> readTranLog(String triggerLogId, Date triggerDate) throws Exception {
        return logKettleTranMapper.selectByID_LOG(Long.valueOf(triggerLogId));
    }

    @LogType("tran_metrics_log")
    public List<LogKettleTranMetrics> readTranMetricsLog(String triggerLogId, Date triggerDate) throws Exception {
        return logKettleTranMetricsMapper.selectByID_LOG(Long.valueOf(triggerLogId));
    }

    @LogType("tran_channel_log")
    public List<LogKettleTranChannel> readTranChannelLog(String triggerLogId, Date triggerDate) throws Exception {
        return logKettleTranChannelMapper.selectByID_LOG(Long.valueOf(triggerLogId));
    }

    @LogType("tran_run_log")
    public List<LogKettleTranRun> readTranRunLog(String triggerLogId, Date triggerDate) throws Exception {
        return logKettleTranRunMapper.selectByID_LOG(Long.valueOf(triggerLogId));
    }

    @LogType("tran_step_log")
    public List<LogKettleTranStep> readTranStepLog(String triggerLogId, Date triggerDate) throws Exception {
        return logKettleTranStepMapper.selectByID_LOG(Long.valueOf(triggerLogId));
    }
}
