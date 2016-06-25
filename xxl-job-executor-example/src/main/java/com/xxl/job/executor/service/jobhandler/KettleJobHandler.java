package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.Worker;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.util.CallBack;
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

import java.util.Map;

/**
 * Created by feiluo on 6/15/2016.
 */
@JobHander("KettleJobHandler")
public class KettleJobHandler extends IJobHandler {

    private final static String FILE_SUFFIX_JOB = "kjb";
    private static transient Logger logger = LoggerFactory.getLogger(KettleJobHandler.class);

    @Override
    public CallBack execute(String... params) throws Exception {
        logger.info("[kettle job]properties init……");
        try {
            return doExecute(params);
        } catch (Exception e) {
            logger.error("[kettle job]execute exception!\n{}", e);
            return CallBack.fail("execute exception");
        }
    }

    @Override
    public CallBack postExecute(CallBack callBack, String... params) throws Exception {
        //todo 执行后更新，记录对应的log_id
//        Worker.getLogId();
        return super.postExecute(callBack, params);
    }

    public CallBack doExecute(String... params) throws KettleException {
        KettleJobParamParser kettleJobParamParser = new KettleJobParamParser(params);

        if (FILE_SUFFIX_JOB.equalsIgnoreCase(kettleJobParamParser.getFileSuffix())) {
            logger.info("[kettle job:{}]execute kettle start……filePath:{}\tVariableMap:{}",
                    new Object[]{kettleJobParamParser.getFilePath(), kettleJobParamParser.getVariableMap().toString()});
            return runJob(kettleJobParamParser);
        } else {
            logger.error("file type not support. full path[{}]", kettleJobParamParser.getFilePath());
            return CallBack.fail("file type not support");
        }
    }


    /**
     * 运行本地kjb文件
     *
     * @param kettleJobParamAdvisor
     * @return
     */
    public CallBack runJob(KettleJobParamParser kettleJobParamAdvisor) throws KettleException {
        KettleEnvironment.init();
        // jobname 是Job脚本的路径及名称
        JobMeta jobMeta = new JobMeta(kettleJobParamAdvisor.getFilePath(), null);
        Job job = new Job(null, jobMeta);
        // job.setVariable("id", params[0]);
        // job.setVariable("dt", params[1]);
        setVariable(job, kettleJobParamAdvisor.getVariableMap());
        job.start();
        job.waitUntilFinished();
        Result result = job.getResult();
        if (job.getErrors() > 0) {
            logger.error("[kettle Transformation]run error. {}", new Object[]{result == null ? "" : result.getLogText()});
            return CallBack.failWithData(job.getBatchId());
        }
        logger.info("[kettle Transformation]run info. {}", new Object[]{result == null ? "" : result.getLogText()});
        // Now the job task is finished, mark it as finished.
        job.setFinished(true);
        // Cleanup the parameters used by the job. Post that readLog GC.
        jobMeta.eraseParameters();
        job.eraseParameters();
        return CallBack.successWithData(job.getBatchId());
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
