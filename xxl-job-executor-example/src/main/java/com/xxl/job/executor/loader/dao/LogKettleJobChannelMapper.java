package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleJobChannel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKettleJobChannelMapper {

    List<LogKettleJobChannel> selectByID_LOG(Long ID_LOG);
}