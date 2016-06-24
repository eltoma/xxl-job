package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.handler.IJobHandler;
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
@JobHander("KettleTransHandler")
public class KettleTransHandler extends IJobHandler {

    private final static String FILE_SUFFIX_TRANSFORMATION = "ktr";
    private static transient Logger logger = LoggerFactory.getLogger(KettleTransHandler.class);

    @Override
    public CallBack execute(String... params) throws Exception {
        logger.info("[kettle job]properties init……");
        try {
            return doExecute(params);
        } catch (Exception e) {
            logger.error("[kettle job]execute exception!\n{}", e);
            return CallBack.fail();
        }
    }

    public CallBack doExecute(String... params) throws KettleException {
        KettleJobParamParser kettleJobParamParser = new KettleJobParamParser(params);
        if (FILE_SUFFIX_TRANSFORMATION.equalsIgnoreCase(kettleJobParamParser.getFileSuffix())) {
            logger.info("[kettle transformation:{}]execute kettle start……filePath:{}\tVariableMap:{}",
                    new Object[]{kettleJobParamParser.getFilePath(), kettleJobParamParser.getVariableMap().toString()});
            return runTransformation(kettleJobParamParser);
        } else {
            logger.error("file type not support. full path[{}]", kettleJobParamParser.getFilePath());
            return CallBack.fail("file type not support");
        }
    }

    /**
     * 运行本地ktr文件
     *
     * @param kettleJobParamAdvisor
     * @return
     */
    public CallBack runTransformation(KettleJobParamParser kettleJobParamAdvisor) throws KettleException {
        // 初始化任务
        KettleEnvironment.init();
        EnvUtil.environmentInit();
        TransMeta transMeta = new TransMeta(kettleJobParamAdvisor.getFilePath());
        transMeta.setCapturingStepPerformanceSnapShots(true);
        Trans trans = new Trans(transMeta);
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
            return CallBack.failWithData(trans.getBatchId());
        }
        logger.info("[kettle Transformation]run info. {}", new Object[]{result == null ? "" : result.getLogText()});
        return CallBack.successWithData(trans.getBatchId());
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
