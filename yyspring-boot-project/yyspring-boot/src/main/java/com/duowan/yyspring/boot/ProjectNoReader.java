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
     *
     * @param environment 环境
     * @param sourceClass 启动class
     * @return 返回项目代号，如果获取不到，在开发环境下则计算当前运行目录
     */
    String readProjectNo(StandardEnvironment environment, Class<?> sourceClass);
}
