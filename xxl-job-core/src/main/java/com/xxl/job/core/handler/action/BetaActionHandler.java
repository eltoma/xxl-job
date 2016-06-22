package com.xxl.job.core.handler.action;

import com.xxl.job.core.constant.ActionEnum;
import com.xxl.job.core.handler.annotation.ActionHandler;
import com.xxl.job.core.util.CallBack;

import java.util.Map;

/**
 * Created by feiluo on 6/20/2016.
 */
@ActionHandler(ActionEnum.BETA)
public class BetaActionHandler implements IActionHandler {


    @Override
    public CallBack action(Map<String, String> params) {
        return CallBack.success();
    }
}
