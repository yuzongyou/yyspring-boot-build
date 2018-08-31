package com.duowan.yyspring.boot;

import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Spring 程序配置
 *
 * @author Arvin
 */
public interface SpringApplicationBuilderConfigurer {

    /**
     * 配置应用程序
     *
     * @param application 应用程序
     */
    void configure(SpringApplicationBuilder application);
}
