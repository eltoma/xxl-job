package com.xxl.job.executor.loader.dao;

import com.xxl.job.executor.loader.dao.model.XxlJobInfo;
import org.springframework.stereotype.Repository;

/**
 * job log for glue
 * @author xuxueli 2016-5-19 18:04:56
 */
@Repository
public interface XxlJobInfoMapper {
	
	XxlJobInfo load(String jobGroup, String jobName);

}
