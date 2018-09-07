package com.duowan.yyspring.boot;

import org.springframework.core.env.StandardEnvironment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/1 19:15
 */
public interface ProjectNoReader {

    /**
     * 读取当前项目代号
     */
    String readProjectNo(StandardEnvironment environment, Class<?> sourceClass);
}
