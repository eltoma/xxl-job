package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.IJobKillHook;
import com.xxl.job.core.handler.Worker;
import com.xxl.job.core.handler.WorkerCallable;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.handler.annotation.JobHanderRepository;
import com.xxl.job.core.util.CallBack;
import com.xxl.job.executor.loader.dao.*;
import com.xxl.job.executor.service.parser.KettleJobParamParser;
import org.apache.log4j.Level;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.Result;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.KettleLoggingEvent;
import org.pentaho.di.core.logging.KettleLoggingEventListener;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by feiluo on 6/15/2016.
 */
@JobHanderRepository(min = 5, max = 15)
@JobHander("KettleJobHandler")
public class KettleJobHandler extends IJobHandler {

    private final static String FILE_SUFFIX_JOB = "kjb";
    private static transient Logger logger = LoggerFactory.getLogger(KettleJobHandler.class);

    @Autowired
    private LogKettleJobMapper logKettleJobMapper;
    @Autowired
    private LogKettleJobChannelMapper logKettleJobChannelMapper;
    @Autowired
    private LogKettleJobItemMapper logKettleJobItemMapper;

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
            return CallBack.fail("execute exception");
        }
    }

    @Override
    public CallBack postExecute(CallBack callBack, String... params) throws Exception {
        //执行后更新，记录对应的log_id
        updateLogIdByID_BATCH(Long.valueOf(WorkerCallable.getLogId()), (Long) callBack.getData());
        return super.postExecute(callBack, params);
    }

    @Transactional
    public void updateLogIdByID_BATCH(long ID_LOG, long JOB_ID_BATCH) {
        updateJobLogIdByID_BATCH(ID_LOG, JOB_ID_BATCH);
        List<Integer> transID_BATCHs = logKettleTranChannelMapper.selectBatchIdByJobID_BATCH(JOB_ID_BATCH);
        for (Integer transID_BATCH : transID_BATCHs) {
            updateTransLogIdByID_Batch(ID_LOG, transID_BATCH);
        }
    }

    /**
     * 更新对应job的LOG_ID
     *
     * @param ID_LOG
     * @param ID_BATCH
     */
    public void updateJobLogIdByID_BATCH(long ID_LOG, long ID_BATCH) {
        logKettleJobMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
        logKettleJobChannelMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
        logKettleJobItemMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
    }

    /**
     * 更新JOB对应trans的LOG_ID
     *
     * @param ID_LOG
     * @param ID_BATCH
     */
    public void updateTransLogIdByID_Batch(long ID_LOG, long ID_BATCH) {
        logKettleTranMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
        logKettleTranRunMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
        logKettleTranStepMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
        logKettleTranChannelMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
        logKettleTranMetricsMapper.updateID_LOGByID_BATCH(ID_LOG, ID_BATCH);
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
        final String logId = WorkerCallable.getLogId();
        // 获取kettle日志
        KettleLogStore.getAppender().addLoggingEventListener(new KettleLoggingEventListener() {
            @Override
            public void eventAdded(KettleLoggingEvent event) {
                WorkerCallable.setLogId(logId);
                String msg = event.getMessage().toString();
                switch (event.getLevel()) {
                    case ERROR:
                        logger.error(msg);
                        break;
                    case DEBUG:
                    case ROWLEVEL:
                        logger.debug(msg);
                        break;
                    default:
                        logger.info(msg);
                        break;
                }
            }
        });
        // jobname 是Job脚本的路径及名称
        JobMeta jobMeta = new JobMeta(kettleJobParamAdvisor.getFilePath(), null);
        final Job job = new Job(null, jobMeta);
        // job.setVariable("id", params[0]);
        // job.setVariable("dt", params[1]);
        setVariable(job, kettleJobParamAdvisor.getVariableMap());
        job.start();
        long batchId = job.getBatchId();
        // 注册job关闭时，资源释放
        Worker.registerKillAction(WorkerCallable.getLogId(), new IJobKillHook() {
            @Override
            public void destroy() {
                job.stopAll();
            }
        });
        try {
            job.waitUntilFinished();
            Result result = job.getResult();

            // 获取kettle日志
//        logger.info(KettleLogStore.getAppender().getBuffer().toString());
            // 清理日志
//        KettleLogStore.getAppender().clear();
            if (job.getErrors() > 0) {
                logger.error("[kettle job]run error. {}", new Object[]{result == null ? "" : result.getLogText()});
                return CallBack.failWithData(job.getBatchId());
            }
            logger.info("[kettle job]run info. {}", new Object[]{result == null ? "" : result.getLogText()});
            // Now the job task is finished, mark it as finished.
            job.setFinished(true);
            // Cleanup the parameters used by the job. Post that readLog GC.
            jobMeta.eraseParameters();
            job.eraseParameters();
            return CallBack.successWithData(batchId);
        } catch (Exception e) {
            logger.error("Kettle job error.", e);
            return CallBack.failWithData(batchId);
        }
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
