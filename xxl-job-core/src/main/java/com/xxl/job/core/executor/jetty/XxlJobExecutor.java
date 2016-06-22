package com.xxl.job.core.executor.jetty;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xuxueli on 2016/3/2 21:14.
 */
public class XxlJobExecutor {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobExecutor.class);

    private int port = 9999;

    public void setPort(int port) {
        this.port = port;
    }

    // ---------------------------------- job server ------------------------------------
    Server server = null;

    public void start() throws Exception {

        new Thread(new Runnable() {
            @Override
            public void run() {
                server = new Server(new ExecutorThreadPool(200, 200, 30000));  // 非阻塞
                // connector
                ServerConnector connector = new ServerConnector(server);
                connector.setPort(port);
                connector.setIdleTimeout(30000);
                server.addConnector(connector);

                // handler
                HandlerCollection handlerc = new HandlerCollection();
                handlerc.setHandlers(new Handler[]{new XxlJobExecutorHandler()});
                server.setHandler(handlerc);

                try {
                    server.start();
                    logger.info(">>>>>>>>>>>> xxl-job jetty server start success at port:{}.", port);
                    server.join();  // block until server ready
                    logger.info(">>>>>>>>>>>> xxl-job jetty server join success at port:{}.", port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void destroy() {
        if (server != null) {
            try {
                server.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
