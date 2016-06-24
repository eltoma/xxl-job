package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranStep;
import org.springframework.stereotype.Repository;

@Repository
public interface LogKettleTranStepMapper {
    int insert(LogKettleTranStep record);

    int insertSelective(LogKettleTranStep record);
}