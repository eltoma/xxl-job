package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.Worker;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobFileAppender;
import com.xxl.job.executor.service.parser.KettleJobParamParser;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by feiluo on 6/15/2016.
 */
@JobHander
public class KettleJobHandler extends IJobHandler {

    private final static String FILE_SUFFIX_JOB = "kjb";
    private final static String FILE_SUFFIX_TRANSFORMATION = "ktr";
    private static transient Logger logger = LoggerFactory.getLogger(KettleJobHandler.class);

    @Override
    public JobHandleStatus execute(String... params) throws Exception {
        logger.info("[kettle job]properties init……");
        try {
            return doExecute(params);
        } catch (Exception e) {
            logger.error("[kettle job]execute exception!\n{}", e);
            return JobHandleStatus.FAIL;
        }
    }

    public JobHandleStatus doExecute(String... params) throws KettleException {
        KettleJobParamParser kettleJobParamParser = new KettleJobParamParser(params);

        if (FILE_SUFFIX_JOB.equalsIgnoreCase(kettleJobParamParser.getFileSuffix())) {
            logger.info("[kettle job:{}]execute kettle start……filePath:{}\tVariableMap:{}",
                    new Object[]{kettleJobParamParser.getFilePath(), kettleJobParamParser.getVariableMap().toString()});
            runJob(kettleJobParamParser);
            return JobHandleStatus.SUCCESS;
        } else if (FILE_SUFFIX_TRANSFORMATION.equalsIgnoreCase(kettleJobParamParser.getFileSuffix())) {
            logger.info("[kettle transformation:{}]execute kettle start……filePath:{}\tVariableMap:{}",
                    new Object[]{kettleJobParamParser.getFilePath(), kettleJobParamParser.getVariableMap().toString()});
            runTransformation(kettleJobParamParser);
            return JobHandleStatus.SUCCESS;
        } else {
            logger.error("unkown file suffix. file path is {}", kettleJobParamParser.getFilePath());
            return JobHandleStatus.FAIL;
        }
    }

    /**
     * 运行本地ktr文件
     *
     * @param kettleJobParamAdvisor
     * @return
     */
    public JobHandleStatus runTransformation(KettleJobParamParser kettleJobParamAdvisor) throws KettleException {
        // 初始化任务
        KettleEnvironment.init();
        EnvUtil.environmentInit();
        TransMeta transMeta = new TransMeta(kettleJobParamAdvisor.getFilePath());
        transMeta.setCapturingStepPerformanceSnapShots(true);
        Trans trans = new Trans(transMeta);
        trans.setBatchId(Long.valueOf(Worker.getLogId()));
        trans.setMonitored(true);
        trans.setInitializing(true);
        trans.setPreparing(true);
        trans.setRunning(true);
        trans.setSafeModeEnabled(true);
        // 设置参数 在kettle文件中sql语句的设置 SELECT * FROM xx where xx=${stnlevel}
        // sql参数必须一一对应
        // trans.setVariable("stnlevel", "2");
        setVariable(trans, kettleJobParamAdvisor.getVariableMap());
        trans.execute(null);
        // 等待转换执行结束
        trans.waitUntilFinished();
        Result result = trans.getResult();
        if (trans.getErrors() > 0) {
            logger.error("[kettle Transformation]run error. {}", new Object[]{result == null ? "" : result.getLogText()});
            return JobHandleStatus.FAIL;
        }
        logger.error("[kettle Transformation]run info. {}", new Object[]{result == null ? "" : result.getLogText()});
        return JobHandleStatus.SUCCESS;
    }


    /**
     * 运行本地kjb文件
     *
     * @param kettleJobParamAdvisor
     * @return
     */
    public JobHandleStatus runJob(KettleJobParamParser kettleJobParamAdvisor) throws KettleException {
        KettleEnvironment.init();
        // jobname 是Job脚本的路径及名称
        JobMeta jobMeta = new JobMeta(kettleJobParamAdvisor.getFilePath(), null);
        Job job = new Job(null, jobMeta);
        job.setBatchId(Long.valueOf(Worker.getLogId()));
        // job.setVariable("id", params[0]);
        // job.setVariable("dt", params[1]);
        setVariable(job, kettleJobParamAdvisor.getVariableMap());
        job.start();
        job.waitUntilFinished();
        Result result = job.getResult();
        if (job.getErrors() > 0) {
            logger.error("[kettle Transformation]run error. {}", new Object[]{result == null ? "" : result.getLogText()});
            return JobHandleStatus.FAIL;
        }
        logger.error("[kettle Transformation]run info. {}", new Object[]{result == null ? "" : result.getLogText()});
        // Now the job task is finished, mark it as finished.
        job.setFinished(true);
        // Cleanup the parameters used by the job. Post that invoke GC.
        jobMeta.eraseParameters();
        job.eraseParameters();
        return JobHandleStatus.SUCCESS;
    }

    public VariableSpace setVariable(VariableSpace variableSpace, Map<String, String> variableMap) {
        if (variableMap != null) {
            for (Map.Entry<String, String> entry : variableMap.entrySet()) {
                variableSpace.setVariable(entry.getKey(), entry.getValue());
            }
        }
        return variableSpace;
    }

}
