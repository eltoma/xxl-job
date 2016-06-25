package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleJobChannel;
import com.xxl.job.executor.loader.dao.model.LogKettleJobItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKettleJobItemMapper {
    List<LogKettleJobItem> selectByID_LOG(String ID_LOG);
}