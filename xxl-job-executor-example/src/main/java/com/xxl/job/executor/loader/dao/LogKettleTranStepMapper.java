package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranStep;

public interface LogKettleTranStepMapper {
    int insert(LogKettleTranStep record);

    int insertSelective(LogKettleTranStep record);
}