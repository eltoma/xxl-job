package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTran;
import com.xxl.job.executor.loader.dao.model.LogKettleTranChannel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKettleTranMapper {
    List<LogKettleTran> selectByID_LOG(String ID_LOG);
}