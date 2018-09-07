package com.duowan.yyspring.boot;

import org.springframework.core.env.StandardEnvironment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/1 19:07
 */
public interface EnvReader {

    /**
     * 读取当前运行环境
     */
    String readRuntimeEnv(StandardEnvironment environment, Class<?> sourceClass);
}
