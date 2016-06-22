package com.xxl.job.core.handler.action;

import com.xxl.job.core.util.CallBack;

import java.util.Map;

/**
 * Action Handler
 * <p>
 * deal request action
 * <p>
 * Created by feiluo on 6/20/2016.
 */
public interface IActionHandler {

    CallBack action(Map<String, String> params);

}
