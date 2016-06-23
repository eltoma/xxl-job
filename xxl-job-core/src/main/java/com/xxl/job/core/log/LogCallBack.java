package com.xxl.job.core.log;

import com.xxl.job.core.util.CallBack;
import com.xxl.job.core.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Created by feiluo on 6/23/2016.
 */
@Service
public class LogCallBack {

    private static final Logger logger = LoggerFactory.getLogger(LogCallBack.class);
    private static final LinkedBlockingQueue<HashMap<String, String>> callBackQueue = new LinkedBlockingQueue<>();

    @PostConstruct
    public void init() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        HashMap<String, String> item = callBackQueue.poll();
                        if (item != null) {
                            CallBack callback = null;
                            try {
                                callback = HttpUtil.post(item.get("_address"), item);
                            } catch (Exception e) {
                                logger.info("Worker Exception:", e);
                            }
                            logger.info(">>>>>>>>>>> xxl-job callback , params:{}, result:{}", new Object[]{item, callback});
                        }
                    } catch (Exception e) {
                        logger.error("xxl-job callback start fail.", e);
                        throw e;
                    }
                }
            }
        }).start();
    }

    public static void pushLog(String address, HashMap<String, String> params) {
        params.put("_address", address);
        callBackQueue.add(params);
    }
}
