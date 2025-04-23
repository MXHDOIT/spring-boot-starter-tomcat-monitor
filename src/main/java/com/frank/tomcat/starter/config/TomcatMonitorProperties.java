package com.frank.tomcat.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: maxinhang.
 **/

@ConfigurationProperties(prefix = "tomcat.monitor")
public class TomcatMonitorProperties {
    private long interval = 1000; // 默认每秒记录一次

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}