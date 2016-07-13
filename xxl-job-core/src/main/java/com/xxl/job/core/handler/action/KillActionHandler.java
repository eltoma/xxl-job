package com.xxl.job.core.handler.action;

import com.xxl.job.core.constant.ActionEnum;
import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.handler.Worker;
import com.xxl.job.core.handler.WorkerRepository;
import com.xxl.job.core.handler.annotation.ActionHandler;
import com.xxl.job.core.util.CallBack;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;


/**
 * Created by feiluo on 6/20/2016.
 */
@ActionHandler(ActionEnum.KILL)
public class KillActionHandler implements IActionHandler {

    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public CallBack action(Map<String, String> params) {
        // kill workerBack, and create new one
        String handler_glue_switch = params.get(HandlerParamEnum.GLUE_SWITCH.name());
        String logId = params.get(HandlerParamEnum.LOG_ID.name());
        String handlerName = null;
        if ("0".equals(handler_glue_switch)) {
            String executor_handler = params.get(HandlerParamEnum.EXECUTOR_HANDLER.name());
            if (executor_handler == null) {
                return CallBack.fail("bean job , param[EXECUTOR_HANDLER] is null");
            }
            handlerName = executor_handler;
        } else {
            // glue
            String job_group = params.get(HandlerParamEnum.JOB_GROUP.name());
            String job_name = params.get(HandlerParamEnum.JOB_NAME.name());
            if (job_group == null || job_group.trim().length() == 0 || job_name == null || job_name.trim().length() == 0) {
                return CallBack.fail("glue job , param[JOB_GROUP or JOB_NAME] is null");
            }
            handlerName = "glue_".concat(job_group).concat("_").concat(job_name);
        }

        Worker worker = workerRepository.getWorker(handlerName);
        if (worker == null) {
            return CallBack.fail("job handler[" + handlerName + "] not found.");
        }
        worker.killJob(logId);
        return CallBack.success();
    }
}
