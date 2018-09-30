package com.duowan.yyspringboot.autoconfigure.logging;

import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspring.boot.SpringApplicationRunListenerAdapter;
import com.duowan.yyspringboot.autoconfigure.logging.log4j2.Log4jYyDefaultLoggingConfigurer;
import com.duowan.yyspringboot.autoconfigure.logging.logback.LogbackYyDefaultLoggingConfigurer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.ClassUtils;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 14:32
 */
public class YyLoggingSpringApplicationRunListener extends SpringApplicationRunListenerAdapter {

    public YyLoggingSpringApplicationRunListener(SpringApplication application, String[] args) {
        super(application, args);
    }

    @Override
    protected boolean needAutoConfigurer() {
        return true;
    }

    @Override
    protected void doStarting() {

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
        configurer.configure(environment, this.getArgs());
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
}
