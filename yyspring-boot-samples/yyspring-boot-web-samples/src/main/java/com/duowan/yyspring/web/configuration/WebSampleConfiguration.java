package com.duowan.yyspring.web.configuration;

import com.duowan.yyspring.web.interceptor.I18nTransferInterceptor;
import com.duowan.yyspring.web.json.I18nMappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/14 11:20
 */
@Configuration
public class WebSampleConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new I18nTransferInterceptor()).addPathPatterns("/**");
    }


    @Bean
    @ConditionalOnMissingBean(value = MappingJackson2HttpMessageConverter.class, ignoredType = {
            "org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter",
            "org.springframework.data.rest.webmvc.alps.AlpsJsonHttpMessageConverter" })
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(
            ObjectMapper objectMapper) {
        return new I18nMappingJackson2HttpMessageConverter(objectMapper);
    }
}
