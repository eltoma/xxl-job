package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranStep;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKettleTranStepMapper {
    List<LogKettleTranStep> selectByID_LOG(Long ID_LOG);
}