package com.xxl.job.core.handler.action;

import com.xxl.job.core.constant.ActionEnum;
import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.handler.annotation.ActionHandler;
import com.xxl.job.core.log.XxlJobFileAppender;
import com.xxl.job.core.util.CallBack;

import java.util.Date;
import java.util.Map;

/**
 * Created by feiluo on 6/20/2016.
 */
@ActionHandler(ActionEnum.LOG)
public class LogActionHandler implements IActionHandler {


    @Override
    public CallBack action(Map<String, String> params) {
        String log_id = params.get(HandlerParamEnum.LOG_ID.name());
        String log_date = params.get(HandlerParamEnum.LOG_DATE.name());
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
        return CallBack.success(XxlJobFileAppender.readLog(triggerDate, log_id));
    }
}
