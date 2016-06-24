package com.xxl.job.core.log.reader;

import com.xxl.job.core.log.LogReaderRepository;
import com.xxl.job.core.log.XxlJobFileAppender;
import org.apache.commons.codec.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by feiluo on 6/24/2016.
 */
@LogReader(forJobHandler = LogReaderRepository.LOG_READER_DEFAULT_NAME)
public class DefaultFileLogReader {

    /**
     * support read log-file
     *
     * @param triggerDate
     * @return
     */
    @LogType
    public List<String> read(String triggerLogId, Date triggerDate) throws IOException {
        if (StringUtils.isBlank(triggerLogId) || triggerDate == null) {
            return null;
        }
        // filePath/yyyy-MM-dd/9999.log
        String fileFullName = XxlJobFileAppender.filePath + DateFormatUtils.ISO_DATE_FORMAT.format(triggerDate) + File.separator + triggerLogId.concat(".log");
        File logFile = new File(fileFullName);
        if (!logFile.exists()) {
            return Collections.EMPTY_LIST;
        }
        return FileUtils.readLines(logFile, Charsets.UTF_8);
    }

}
