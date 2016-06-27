package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranRun;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKettleTranRunMapper {
    List<LogKettleTranRun> selectByID_LOG(Long ID_LOG);

    /**
     * 通过ID_BATCH更新ID_LOG
     *
     * @param ID_LOG
     * @param ID_BATCH
     */
    void updateID_LOGByID_BATCH(@Param("ID_LOG")Long ID_LOG, @Param("ID_BATCH")Long ID_BATCH);
}