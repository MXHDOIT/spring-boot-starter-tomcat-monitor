package com.frank.tomcat.starter.config;

import com.frank.tomcat.starter.decorator.TomcatExecutorMonitoringDecorator;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.server.WebServer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;

/**
 * @author: maxinhang.
 */
@Configuration
public class TomcatConfig implements ApplicationListener<WebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        WebServer webServer = event.getWebServer();
        if (webServer instanceof TomcatWebServer) {
            TomcatWebServer tomcatWebServer = (TomcatWebServer) webServer;
            Connector[] connectors = tomcatWebServer.getTomcat().getService().findConnectors();
            for (Connector connector : connectors) {
                // 获取原始 Executor 并包装
                Executor originalExecutor = connector.getProtocolHandler().getExecutor();
                TomcatExecutorMonitoringDecorator tomcatExecutor = new TomcatExecutorMonitoringDecorator(originalExecutor);

                // 重新设置装饰后的 Executor
                connector.getProtocolHandler().setExecutor(tomcatExecutor);
            }
        }
    }
}

