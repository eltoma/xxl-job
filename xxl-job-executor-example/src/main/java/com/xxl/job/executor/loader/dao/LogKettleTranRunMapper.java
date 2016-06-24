package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranRun;

public interface LogKettleTranRunMapper {
    int insert(LogKettleTranRun record);

    int insertSelective(LogKettleTranRun record);
}