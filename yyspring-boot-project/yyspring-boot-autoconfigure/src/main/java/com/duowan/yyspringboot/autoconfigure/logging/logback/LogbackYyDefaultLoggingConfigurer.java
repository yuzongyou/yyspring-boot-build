package com.duowan.yyspringboot.autoconfigure.logging.logback;

import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspringboot.autoconfigure.logging.AbstractYyDefaultLoggingConfigurer;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.ClassUtils;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 14:46
 */
public class LogbackYyDefaultLoggingConfigurer extends AbstractYyDefaultLoggingConfigurer {

    @Override
    protected String getDefaultConfigFile(StandardEnvironment environment, String[] args) {
        ClassLoader classLoader = this.getClass().getClassLoader();
        boolean hasAlarmImported = ClassUtils.isPresent("com.duowan.common.alarm.Alarm", classLoader);
        // 根据环境设置默认日志文件
        String loggingFilePrefix = "classpath:com/duowan/yyspringboot/autoconfigure/logging/logback/logback-default-" + AppContext.getEnv();
        if (hasAlarmImported && AppContext.isProd()) {
            return loggingFilePrefix + "-alarm.xml";
        } else {
            return loggingFilePrefix + ".xml";
        }
    }
}
