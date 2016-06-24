package com.xxl.job.executor.service.joblogreader;

import com.xxl.job.core.log.reader.LogReader;
import com.xxl.job.core.log.reader.LogType;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by feiluo on 6/24/2016.
 */
@LogReader(forJobHandler = "KettleJobHandler")
public class LogReader4KettleJobHandler {


    @LogType("job_log")
    public List<? extends Serializable> readJobLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }

    @LogType("job_item_log")
    public List<? extends Serializable> readJobItemLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }

    @LogType("job_channel_log")
    public List<? extends Serializable> readJobChannelLog(String triggerLogId, Date triggerDate) throws Exception {
        return null;
    }
}
