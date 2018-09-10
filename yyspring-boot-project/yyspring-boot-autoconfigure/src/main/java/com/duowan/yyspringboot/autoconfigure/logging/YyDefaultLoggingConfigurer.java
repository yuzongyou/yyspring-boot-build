package com.duowan.yyspringboot.autoconfigure.logging;

import org.springframework.core.env.StandardEnvironment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 14:43
 */
public interface YyDefaultLoggingConfigurer {

    /**
     * 配置默认日志
     *
     * @param environment 当前运行环境
     * @param args        命令行参数
     */
    void configure(StandardEnvironment environment, String[] args);
}
