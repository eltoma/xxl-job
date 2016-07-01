package com.xxl.job.core.log;

import com.xxl.job.core.log.annotation.LogView;

/**
 * Created by feiluo on 6/29/2016.
 */
public interface ILogTemplate {

    Object getView(LogView logView, Object returnData);

}
