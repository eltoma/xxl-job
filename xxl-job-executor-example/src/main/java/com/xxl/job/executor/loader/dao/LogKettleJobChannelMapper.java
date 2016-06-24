package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleJobChannel;
import org.springframework.stereotype.Repository;

@Repository
public interface LogKettleJobChannelMapper {
    int insert(LogKettleJobChannel record);

    int insertSelective(LogKettleJobChannel record);
}