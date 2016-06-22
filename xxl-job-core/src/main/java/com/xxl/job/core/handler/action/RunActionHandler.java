package com.xxl.job.core.handler.action;

import com.xxl.job.core.constant.ActionEnum;
import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.handler.Worker;
import com.xxl.job.core.handler.WorkerRepository;
import com.xxl.job.core.handler.annotation.ActionHandler;
import com.xxl.job.core.handler.impl.GlueJobHandler;
import com.xxl.job.core.util.CallBack;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by feiluo on 6/20/2016.
 */
@ActionHandler(ActionEnum.RUN)
public class RunActionHandler implements IActionHandler {


    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public CallBack action(Map<String, String> params) {
        // push data to queue
        String handler_glue_switch = params.get(HandlerParamEnum.GLUE_SWITCH.name());
        Worker worker;
        if ("0".equals(handler_glue_switch)) {
            // bean model
            String jobHanderName = params.get(HandlerParamEnum.EXECUTOR_HANDLER.name());
            if (jobHanderName == null || jobHanderName.trim().length() == 0) {
                return CallBack.fail("bean model handler[HANDLER_NAME] not found.");
            }
            worker = workerRepository.getWorker(jobHanderName);
            if (worker == null) {
                return CallBack.fail("handler[" + jobHanderName + "] not found.");
            }
        } else {
            // glue
            String job_group = params.get(HandlerParamEnum.JOB_GROUP.name());
            String job_name = params.get(HandlerParamEnum.JOB_NAME.name());
            if (job_group == null || job_group.trim().length() == 0 || job_name == null || job_name.trim().length() == 0) {
                return CallBack.fail("glue model handler[job group or name] is null.");
            }
            String glueHandleName = "glue_".concat(job_group).concat("_").concat(job_name);
            worker = workerRepository.getWorker(glueHandleName);
            if (worker == null) {
                worker = workerRepository.regist(glueHandleName, new GlueJobHandler(job_group, job_name));
            }
        }

        worker.pushJob(params);
        return CallBack.success();
    }
}
