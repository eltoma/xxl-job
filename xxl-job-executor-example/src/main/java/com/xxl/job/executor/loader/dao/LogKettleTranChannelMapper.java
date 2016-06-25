package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.LogKettleTranChannel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogKettleTranChannelMapper {

    List<LogKettleTranChannel> selectByID_LOG(String ID_LOG);

}