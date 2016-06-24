package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranChannel;
import org.springframework.stereotype.Repository;

@Repository
public interface LogKettleTranChannelMapper {
    int insert(LogKettleTranChannel record);

    int insertSelective(LogKettleTranChannel record);
}