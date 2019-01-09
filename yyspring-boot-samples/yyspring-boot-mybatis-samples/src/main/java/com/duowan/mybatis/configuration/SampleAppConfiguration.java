package com.duowan.mybatis.configuration;

import com.duowan.mybatis.dao.mybatis.plugins.SampleInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/9 16:46
 */
@Configuration
public class SampleAppConfiguration {

    @Bean
    public SampleInterceptor sampleInterceptor1() {
        return new SampleInterceptor("1");
    }

    @Bean
    public SampleInterceptor sampleInterceptor2() {
        return new SampleInterceptor("2");
    }
}
