package com.xxl.job.core.handler.action;

import com.google.common.base.Optional;
import com.xxl.job.core.constant.ActionEnum;
import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.handler.ActionHandlerRepository;
import com.xxl.job.core.handler.annotation.ActionHandler;
import com.xxl.job.core.log.LogReaderRepository;
import com.xxl.job.core.util.CallBack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * Created by feiluo on 6/20/2016.
 */
@ActionHandler(ActionEnum.LOG)
public class LogActionHandler implements IActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(LogActionHandler.class);
    @Autowired
    private LogReaderRepository logReaderRepository;

    @Override
    public CallBack action(Map<String, String> params) {
        String log_id = params.get(HandlerParamEnum.LOG_ID.name());
        String log_date = params.get(HandlerParamEnum.LOG_DATE.name());
        String jobHandlerName = params.get(HandlerParamEnum.JOB_HANDLER_NAME.name());
        String logType = params.get(HandlerParamEnum.LOG_TYPE.name());
        if (log_id == null || log_date == null) {
            return CallBack.fail("LOG_ID | LOG_DATE can not be null.");
        }
        int logId = -1;
        Date triggerDate = null;
        try {
            logId = Integer.valueOf(log_id);
            triggerDate = new Date(Long.valueOf(log_date));
        } catch (Exception e) {
        }
        if (logId <= 0 || triggerDate == null) {
            return CallBack.fail("LOG_ID | LOG_DATE parse error.");
        }
        try {
            Optional<Object> objectOptional = logReaderRepository.readLog(jobHandlerName, logType, log_id, triggerDate);
            return objectOptional.isPresent() ? CallBack.successWithData(objectOptional.get()) : CallBack.success();
        } catch (Exception e) {
            logger.error("read log error.", e);
            return CallBack.fail("read log error.");
        }
    }
}
