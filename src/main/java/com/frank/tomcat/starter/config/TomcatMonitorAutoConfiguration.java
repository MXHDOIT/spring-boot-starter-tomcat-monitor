package com.frank.tomcat.starter.config;

import com.frank.tomcat.starter.scheduler.TomcatMetricsScheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: maxinhang.
 */
@Configuration
@EnableConfigurationProperties(TomcatMonitorProperties.class)
public class TomcatMonitorAutoConfiguration {

    @Bean
    public TomcatConfig tomcatConfig() {
        return new TomcatConfig();
    }

    @Bean
    @ConditionalOnMissingBean
    public TomcatMetricsScheduler tomcatMetricsScheduler(ServletWebServerApplicationContext applicationContext,
                                                         TomcatMonitorProperties tomcatMonitorProperties) {
        return new TomcatMetricsScheduler(tomcatMonitorProperties, applicationContext.getWebServer());
    }
}
