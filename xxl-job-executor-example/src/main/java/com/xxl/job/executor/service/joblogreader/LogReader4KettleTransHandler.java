package com.xxl.job.executor.service.joblogreader;

import com.xxl.job.core.log.reader.LogReader;
import com.xxl.job.core.log.reader.LogType;
import com.xxl.job.executor.loader.dao.*;
import com.xxl.job.executor.loader.dao.model.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
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
        return null;
    }

    @LogType("tran_metrics_log")
    public List<LogKettleTranMetrics> readTranMetricsLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }

    @LogType("tran_channel_log")
    public List<LogKettleTranChannel> readTranChannelLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }

    @LogType("tran_run_log")
    public List<LogKettleTranRun> readTranRunLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }

    @LogType("tran_step_log")
    public List<LogKettleTranStep> readTranStepLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }
}
