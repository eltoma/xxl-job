package com.xxl.job.executor.loader.dao.model;

import java.util.Date;

public class LogKettleTranMetrics {
    private Integer ID_LOG;

    private Integer ID_BATCH;

    private String CHANNEL_ID;

    private Date LOG_DATE;

    private Date METRICS_DATE;

    private String METRICS_CODE;

    private String METRICS_DESCRIPTION;

    private String METRICS_SUBJECT;

    private String METRICS_TYPE;

    private Long METRICS_VALUE;

    public Integer getID_LOG() {
        return ID_LOG;
    }

    public void setID_LOG(Integer ID_LOG) {
        this.ID_LOG = ID_LOG;
    }

    public Integer getID_BATCH() {
        return ID_BATCH;
    }

    public void setID_BATCH(Integer ID_BATCH) {
        this.ID_BATCH = ID_BATCH;
    }

    public String getCHANNEL_ID() {
        return CHANNEL_ID;
    }

    public void setCHANNEL_ID(String CHANNEL_ID) {
        this.CHANNEL_ID = CHANNEL_ID == null ? null : CHANNEL_ID.trim();
    }

    public Date getLOG_DATE() {
        return LOG_DATE;
    }

    public void setLOG_DATE(Date LOG_DATE) {
        this.LOG_DATE = LOG_DATE;
    }

    public Date getMETRICS_DATE() {
        return METRICS_DATE;
    }

    public void setMETRICS_DATE(Date METRICS_DATE) {
        this.METRICS_DATE = METRICS_DATE;
    }

    public String getMETRICS_CODE() {
        return METRICS_CODE;
    }

    public void setMETRICS_CODE(String METRICS_CODE) {
        this.METRICS_CODE = METRICS_CODE == null ? null : METRICS_CODE.trim();
    }

    public String getMETRICS_DESCRIPTION() {
        return METRICS_DESCRIPTION;
    }

    public void setMETRICS_DESCRIPTION(String METRICS_DESCRIPTION) {
        this.METRICS_DESCRIPTION = METRICS_DESCRIPTION == null ? null : METRICS_DESCRIPTION.trim();
    }

    public String getMETRICS_SUBJECT() {
        return METRICS_SUBJECT;
    }

    public void setMETRICS_SUBJECT(String METRICS_SUBJECT) {
        this.METRICS_SUBJECT = METRICS_SUBJECT == null ? null : METRICS_SUBJECT.trim();
    }

    public String getMETRICS_TYPE() {
        return METRICS_TYPE;
    }

    public void setMETRICS_TYPE(String METRICS_TYPE) {
        this.METRICS_TYPE = METRICS_TYPE == null ? null : METRICS_TYPE.trim();
    }

    public Long getMETRICS_VALUE() {
        return METRICS_VALUE;
    }

    public void setMETRICS_VALUE(Long METRICS_VALUE) {
        this.METRICS_VALUE = METRICS_VALUE;
    }
}