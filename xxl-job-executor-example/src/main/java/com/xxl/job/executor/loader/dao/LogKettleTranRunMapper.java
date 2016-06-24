package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranRun;
import org.springframework.stereotype.Repository;

@Repository
public interface LogKettleTranRunMapper {
    int insert(LogKettleTranRun record);

    int insertSelective(LogKettleTranRun record);
}