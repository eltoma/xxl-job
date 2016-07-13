package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.WorkerCallable;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.handler.annotation.JobHanderRepository;
import com.xxl.job.core.util.CallBack;
import com.xxl.job.executor.loader.dao.*;
import com.xxl.job.executor.service.parser.KettleJobParamParser;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by feiluo on 6/15/2016.
 */
@JobHanderRepository(min = 5, max = 15)
@JobHander("KettleTransHandler")
public class KettleTransHandler extends IJobHandler {

    private final static String FILE_SUFFIX_TRANSFORMATION = "ktr";
    private static transient Logger logger = LoggerFactory.getLogger(KettleTransHandler.class);

    @Autowired
    private LogKettleTranMapper logKettleTranMapper;
    @Autowired
    private LogKettleTranRunMapper logKettleTranRunMapper;
    @Autowired
    private LogKettleTranStepMapper logKettleTranStepMapper;
    @Autowired
    private LogKettleTranChannelMapper logKettleTranChannelMapper;
    @Autowired
    private LogKettleTranMetricsMapper logKettleTranMetricsMapper;

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

    @Override
    public CallBack postExecute(CallBack callBack, String... params) throws Exception {
        //执行后更新，记录对应的log_id
        updateTransLogIdByID_Batch(Long.valueOf(WorkerCallable.getLogId()), (Long) callBack.getData());
        return super.postExecute(callBack, params);
    }

    /**
     * 更新对应trans的LOG_ID
     *
     * @param ID_LOG
     * @param ID_BATCH
     */
    @Transactional
    public void updateTransLogIdByID_Batch(long ID_LOG, long ID_BATCH) {
        logKettleTranMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
        logKettleTranRunMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
        logKettleTranStepMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
        logKettleTranChannelMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
        logKettleTranMetricsMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
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
        // 获取kettle日志
        logger.info(KettleLogStore.getAppender().getBuffer().toString());
        // 清理日志
        KettleLogStore.getAppender().clear();
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
