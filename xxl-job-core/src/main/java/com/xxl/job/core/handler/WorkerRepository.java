package com.xxl.job.core.handler;

import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.handler.annotation.JobHanderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by feiluo on 6/22/2016.
 */
@Service
public class WorkerRepository implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(WorkerRepository.class);
    private static final ConcurrentHashMap<String, Worker> workerMap = new ConcurrentHashMap<>();

    /**
     * regist handler
     *
     * @param jobHandlerName
     * @param handler
     */
    public Worker regist(String jobHandlerName, IJobHandler handler) {
        Worker worker = new Worker(jobHandlerName, getJobProxy(handler), AopUtils.getTargetClass(handler));
        workerMap.put(jobHandlerName, worker);
        logger.info(">>>>>>>>>>> xxl-job regist handler success, jobName:{}, handler:{}", new Object[]{jobHandlerName, handler});
        return worker;
    }

    protected IJobHandler getJobProxy(IJobHandler handler) {
        return new JobHandlerProxy(handler);
    }

    /**
     * get worker
     *
     * @param jobHanderName
     * @return
     */
    public Worker getWorker(String jobHanderName) {
        return workerMap.get(jobHanderName);
    }

    /**
     * 获取所有handlerName
     *
     * @return
     */
    public Set<String> getHandlerNames() {
        return workerMap.keySet();
    }

    // ---------------------------------- init job handler ------------------------------------
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        initRepository();
    }

    /**
     * init job handler service
     */
    public void initRepository() {
        if (logger.isDebugEnabled()) {
            logger.debug(">>>>>>>>>>> xxl-job initJobHandler start");
        }
        Map<String, Object> serviceBeanMap = this.applicationContext.getBeansWithAnnotation(JobHander.class);
        if (CollectionUtils.isEmpty(serviceBeanMap)) {
            return;
        }
        for (Map.Entry<String, Object> entry : serviceBeanMap.entrySet()) {
            String jobHandlerName = getJobName(entry.getKey(), entry.getValue());
            if (entry.getValue() instanceof IJobHandler) {
                IJobHandler handler = (IJobHandler) entry.getValue();
                if (logger.isDebugEnabled()) {
                    logger.debug("job handler regist [{}]<-[{}]", new Object[]{jobHandlerName, entry.getValue().getClass().getSimpleName()});
                }
                regist(jobHandlerName, handler);
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug(">>>>>>>>>>> xxl-job initJobHandler Completed");
        }
    }

    protected String getJobName(String beanName, Object bean) {
        String jobName = AopUtils.getTargetClass(bean).getAnnotation(JobHander.class).value();
        if (StringUtils.hasText(jobName)) {
            return jobName;
        }
        return beanName;
    }
}
