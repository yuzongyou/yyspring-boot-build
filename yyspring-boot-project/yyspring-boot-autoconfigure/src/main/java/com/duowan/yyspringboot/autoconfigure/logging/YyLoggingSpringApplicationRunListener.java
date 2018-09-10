package com.duowan.yyspringboot.autoconfigure.logging;

import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspringboot.autoconfigure.logging.log4j2.Log4jYyDefaultLoggingConfigurer;
import com.duowan.yyspringboot.autoconfigure.logging.logback.LogbackYyDefaultLoggingConfigurer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.ClassUtils;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 14:32
 */
public class YyLoggingSpringApplicationRunListener implements SpringApplicationRunListener, Ordered {

    private final SpringApplication application;

    private final String[] args;

    public YyLoggingSpringApplicationRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    @Override
    public void starting() {

        StandardEnvironment environment = AppContext.getEnvironment();
        String loggingFile = environment.getProperty(LoggingConstants.CONFIG_PROPERTY);
        if (StringUtils.isNotBlank(loggingFile)) {
            return;
        }

        ClassLoader classLoader = this.getClass().getClassLoader();
        YyDefaultLoggingConfigurer configurer = deduceRuntimeDefaultLoggingConfigurer(classLoader);
        if (null == configurer) {
            return;
        }

        // 配置默认日志
        configurer.configure(environment, this.args);
    }

    private YyDefaultLoggingConfigurer deduceRuntimeDefaultLoggingConfigurer(ClassLoader classLoader) {

        if (ClassUtils.isPresent("ch.qos.logback.core.Appender", classLoader)) {
            return new LogbackYyDefaultLoggingConfigurer();
        }

        if (ClassUtils.isPresent("org.apache.logging.log4j.core.impl.Log4jContextFactory", classLoader)) {
            return new Log4jYyDefaultLoggingConfigurer();
        }

        return null;
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }

    @Override
    public int getOrder() {
        return 2;
    }
}
