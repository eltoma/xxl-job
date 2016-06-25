package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranMetrics;
import com.xxl.job.executor.loader.dao.model.LogKettleTranRun;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKettleTranRunMapper {
    List<LogKettleTranRun> selectByID_LOG(Long ID_LOG);
}