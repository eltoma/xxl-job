package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleJob;
import org.springframework.stereotype.Repository;

@Repository
public interface LogKettleJobMapper {
    int insert(LogKettleJob record);

    int insertSelective(LogKettleJob record);
}