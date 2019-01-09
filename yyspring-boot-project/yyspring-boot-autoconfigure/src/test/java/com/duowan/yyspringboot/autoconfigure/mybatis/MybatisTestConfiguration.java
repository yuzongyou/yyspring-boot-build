package com.duowan.yyspringboot.autoconfigure.mybatis;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/8 16:25
 */
@Configuration
public class MybatisTestConfiguration {

    @Bean
    public Interceptor mybatisInterceptor() {

        return new Interceptor() {
            @Override
            public Object intercept(Invocation invocation) throws Throwable {
                return null;
            }

            @Override
            public Object plugin(Object target) {
                return null;
            }

            @Override
            public void setProperties(Properties properties) {

            }
        };
    }
}
