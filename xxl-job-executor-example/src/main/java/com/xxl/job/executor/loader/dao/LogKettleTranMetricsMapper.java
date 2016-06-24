package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranMetrics;

public interface LogKettleTranMetricsMapper {
    int insert(LogKettleTranMetrics record);

    int insertSelective(LogKettleTranMetrics record);
}