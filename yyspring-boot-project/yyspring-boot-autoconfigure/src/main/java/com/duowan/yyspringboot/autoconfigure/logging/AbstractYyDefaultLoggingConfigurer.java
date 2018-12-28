package com.duowan.yyspringboot.autoconfigure.logging;

import com.duowan.common.utils.ConvertUtil;
import com.duowan.common.utils.StringUtil;
import com.duowan.yyspring.boot.AppContext;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.ClassUtils;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 16:42
 */
public abstract class AbstractYyDefaultLoggingConfigurer implements YyDefaultLoggingConfigurer {

    @Override
    public void configure(StandardEnvironment environment, String[] args) {

        boolean needLogErrorAlarm = needLogErrorAppenderAlarm(environment, args);
        String defaultConfigFile = getDefaultConfigFile(needLogErrorAlarm, environment, args);

        System.err.println("使用默认的日志配置文件： " + defaultConfigFile);
        // 设置日志配置文件
        System.setProperty(LoggingConstants.CONFIG_PROPERTY, defaultConfigFile);

        // 公共属性设置
        commonPropertySet(environment, args);

        // 其他配置
        customConfigure(environment, args);
    }

    protected boolean needLogErrorAppenderAlarm(StandardEnvironment environment, String[] args) {

        boolean enabledLogErrorAppender = ConvertUtil.toBoolean(environment.getProperty("yyspring.alarm.log-error-enabled", "true"), true);

        ClassLoader classLoader = this.getClass().getClassLoader();
        boolean hasAlarmImported = ClassUtils.isPresent("com.duowan.common.alarm.Alarm", classLoader);

        return hasAlarmImported && enabledLogErrorAppender;

    }

    protected void commonPropertySet(StandardEnvironment environment, String[] args) {
        String logDir = getLogDir(environment, args);

        System.setProperty("LOG_FILE_DIR", logDir);
        // 设置一些变量
        String logFileNamePrefix = deduceLogFileNamePrefix(environment, args);
        System.setProperty("LOG_ALL_FILE_NAME", logFileNamePrefix + "all.log");
        System.setProperty("LOG_DEBUG_FILE_NAME", logFileNamePrefix + "debug.log");
        System.setProperty("LOG_INFO_FILE_NAME", logFileNamePrefix + "info.log");
        System.setProperty("LOG_WARN_FILE_NAME", logFileNamePrefix + "warn.log");
        System.setProperty("LOG_ERROR_FILE_NAME", logFileNamePrefix + "error.log");

        String basePrefix = logDir + logFileNamePrefix;
        System.setProperty("LOG_ALL_FILE_PATH", basePrefix + "all.log");
        System.setProperty("LOG_DEBUG_FILE_PATH", basePrefix + "debug.log");
        System.setProperty("LOG_INFO_FILE_PATH", basePrefix + "info.log");
        System.setProperty("LOG_WARN_FILE_PATH", basePrefix + "warn.log");
        System.setProperty("LOG_ERROR_FILE_PATH", basePrefix + "error.log");

    }

    protected String getLogDir(StandardEnvironment environment, String[] args) {
        String logDir = AppContext.getLogDir();

        if (StringUtil.isBlank(logDir)) {
            logDir = System.getProperty("user.dir");
        }

        return logDir.replaceAll("[\\\\/]*$", "/");
    }

    private String deduceLogFileNamePrefix(StandardEnvironment environment, String[] args) {
        String moduleNo = AppContext.getModuleNo();

        if (StringUtil.isNotBlank(moduleNo)) {
            return moduleNo + "-";
        }
        return "";
    }

    protected void customConfigure(StandardEnvironment environment, String[] args) {
    }

    /**
     * 获取默认的日志配置文件路径
     *
     * @param needLogErrorAlarm 日志是否需要进行告警
     * @param environment       环境
     * @param args              jvm启动参数
     * @return 返回默认日志配置文件
     */
    protected abstract String getDefaultConfigFile(boolean needLogErrorAlarm, StandardEnvironment environment, String[] args);
}
