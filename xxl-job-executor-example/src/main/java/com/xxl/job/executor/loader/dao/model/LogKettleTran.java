package com.xxl.job.executor.loader.dao.model;

import java.util.Date;

public class LogKettleTran {
    private Integer ID_LOG;

    private Integer ID_BATCH;

    private String CHANNEL_ID;

    private String TRANSNAME;

    private String STATUS;

    private Long LINES_READ;

    private Long LINES_WRITTEN;

    private Long LINES_UPDATED;

    private Long LINES_INPUT;

    private Long LINES_OUTPUT;

    private Long LINES_REJECTED;

    private Long ERRORS;

    private Date STARTDATE;

    private Date ENDDATE;

    private Date LOGDATE;

    private Date DEPDATE;

    private Date REPLAYDATE;

    private String EXECUTING_SERVER;

    private String EXECUTING_USER;

    private String CLIENT;

    private Integer PARANT_BATCH_ID;

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

    public String getTRANSNAME() {
        return TRANSNAME;
    }

    public void setTRANSNAME(String TRANSNAME) {
        this.TRANSNAME = TRANSNAME == null ? null : TRANSNAME.trim();
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS == null ? null : STATUS.trim();
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

    public Date getSTARTDATE() {
        return STARTDATE;
    }

    public void setSTARTDATE(Date STARTDATE) {
        this.STARTDATE = STARTDATE;
    }

    public Date getENDDATE() {
        return ENDDATE;
    }

    public void setENDDATE(Date ENDDATE) {
        this.ENDDATE = ENDDATE;
    }

    public Date getLOGDATE() {
        return LOGDATE;
    }

    public void setLOGDATE(Date LOGDATE) {
        this.LOGDATE = LOGDATE;
    }

    public Date getDEPDATE() {
        return DEPDATE;
    }

    public void setDEPDATE(Date DEPDATE) {
        this.DEPDATE = DEPDATE;
    }

    public Date getREPLAYDATE() {
        return REPLAYDATE;
    }

    public void setREPLAYDATE(Date REPLAYDATE) {
        this.REPLAYDATE = REPLAYDATE;
    }

    public String getEXECUTING_SERVER() {
        return EXECUTING_SERVER;
    }

    public void setEXECUTING_SERVER(String EXECUTING_SERVER) {
        this.EXECUTING_SERVER = EXECUTING_SERVER == null ? null : EXECUTING_SERVER.trim();
    }

    public String getEXECUTING_USER() {
        return EXECUTING_USER;
    }

    public void setEXECUTING_USER(String EXECUTING_USER) {
        this.EXECUTING_USER = EXECUTING_USER == null ? null : EXECUTING_USER.trim();
    }

    public String getCLIENT() {
        return CLIENT;
    }

    public void setCLIENT(String CLIENT) {
        this.CLIENT = CLIENT == null ? null : CLIENT.trim();
    }

    public Integer getPARANT_BATCH_ID() {
        return PARANT_BATCH_ID;
    }

    public void setPARANT_BATCH_ID(Integer PARANT_BATCH_ID) {
        this.PARANT_BATCH_ID = PARANT_BATCH_ID;
    }

    public String getLOG_FIELD() {
        return LOG_FIELD;
    }

    public void setLOG_FIELD(String LOG_FIELD) {
        this.LOG_FIELD = LOG_FIELD == null ? null : LOG_FIELD.trim();
    }
}