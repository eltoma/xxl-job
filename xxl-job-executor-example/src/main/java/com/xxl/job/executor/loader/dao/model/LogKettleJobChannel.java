package com.xxl.job.executor.loader.dao.model;

import java.util.Date;

public class LogKettleJobChannel {
    private Integer ID_LOG;

    private Integer ID_BATCH;

    private String CHANNEL_ID;

    private Date LOG_DATE;

    private String LOGGING_OBJECT_TYPE;

    private String OBJECT_NAME;

    private String OBJECT_COPY;

    private String REPOSITORY_DIRECTORY;

    private String FILENAME;

    private String OBJECT_ID;

    private String OBJECT_REVISION;

    private String PARENT_CHANNEL_ID;

    private String ROOT_CHANNEL_ID;

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

    public String getLOGGING_OBJECT_TYPE() {
        return LOGGING_OBJECT_TYPE;
    }

    public void setLOGGING_OBJECT_TYPE(String LOGGING_OBJECT_TYPE) {
        this.LOGGING_OBJECT_TYPE = LOGGING_OBJECT_TYPE == null ? null : LOGGING_OBJECT_TYPE.trim();
    }

    public String getOBJECT_NAME() {
        return OBJECT_NAME;
    }

    public void setOBJECT_NAME(String OBJECT_NAME) {
        this.OBJECT_NAME = OBJECT_NAME == null ? null : OBJECT_NAME.trim();
    }

    public String getOBJECT_COPY() {
        return OBJECT_COPY;
    }

    public void setOBJECT_COPY(String OBJECT_COPY) {
        this.OBJECT_COPY = OBJECT_COPY == null ? null : OBJECT_COPY.trim();
    }

    public String getREPOSITORY_DIRECTORY() {
        return REPOSITORY_DIRECTORY;
    }

    public void setREPOSITORY_DIRECTORY(String REPOSITORY_DIRECTORY) {
        this.REPOSITORY_DIRECTORY = REPOSITORY_DIRECTORY == null ? null : REPOSITORY_DIRECTORY.trim();
    }

    public String getFILENAME() {
        return FILENAME;
    }

    public void setFILENAME(String FILENAME) {
        this.FILENAME = FILENAME == null ? null : FILENAME.trim();
    }

    public String getOBJECT_ID() {
        return OBJECT_ID;
    }

    public void setOBJECT_ID(String OBJECT_ID) {
        this.OBJECT_ID = OBJECT_ID == null ? null : OBJECT_ID.trim();
    }

    public String getOBJECT_REVISION() {
        return OBJECT_REVISION;
    }

    public void setOBJECT_REVISION(String OBJECT_REVISION) {
        this.OBJECT_REVISION = OBJECT_REVISION == null ? null : OBJECT_REVISION.trim();
    }

    public String getPARENT_CHANNEL_ID() {
        return PARENT_CHANNEL_ID;
    }

    public void setPARENT_CHANNEL_ID(String PARENT_CHANNEL_ID) {
        this.PARENT_CHANNEL_ID = PARENT_CHANNEL_ID == null ? null : PARENT_CHANNEL_ID.trim();
    }

    public String getROOT_CHANNEL_ID() {
        return ROOT_CHANNEL_ID;
    }

    public void setROOT_CHANNEL_ID(String ROOT_CHANNEL_ID) {
        this.ROOT_CHANNEL_ID = ROOT_CHANNEL_ID == null ? null : ROOT_CHANNEL_ID.trim();
    }
}