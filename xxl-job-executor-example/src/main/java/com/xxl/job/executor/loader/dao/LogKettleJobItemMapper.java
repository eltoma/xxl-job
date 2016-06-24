package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleJobItem;
import org.springframework.stereotype.Repository;

@Repository
public interface LogKettleJobItemMapper {
    int insert(LogKettleJobItem record);

    int insertSelective(LogKettleJobItem record);
}