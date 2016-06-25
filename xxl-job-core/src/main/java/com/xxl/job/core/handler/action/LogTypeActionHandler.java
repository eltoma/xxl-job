package com.xxl.job.core.handler.action;

import com.xxl.job.core.constant.ActionEnum;
import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.handler.annotation.ActionHandler;
import com.xxl.job.core.log.LogReaderRepository;
import com.xxl.job.core.util.CallBack;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by feiluo on 6/20/2016.
 */
@ActionHandler(ActionEnum.LOG_TYPE)
public class LogTypeActionHandler implements IActionHandler {

    @Autowired
    private LogReaderRepository logReaderRepository;

    @Override
    public CallBack action(Map<String, String> params) {
        String jobHandlerName = params.get(HandlerParamEnum.JOB_HANDLER_NAME.name());
        if (StringUtils.isBlank(jobHandlerName)) {
            return CallBack.fail("job hander name required.");
        }
        return CallBack.successWithData(logReaderRepository.getLogType(jobHandlerName));
    }
}
