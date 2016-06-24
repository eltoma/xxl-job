package com.xxl.job.executor.loader.dao.model;

import java.util.Date;

public class LogKettleTranRun {
    private Integer ID_LOG;

    private Integer ID_BATCH;

    private Integer SEQ_NR;

    private Date LOGDATE;

    private String TRANSNAME;

    private String STEPNAME;

    private Integer STEP_COPY;

    private Long LINES_READ;

    private Long LINES_WRITTEN;

    private Long LINES_UPDATED;

    private Long LINES_INPUT;

    private Long LINES_OUTPUT;

    private Long LINES_REJECTED;

    private Long ERRORS;

    private Long INPUT_BUFFER_ROWS;

    private Long OUTPUT_BUFFER_ROWS;

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

    public Integer getSEQ_NR() {
        return SEQ_NR;
    }

    public void setSEQ_NR(Integer SEQ_NR) {
        this.SEQ_NR = SEQ_NR;
    }

    public Date getLOGDATE() {
        return LOGDATE;
    }

    public void setLOGDATE(Date LOGDATE) {
        this.LOGDATE = LOGDATE;
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

    public Integer getSTEP_COPY() {
        return STEP_COPY;
    }

    public void setSTEP_COPY(Integer STEP_COPY) {
        this.STEP_COPY = STEP_COPY;
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

    public Long getINPUT_BUFFER_ROWS() {
        return INPUT_BUFFER_ROWS;
    }

    public void setINPUT_BUFFER_ROWS(Long INPUT_BUFFER_ROWS) {
        this.INPUT_BUFFER_ROWS = INPUT_BUFFER_ROWS;
    }

    public Long getOUTPUT_BUFFER_ROWS() {
        return OUTPUT_BUFFER_ROWS;
    }

    public void setOUTPUT_BUFFER_ROWS(Long OUTPUT_BUFFER_ROWS) {
        this.OUTPUT_BUFFER_ROWS = OUTPUT_BUFFER_ROWS;
    }
}