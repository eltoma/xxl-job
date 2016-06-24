package com.xxl.job.executor.service.joblogreader;

import com.xxl.job.core.log.reader.LogReader;
import com.xxl.job.core.log.reader.LogType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by feiluo on 6/24/2016.
 */
@LogReader(forJobHandler = "KettleTransHandler")
public class LogReader4KettleTransHandler {


    @LogType("tran_log")
    public List<? extends Serializable> readTranLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }

    @LogType("tran_metrics_log")
    public List<? extends Serializable> readTranMetricsLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }

    @LogType("tran_channel_log")
    public List<? extends Serializable> readTranChannelLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }

    @LogType("tran_run_log")
    public List<? extends Serializable> readTranRunLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }

    @LogType("tran_step_log")
    public List<? extends Serializable> readTranStepLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }
}
