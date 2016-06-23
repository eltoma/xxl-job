package com.xxl.job.admin.core.thread;

import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.core.util.DynamicSchedulerUtil;
import com.xxl.job.admin.core.util.MailUtil;
import com.xxl.job.core.util.CallBack;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.concurrent.*;

/**
 * job monitor helper
 *
 * @author xuxueli 2015-9-1 18:05:56
 */
public class JobMonitorHelper {
    private static Logger logger = LoggerFactory.getLogger(JobMonitorHelper.class);

    private static final JobMonitorHelper helper = new JobMonitorHelper();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(0xfff8);
    private final ConcurrentHashMap<String, Integer> countMap = new ConcurrentHashMap<>();

    public JobMonitorHelper() {
        logger.info("job monitor start ... ");
        // consumer
        executor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    logger.debug(">>>>>>>>>>> job monitor run ... ");
                    Integer jobLogId = JobMonitorHelper.helper.queue.poll();
                    if (jobLogId != null && jobLogId > 0) {
                        XxlJobLog log = DynamicSchedulerUtil.xxlJobLogDao.load(jobLogId);
                        if (log != null) {
                            if (CallBack.SUCCESS.equals(log.getTriggerStatus()) && StringUtils.isBlank(log.getHandleStatus())) {
                                try {
                                    TimeUnit.SECONDS.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                JobMonitorHelper.monitor(jobLogId);
                            }
                            if (CallBack.SUCCESS.equals(log.getTriggerStatus()) && CallBack.SUCCESS.equals(log.getHandleStatus())) {
                                // pass
                            }
                            if (CallBack.FAIL.equals(log.getTriggerStatus()) || CallBack.FAIL.equals(log.getHandleStatus())) {
                                String monotorKey = log.getJobGroup().concat("_").concat(log.getJobName());
                                Integer count = countMap.get(monotorKey);
                                if (count == null) {
                                    count = new Integer(0);
                                }
                                count += 1;
                                countMap.put(monotorKey, count);
                                XxlJobInfo info = DynamicSchedulerUtil.xxlJobInfoDao.load(log.getJobGroup(), log.getJobName());
                                if (count >= info.getAlarmThreshold()) {
                                    MailUtil.sendMail(info.getAlarmEmail(), "《调度平台中心-监控报警》",
                                            MessageFormat.format("调度任务[{0}]失败报警，连续失败次数：{1}", monotorKey, count), false, null);
                                    countMap.remove(monotorKey);
                                }
                            }
                        }
                    } else {
                        try {
                            TimeUnit.SECONDS.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    // producer
    public static void monitor(int jobLogId) {
        JobMonitorHelper.helper.queue.offer(jobLogId);
    }

}
