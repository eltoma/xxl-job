package com.xxl.job.executor.service.joblogreader;

import com.xxl.job.core.log.reader.LogReader;
import com.xxl.job.core.log.reader.LogType;
import com.xxl.job.executor.loader.dao.LogKettleJobChannelMapper;
import com.xxl.job.executor.loader.dao.LogKettleJobItemMapper;
import com.xxl.job.executor.loader.dao.LogKettleJobMapper;
import com.xxl.job.executor.loader.dao.model.LogKettleJob;
import com.xxl.job.executor.loader.dao.model.LogKettleJobChannel;
import com.xxl.job.executor.loader.dao.model.LogKettleJobItem;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * read log for kettle job handler
 * <p>
 * Created by feiluo on 6/24/2016.
 */
@LogReader(forJobHandler = "KettleJobHandler")
public class LogReader4KettleJobHandler {

    @Autowired
    private LogKettleJobMapper logKettleJobMapper;
    @Autowired
    private LogKettleJobChannelMapper logKettleJobChannelMapper;
    @Autowired
    private LogKettleJobItemMapper logKettleJobItemMapper;

    @LogType("job_log")
    public List<LogKettleJob> readJobLog(String triggerLogId, Date triggerDate) throws Exception {
        return logKettleJobMapper.selectByID_LOG(Long.valueOf(triggerLogId));
    }

    @LogType("job_item_log")
    public List<LogKettleJobItem> readJobItemLog(String triggerLogId, Date triggerDate) throws Exception {
        return logKettleJobItemMapper.selectByID_LOG(Long.valueOf(triggerLogId));
    }

    @LogType("job_channel_log")
    public List<LogKettleJobChannel> readJobChannelLog(String triggerLogId, Date triggerDate) throws Exception {
        return logKettleJobChannelMapper.selectByID_LOG(Long.valueOf(triggerLogId));
    }
}
