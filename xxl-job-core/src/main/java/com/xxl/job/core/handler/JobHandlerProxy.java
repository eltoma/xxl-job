package com.xxl.job.core.handler;

import com.xxl.job.core.util.CallBack;

/**
 * Created by feiluo on 6/22/2016.
 */
public class JobHandlerProxy extends IJobHandler {

    private IJobHandler jobHandler;

    public JobHandlerProxy(IJobHandler jobHandler) {
        this.jobHandler = jobHandler;
    }

    @Override
    public CallBack execute(String... params) throws Exception {
        String[] preExecuteParams = jobHandler.perExecute(params);
        CallBack callBack = jobHandler.execute(preExecuteParams);
        callBack = jobHandler.postExecute(callBack, preExecuteParams);
        return callBack;
    }

    public void init() {
        //TODO: 6/22/2016 jobHandler参数解析器
        // 检查缓存
        // 获取所有需要调用方法的参数 ： execute 或者 为注解的方法

        // 获取方法的参数列表
        // 根据参数列表，获取参数解析器
        // 名称获取器分类：默认名称获取器=用fieldName,用注解(普通参数注解，环境变量注解)
        // 参数解析器：根据目标类型，解析获取到的参数
        // 参数解析器
        // 获取可以注入的参数
        // 开始调用

    }
}
