package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleJob;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKettleJobMapper {
    List<LogKettleJob> selectByID_LOG(Long ID_LOG);


    /**
     * 通过ID_JOB更新ID_LOG
     *
     * @param ID_LOG
     * @param ID_JOB
     */
    void updateID_LOGByID_BATCH(Long ID_LOG, Long ID_JOB);
}