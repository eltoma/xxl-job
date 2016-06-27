package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranChannel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKettleTranChannelMapper {

    List<LogKettleTranChannel> selectByID_LOG(Long ID_LOG);

    /**
     * 通过job的ID_BATCH查找对的转换的ID_BATCH
     *
     * @param jobID_BATCH
     * @return
     */
    List<Integer> selectBatchIdByJobID_BATCH(@Param("jobID_BATCH")Long jobID_BATCH);

    /**
     * 通过ID_BATCH更新ID_LOG
     *
     * @param ID_LOG
     * @param ID_BATCH
     */
    void updateID_LOGByID_BATCH(@Param("ID_LOG")Long ID_LOG, @Param("ID_BATCH")Long ID_BATCH);

}