package com.frank.tomcat.starter.scheduler;

import com.frank.tomcat.starter.config.TomcatMonitorProperties;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServer;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author: maxinhang.
 */
public class TomcatMetricsScheduler {
    private static final Logger logger = LoggerFactory.getLogger(TomcatMetricsScheduler.class);

    private TomcatMonitorProperties properties;

    private WebServer webServer;

    public TomcatMetricsScheduler(TomcatMonitorProperties properties, WebServer webServer) {
        this.properties = properties;
        this.webServer = webServer;
    }

    @PostConstruct
    public void startMonitoring() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(() -> {
            if (webServer instanceof TomcatWebServer) {
                TomcatWebServer tomcatWebServer = (TomcatWebServer) webServer;
                Connector[] connectors = tomcatWebServer.getTomcat().getService().findConnectors();
                for (Connector connector : connectors) {
                    Executor executor = connector.getProtocolHandler().getExecutor();
                    if (executor instanceof ThreadPoolExecutor) {
                        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                        int maximumPoolSize = threadPoolExecutor.getMaximumPoolSize();
                        int activeSize = threadPoolExecutor.getActiveCount();
                        int queueSize = threadPoolExecutor.getQueue().size();
                        // 从进入队列到执行耗时较难直接获取，这里暂时不实现
                        logger.info("Tomcat Max Thread Size {} Active Thread Size: {}, Queue Size: {}", maximumPoolSize, activeSize, queueSize);
                    }
                }
            }
        }, 0, properties.getInterval(), TimeUnit.MILLISECONDS);
    }
}
