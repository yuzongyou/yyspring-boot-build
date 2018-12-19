package com.duowan.yyspringboot.autoconfigure.logging.logback;

import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspringboot.autoconfigure.logging.AbstractYyDefaultLoggingConfigurer;
import org.springframework.core.env.StandardEnvironment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 14:46
 */
public class LogbackYyDefaultLoggingConfigurer extends AbstractYyDefaultLoggingConfigurer {

    @Override
    protected String getDefaultConfigFile(boolean needLogErrorAlarm, StandardEnvironment environment, String[] args) {
        // 根据环境设置默认日志文件
        String loggingFilePrefix = "classpath:com/duowan/yyspringboot/autoconfigure/logging/logback/logback-default-" + AppContext.getEnv();
        if (needLogErrorAlarm && AppContext.isProd()) {
            return loggingFilePrefix + "-alarm.xml";
        } else {
            return loggingFilePrefix + ".xml";
        }
    }
}
