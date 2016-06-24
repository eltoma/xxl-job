package com.xxl.job.core.util;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class CallBack<T> {
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_FAIL = "FAIL";

    private String status;
    private Integer code;
    private String msg;
    private T data;

    public CallBack() {
    }

    public CallBack(String status) {
        this.status = status;
    }

    public CallBack(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public CallBack(String status, Integer code, String msg, T data) {
        this.status = status;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return STATUS_SUCCESS.equals(status);
    }

    @JsonIgnore
    public boolean isFail() {
        return STATUS_FAIL.equals(status);
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
        this.status = STATUS_SUCCESS;
    }

    public void setFail() {
        this.status = STATUS_FAIL;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static CallBack success() {
        return new CallBack(STATUS_SUCCESS);
    }

    public static CallBack success(String msg) {
        return new CallBack(STATUS_SUCCESS, msg);
    }

    public static <R> CallBack<R> success(String msg, R data) {
        return new CallBack(STATUS_SUCCESS, null, msg, data);
    }

    public static <R> CallBack<R> successWithData(R data) {
        return new CallBack(STATUS_SUCCESS, null, null, data);
    }


    public static CallBack fail() {
        return new CallBack(STATUS_FAIL);
    }

    public static CallBack fail(String msg) {
        return new CallBack(STATUS_FAIL, msg);
    }


    public static <R> CallBack<R> fail(String msg, R data) {
        return new CallBack(STATUS_FAIL, null, msg, data);
    }

    public static <R> CallBack<R> failWithData(R data) {
        return new CallBack(STATUS_FAIL, null, null, data);
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