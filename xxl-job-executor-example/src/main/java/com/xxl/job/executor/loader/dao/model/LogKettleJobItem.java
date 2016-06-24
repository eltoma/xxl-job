package com.xxl.job.executor.loader.dao.model;

import java.util.Date;

public class LogKettleJobItem {
    private Integer ID_LOG;

    private Integer ID_BATCH;

    private String CHANNEL_ID;

    private Date LOG_DATE;

    private String TRANSNAME;

    private String STEPNAME;

    private Long LINES_READ;

    private Long LINES_WRITTEN;

    private Long LINES_UPDATED;

    private Long LINES_INPUT;

    private Long LINES_OUTPUT;

    private Long LINES_REJECTED;

    private Long ERRORS;

    private Boolean RESULT;

    private Long NR_RESULT_ROWS;

    private Long NR_RESULT_FILES;

    private Integer COPY_NR;

    private String LOG_FIELD;

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

    public String getTRANSNAME() {
        return TRANSNAME;
    }

    public void setTRANSNAME(String TRANSNAME) {
        this.TRANSNAME = TRANSNAME == null ? null : TRANSNAME.trim();
    }

    public String getSTEPNAME() {
        return STEPNAME;
    }

    public void setSTEPNAME(String STEPNAME) {
        this.STEPNAME = STEPNAME == null ? null : STEPNAME.trim();
    }

    public Long getLINES_READ() {
        return LINES_READ;
    }

    public void setLINES_READ(Long LINES_READ) {
        this.LINES_READ = LINES_READ;
    }

    public Long getLINES_WRITTEN() {
        return LINES_WRITTEN;
    }

    public void setLINES_WRITTEN(Long LINES_WRITTEN) {
        this.LINES_WRITTEN = LINES_WRITTEN;
    }

    public Long getLINES_UPDATED() {
        return LINES_UPDATED;
    }

    public void setLINES_UPDATED(Long LINES_UPDATED) {
        this.LINES_UPDATED = LINES_UPDATED;
    }

    public Long getLINES_INPUT() {
        return LINES_INPUT;
    }

    public void setLINES_INPUT(Long LINES_INPUT) {
        this.LINES_INPUT = LINES_INPUT;
    }

    public Long getLINES_OUTPUT() {
        return LINES_OUTPUT;
    }

    public void setLINES_OUTPUT(Long LINES_OUTPUT) {
        this.LINES_OUTPUT = LINES_OUTPUT;
    }

    public Long getLINES_REJECTED() {
        return LINES_REJECTED;
    }

    public void setLINES_REJECTED(Long LINES_REJECTED) {
        this.LINES_REJECTED = LINES_REJECTED;
    }

    public Long getERRORS() {
        return ERRORS;
    }

    public void setERRORS(Long ERRORS) {
        this.ERRORS = ERRORS;
    }

    public Boolean getRESULT() {
        return RESULT;
    }

    public void setRESULT(Boolean RESULT) {
        this.RESULT = RESULT;
    }

    public Long getNR_RESULT_ROWS() {
        return NR_RESULT_ROWS;
    }

    public void setNR_RESULT_ROWS(Long NR_RESULT_ROWS) {
        this.NR_RESULT_ROWS = NR_RESULT_ROWS;
    }

    public Long getNR_RESULT_FILES() {
        return NR_RESULT_FILES;
    }

    public void setNR_RESULT_FILES(Long NR_RESULT_FILES) {
        this.NR_RESULT_FILES = NR_RESULT_FILES;
    }

    public Integer getCOPY_NR() {
        return COPY_NR;
    }

    public void setCOPY_NR(Integer COPY_NR) {
        this.COPY_NR = COPY_NR;
    }

    public String getLOG_FIELD() {
        return LOG_FIELD;
    }

    public void setLOG_FIELD(String LOG_FIELD) {
        this.LOG_FIELD = LOG_FIELD == null ? null : LOG_FIELD.trim();
    }
}