package com.xxl.job.core.handler.action;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.xxl.job.core.constant.ActionEnum;
import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.handler.Worker;
import com.xxl.job.core.handler.WorkerRepository;
import com.xxl.job.core.handler.annotation.ActionHandler;
import com.xxl.job.core.handler.impl.GlueJobHandler;
import com.xxl.job.core.util.CallBack;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by feiluo on 6/20/2016.
 */
@ActionHandler(ActionEnum.RUN)
public class RunActionHandler implements IActionHandler {


    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public CallBack action(Map<String, String> jobInfo) {
        // push data to queue
        String handler_glue_switch = jobInfo.get(HandlerParamEnum.GLUE_SWITCH.name());
        Worker worker;
        if ("0".equals(handler_glue_switch)) {
            // bean model
            String jobHanderName = jobInfo.get(HandlerParamEnum.EXECUTOR_HANDLER.name());
            if (jobHanderName == null || jobHanderName.trim().length() == 0) {
                return CallBack.fail("bean model handler[HANDLER_NAME] not found.");
            }
            worker = workerRepository.getWorker(jobHanderName);
            if (worker == null) {
                return CallBack.fail("handler[" + jobHanderName + "] not found.");
            }
        } else {
            // glue
            String job_group = jobInfo.get(HandlerParamEnum.JOB_GROUP.name());
            String job_name = jobInfo.get(HandlerParamEnum.JOB_NAME.name());
            if (job_group == null || job_group.trim().length() == 0 || job_name == null || job_name.trim().length() == 0) {
                return CallBack.fail("glue model handler[job group or name] is null.");
            }
            String glueHandleName = "glue_".concat(job_group).concat("_").concat(job_name);
            worker = workerRepository.getWorker(glueHandleName);
            if (worker == null) {
                worker = workerRepository.regist(glueHandleName, new GlueJobHandler(job_group, job_name));
            }
        }

        worker.submit(jobInfo);
        return CallBack.success();
    }

    /**
     * prepare Handler paramter
     *
     * @param jobInfo
     * @return java.util.Map<String, String>
     */
    public Map<String, String> prepareHandlerParamter(Map<String, String> jobInfo) {
        String jobHanderName = jobInfo.get(HandlerParamEnum.EXECUTOR_HANDLER.name());
        if (StringUtils.isBlank(jobHanderName)) {
            return jobInfo;
        }
        Optional<File> handlerParamterFile = findHandlerParamterFile(jobHanderName);
        if (!handlerParamterFile.isPresent() || !handlerParamterFile.get().exists()) {
            return jobInfo;
        }
        try {
            Configuration configuration = new Configurations().properties(handlerParamterFile.get());
            Map<String, String> resultMap = toMap(configuration);
            // replace by runtime paramter & job paramter
            resultMap.putAll(jobInfo);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        } finally {
        }
        return jobInfo;
    }

    /**
     * config to Map
     *
     * @param config
     * @return
     */
    public Map<String, String> toMap(Configuration config) {
        Map<String, String> resultMap = Maps.newHashMap();
        Iterator<String> keys = config.getKeys();
        while (keys.hasNext()) {
            String key = keys.next();
            resultMap.put(key, config.getString(key));
        }
        return resultMap;
    }


    /**
     * 查找handler对应的property
     *
     * @param jobHanderName
     * @return
     */
    public Optional<File> findHandlerParamterFile(String jobHanderName) {
        // 先从classpath:handler-paramter中读取
        File paramterFile = null;
        try {
            paramterFile = ResourceUtils.getFile(String.format("classpath:handler-paramter/%s.properties", jobHanderName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (paramterFile != null) {
            return Optional.of(paramterFile);
        }
        // 再从根目录查找
        try {
            paramterFile = ResourceUtils.getFile(String.format("classpath:%s.properties", jobHanderName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.fromNullable(paramterFile);
    }

}
