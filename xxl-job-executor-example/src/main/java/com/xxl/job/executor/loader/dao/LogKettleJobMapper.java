package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleJob;
import com.xxl.job.executor.loader.dao.model.LogKettleJobChannel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKettleJobMapper {
    List<LogKettleJob> selectByID_LOG(Long ID_LOG);
}