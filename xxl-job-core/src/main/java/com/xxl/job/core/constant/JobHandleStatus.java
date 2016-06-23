package com.xxl.job.core.constant;

import com.xxl.job.core.util.CallBack;

public enum JobHandleStatus {
    /**
     * handle success
     */
    SUCCESS,
    /**
     * handle fail
     */
    FAIL,
    /**
     * handle not found
     */
    NOT_FOUND;

    public static JobHandleStatus valueOf(CallBack callBack) {
        return valueOf(callBack.getStatus());
    }
}