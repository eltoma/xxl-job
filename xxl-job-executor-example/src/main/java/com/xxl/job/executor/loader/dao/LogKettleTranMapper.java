package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTran;
import org.springframework.stereotype.Repository;

@Repository
public interface LogKettleTranMapper {
    int insert(LogKettleTran record);

    int insertSelective(LogKettleTran record);
}