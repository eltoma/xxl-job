package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTran;

public interface LogKettleTranMapper {
    int insert(LogKettleTran record);

    int insertSelective(LogKettleTran record);
}