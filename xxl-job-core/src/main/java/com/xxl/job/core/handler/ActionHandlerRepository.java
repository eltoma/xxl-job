package com.xxl.job.core.handler;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Multimap;
import com.xxl.job.core.constant.ActionEnum;
import com.xxl.job.core.constant.HandlerParamEnum;
import com.xxl.job.core.handler.action.IActionHandler;
import com.xxl.job.core.handler.annotation.ActionHandler;
import com.xxl.job.core.util.CallBack;
import com.xxl.job.core.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * handler repository
 *
 * @author xuxueli 2015-12-19 19:28:44
 */
@Service
public class ActionHandlerRepository implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ActionHandlerRepository.class);


    private static Multimap<ActionEnum, Class<? extends IActionHandler>> actionHandlerMap;

    // handler push to queue
    public static CallBack service(Map<String, String> _param) {
        if (logger.isDebugEnabled()) {
            logger.debug(">>>>>>>>>>> xxl-job service start, _param:{}", new Object[]{_param});
        }
        // check namespace
        String namespace = _param.get(HandlerParamEnum.ACTION.name());
        if (namespace == null || namespace.trim().length() == 0) {
            logger.error("param[NAMESPACE] can not be null.");
            return CallBack.fail("param[NAMESPACE] can not be null.");
        }
        // encryption check
        long timestamp = _param.get(HandlerParamEnum.TIMESTAMP.name()) != null ? Long.valueOf(_param.get(HandlerParamEnum.TIMESTAMP.name())) : -1;
        if (System.currentTimeMillis() - timestamp > 60000) {
            logger.error("Timestamp check failed.");
            return CallBack.fail("Timestamp check failed.");
        }
        if (actionHandlerMap == null) {
            logger.warn("action handler not init!");
            return CallBack.fail("action handler not init!");
        }

        ActionEnum actionEnum = ActionEnum.valueOf(namespace);
        Collection<Class<? extends IActionHandler>> actionHandlerClasses = actionHandlerMap.get(actionEnum);
        if (actionHandlerClasses.isEmpty()) {
            Collection<Class<? extends IActionHandler>> actionClazz = actionHandlerMap.get(ActionEnum.DEFAULT);
            callAction(actionClazz, _param, false);
            logger.error("param[Action] is not valid.");
            return CallBack.fail("param[Action] is not valid.");
        }
        if (logger.isDebugEnabled()) {
            logger.debug(">>>>>>>>>>> xxl-job service end, triggerData:{}");
        }
        return callAction(actionHandlerClasses, _param, true);
    }


    private static CallBack callAction(Collection<Class<? extends IActionHandler>> actionHandlerClasses, Map<String, String> _param, boolean isFailReturn) {
        CallBack callback = null;
        for (Class<? extends IActionHandler> actionHandlerClazz : actionHandlerClasses) {
            IActionHandler actionHandler = applicationContext.getBean(actionHandlerClazz);
            if (actionHandler == null) {
                continue;
            }
            callback = actionHandler.action(_param);
            if (isFailReturn && callback.isFail()) {
                break;
            }
        }
        return callback;
    }

    // ---------------------------------- init action handler ------------------------------------
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        initRepository();
    }

    /**
     * init action handler service
     */
    public void initRepository() {
        if (logger.isDebugEnabled()) {
            logger.debug(">>>>>>>>>>> xxl-job initActionHandler start");
        }
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(ActionHandler.class);
        if (serviceBeanMap != null && serviceBeanMap.isEmpty()) {
            return;
        }
        ImmutableListMultimap.Builder<ActionEnum, Class<? extends IActionHandler>> builder = ImmutableListMultimap.builder();
        for (Map.Entry<String, Object> entry : serviceBeanMap.entrySet()) {
            if (!(entry.getValue() instanceof IActionHandler)) {
                continue;
            }
            Class<?> targetClass = AopUtils.getTargetClass(entry.getValue());
            ActionHandler actionHandler = targetClass.getAnnotation(ActionHandler.class);
            if (actionHandler == null) {
                continue;
            }
            for (ActionEnum actionEnum : actionHandler.value()) {
                if (logger.isDebugEnabled()) {
                    logger.debug("action handler regist [{}]<-[{}]", new Object[]{actionEnum.name(), entry.getKey()});
                }
                builder.put(actionEnum, (Class<? extends IActionHandler>) targetClass);
            }
        }
        actionHandlerMap = builder.build();
        if (logger.isDebugEnabled()) {
            logger.debug(">>>>>>>>>>> xxl-job initActionHandler Completed");
        }
    }

}
