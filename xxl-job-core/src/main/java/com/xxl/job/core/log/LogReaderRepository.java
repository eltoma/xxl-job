package com.xxl.job.core.log;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.xxl.job.core.log.reader.LogReader;
import com.xxl.job.core.log.reader.LogType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by feiluo on 6/24/2016.
 */
@Service
public class LogReaderRepository implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(LogReaderRepository.class);

    public static final String LOG_READER_DFAULT_CONSOLE_NAME = "console";
    private static Class<?> LOG_READER_DEFAULT_CLASS;
    private static Method LOG_READER_DEFAULT_METHOD;

    private static Table<String, String, Method> handlerTypeToMethod;
    private static Map<String, Method> handlerToMethod;
    private static Table<String, String, Class<?>> handlerTypeToClass;
    private static Map<String, Class<?>> handlerToClass;
    public ApplicationContext applicationContext;

    public Optional<Object> readLog(String jobHandlerName, String logType, String triggerLogId, Date triggerDate) throws Exception {
        if (handlerTypeToMethod == null || handlerToMethod == null || handlerTypeToClass == null || handlerToClass == null) {
            logger.error("LogReader Repository is not init");
            return Optional.absent();
        }
        Class<?> logReaderClass = getLogReaderClass(jobHandlerName, logType);
        Method method = getLogReaderMethod(jobHandlerName, logType);
        Object bean = applicationContext.getBean(logReaderClass);
        try {
            Object returnData = method.invoke(bean, triggerLogId, triggerDate);
            return Optional.fromNullable(returnData);
        } catch (Exception e) {
            logger.error("call Log reader fail.", e);
            throw e;
        }
    }

    public Collection<String> getLogType(String jobHandlerName) {
        return handlerTypeToMethod.row(jobHandlerName).keySet();
    }

    public Class<?> getLogReaderClass(String jobHandlerName, String logType) {
        Class<?> logReaderClass = handlerTypeToClass.get(jobHandlerName, logType);
        if (logReaderClass != null) {
            return logReaderClass;
        }
        logReaderClass = handlerToClass.get(jobHandlerName);
        if (logReaderClass != null) {
            return logReaderClass;
        }
        return LOG_READER_DEFAULT_CLASS;
    }

    public Method getLogReaderMethod(String jobHandlerName, String logType) {
        Method logReaderMethod = handlerTypeToMethod.get(jobHandlerName, logType);
        if (logReaderMethod != null) {
            return logReaderMethod;
        }
        logReaderMethod = handlerToMethod.get(jobHandlerName);
        if (logReaderMethod != null) {
            return logReaderMethod;
        }
        return LOG_READER_DEFAULT_METHOD;
    }

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
            logger.debug(">>>>>>>>>>> xxl-job initLogReader start");
        }
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(LogReader.class);
        if (serviceBeanMap != null && serviceBeanMap.isEmpty()) {
            return;
        }
        LogReaderRepositoryBuilderHelper builderHelper = new LogReaderRepositoryBuilderHelper();
        for (Map.Entry<String, Object> entry : serviceBeanMap.entrySet()) {
            LogReader logReader = AopUtils.getTargetClass(entry.getValue()).getAnnotation(LogReader.class);
            String forJobHandler = logReader.forJobHandler();
            if (LOG_READER_DFAULT_CONSOLE_NAME.equals(logReader.forJobHandler())) {
                //默认日志处理
                LOG_READER_DEFAULT_CLASS = AopUtils.getTargetClass(entry.getValue());
                LOG_READER_DEFAULT_METHOD = MethodUtils.getMethodsListWithAnnotation(LOG_READER_DEFAULT_CLASS, LogType.class).get(0);
                builderHelper.canLogReaderInvoke(LOG_READER_DEFAULT_CLASS, LOG_READER_DEFAULT_METHOD);
                continue;
            }
            Class<?> targetClass = AopUtils.getTargetClass(entry.getValue());
            // 获取有@LogType标识的方法
            List<Method> methodsListHasLogType = MethodUtils.getMethodsListWithAnnotation(targetClass, LogType.class);
            if (CollectionUtils.isEmpty(methodsListHasLogType)) {
                // 没有@LogType标识时，使用唯一方法处理
                Method[] methods = targetClass.getMethods();
                if (methods.length == 1) {
                    builderHelper.builder(forJobHandler, targetClass, methods[0]);
                }
            } else {
                // 添加所有包含@LogType的方法
                builderHelper.builder(forJobHandler, targetClass, methodsListHasLogType);
            }

        }
        handlerTypeToMethod = builderHelper.getHandlerTypeToMethod();
        handlerToMethod = builderHelper.getHandlerToMethod();
        handlerTypeToClass = builderHelper.getHandlerTypeToClass();
        handlerToClass = builderHelper.getHandlerToClass();
        if (logger.isDebugEnabled()) {
            logger.debug(">>>>>>>>>>> xxl-job initLogReader Completed");
        }
    }

    class LogReaderRepositoryBuilderHelper {
        ImmutableTable.Builder<String, String, Method> handlerTypeToMethodBuilder;
        ImmutableMap.Builder<String, Method> handlerToMethodBuilder;
        ImmutableTable.Builder<String, String, Class<?>> handlerTypeToClassBuilder;
        ImmutableMap.Builder<String, Class<?>> handlerToClassBuilder;

        public LogReaderRepositoryBuilderHelper() {
            handlerTypeToMethodBuilder = ImmutableTable.builder();
            handlerToMethodBuilder = ImmutableMap.builder();
            handlerTypeToClassBuilder = ImmutableTable.builder();
            handlerToClassBuilder = ImmutableMap.builder();
        }

        public void builder(String forJobHandler, Class<?> forClass, Method method) {
            LogType logTypeAnnotation = method.getAnnotation(LogType.class);
            if (logTypeAnnotation == null) {
                return;
            }
            canLogReaderInvoke(forClass, method);
            String[] logTypes = logTypeAnnotation.value();
            if (ArrayUtils.isEmpty(logTypes)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("log reader class regist [{}]<-[{}]", new Object[]{forJobHandler, forClass.getName()});
                    logger.debug("log reader method regist [{}]<-[{}]", new Object[]{forJobHandler, method.getName()});
                }
                handlerToClassBuilder.put(forJobHandler, forClass);
                handlerToMethodBuilder.put(forJobHandler, method);
            } else {
                for (String logType : logTypes) {
                    if (StringUtils.isBlank(logType)) {
                        continue;
                    }
                    if (logger.isDebugEnabled()) {
                        logger.debug("log reader class regist [{},{}]<-[{}]", new Object[]{forJobHandler, logType, forClass.getName()});
                        logger.debug("log reader method regist [{},{}]<-[{}]", new Object[]{forJobHandler, logType, method.getName()});
                    }
                    handlerTypeToClassBuilder.put(forJobHandler, logType, forClass);
                    handlerTypeToMethodBuilder.put(forJobHandler, logType, method);
                }
            }
        }

        public void builder(String forJobHandler, Class<?> forClass, Method[] methods) {
            for (Method method : methods) {
                builder(forJobHandler, forClass, method);
            }
        }

        public void builder(String forJobHandler, Class<?> forClass, Iterable<Method> methods) {
            for (Method method : methods) {
                builder(forJobHandler, forClass, method);
            }
        }

        public ImmutableTable<String, String, Method> getHandlerTypeToMethod() {
            return handlerTypeToMethodBuilder.build();
        }

        public ImmutableMap<String, Method> getHandlerToMethod() {
            return handlerToMethodBuilder.build();
        }

        public ImmutableTable<String, String, Class<?>> getHandlerTypeToClass() {
            return handlerTypeToClassBuilder.build();
        }

        public ImmutableMap<String, Class<?>> getHandlerToClass() {
            return handlerToClassBuilder.build();
        }

        /**
         * 检查需要注入的方法，参数是否满足条件
         *
         * @param method
         * @return
         */
        public void canLogReaderInvoke(Class<?> forClass, Method method) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            boolean canInvoke = parameterTypes.length == 2 && String.class.equals(parameterTypes[0]) && Date.class.equals(parameterTypes[1]);
            Assert.isTrue(canInvoke, "Paramter not  match method: " + method.getName() + "() on class: " + forClass.getName());
        }
    }


}

