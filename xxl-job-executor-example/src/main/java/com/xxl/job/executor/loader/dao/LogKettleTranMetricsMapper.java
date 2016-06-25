package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranMetrics;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKettleTranMetricsMapper {
    List<LogKettleTranMetrics> selectByID_LOG(Long ID_LOG);
}