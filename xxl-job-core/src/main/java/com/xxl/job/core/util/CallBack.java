package com.xxl.job.core.util;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class CallBack {
    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";

    private String status;
    private Integer code;
    private String msg;

    public CallBack() {
    }

    public CallBack(String status) {
        this.status = status;
    }

    public CallBack(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return status == SUCCESS;
    }

    @JsonIgnore
    public boolean isFail() {
        return status == FAIL;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setSuccess() {
        this.status = SUCCESS;
    }

    public void setFail() {
        this.status = FAIL;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public static CallBack success() {
        return new CallBack(SUCCESS);
    }

    public static CallBack success(String msg) {
        return new CallBack(SUCCESS, msg);
    }

    public static CallBack fail() {
        return new CallBack(FAIL);
    }

    public static CallBack fail(String msg) {
        return new CallBack(FAIL, msg);
    }

    @Override
    public String toString() {
        return "CallBack{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}