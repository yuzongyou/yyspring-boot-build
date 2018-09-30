package com.duowan.yyspringboot.autoconfigure.web;

import com.duowan.common.web.WebContext;
import com.duowan.common.web.YyServletContextInitializer;
import com.duowan.common.web.converter.json.ExtendMappingJackson2HttpMessageConverter;
import com.duowan.common.web.converter.json.JsonJavascriptAdvice;
import com.duowan.common.web.converter.json.JsonpAdvice;
import com.duowan.common.web.converter.json.StringHttpMessageAdvice;
import com.duowan.common.web.filter.YyRootFilter;
import com.duowan.common.web.pageparameter.ClientIpPageParameter;
import com.duowan.common.web.pageparameter.ProtocolPageParameter;
import com.duowan.common.web.pageparameter.ProtocolTypePageParameter;
import com.duowan.common.web.pageparameter.RequestUriPageParameter;
import com.duowan.common.web.view.AjaxView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 10:27
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, YyServletContextInitializer.class})
@EnableConfigurationProperties({WebMvcProperties.class})
@Import({JsonpAdvice.class, JsonJavascriptAdvice.class, StringHttpMessageAdvice.class})
public class YyWebMvcAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(YyWebMvcAutoConfiguration.class);

    @Autowired
    public YyWebMvcAutoConfiguration(WebMvcProperties webMvcProperties) {
        initializeWebContext(webMvcProperties);
    }

    private void initializeWebContext(WebMvcProperties webMvcProperties) {

        if (StringUtils.isNotBlank(webMvcProperties.getAjaxStatusCodeName())) {
            AjaxView.setAjaxStatusCodeName(webMvcProperties.getAjaxStatusCodeName());
        }

        if (!StringUtils.isAllBlank(webMvcProperties.getDateFormatVars())) {
            WebContext.setDateFormatVars(webMvcProperties.getDateFormatVars());
        }

        if (!StringUtils.isAllBlank(webMvcProperties.getJavascriptVars())) {
            WebContext.setJavascriptVars(webMvcProperties.getJavascriptVars());
        }

        if (!StringUtils.isAllBlank(webMvcProperties.getJsonpCallbackVars())) {
            WebContext.setJsonpCallbackVars(webMvcProperties.getJsonpCallbackVars());
        }

        if (!StringUtils.isAllBlank(webMvcProperties.getLookupClientIpHeaders())) {
            WebContext.setLookupClientIpHeaders(webMvcProperties.getLookupClientIpHeaders());
        }

        if (!StringUtils.isAllBlank(webMvcProperties.getLookupProtocolHeaders())) {
            WebContext.setLookupProtocolHeaders(webMvcProperties.getLookupProtocolHeaders());
        }

    }

    @Bean
    @ConditionalOnMissingBean
    public YyServletContextInitializer yyServletContextInitializer(WebMvcProperties webMvcProperties) {
        YyServletContextInitializer initializer = new YyServletContextInitializer();

        if (StringUtils.isNotBlank(webMvcProperties.getCookieSessionIdName())) {
            initializer.setSessionIdName(webMvcProperties.getCookieSessionIdName());
        }

        return initializer;
    }

    /**
     * 自定义 HttpMessageConverter
     * https://docs.spring.io/spring-boot/docs/1.5.8.RELEASE/reference/htmlsingle/#HttpMessageConverters
     *
     * @return 返回自定义的 HttpMessageConverter
     */
    @Bean
    @ConditionalOnMissingBean(value = HttpMessageConverters.class, search = SearchStrategy.CURRENT)
    public HttpMessageConverters customConverters() {

        logger.info("创建 [HttpMessageConverters], 支持 JSONP, JAVASCRIPT, JSON");

        ExtendMappingJackson2HttpMessageConverter objectHttpMessageConverter = new ExtendMappingJackson2HttpMessageConverter();

        objectHttpMessageConverter.setDefaultCharset(Charset.forName("UTF-8"));
        objectHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.APPLICATION_JSON_UTF8
        ));

        ObjectMapper objectMapper = new ObjectMapper();
        // 序列化时，include规则为：不包含为空字符串，空集合，空数组或null的属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectHttpMessageConverter.setObjectMapper(objectMapper);

        return new HttpMessageConverters(objectHttpMessageConverter);
    }

    @Bean
    @ConditionalOnMissingBean
    public YyRootFilter yyRootFilter() {
        return new YyRootFilter();
    }

    @Bean
    @ConditionalOnBean(YyRootFilter.class)
    public FilterRegistrationBean filterRegistrationBean(YyRootFilter yyRootFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(yyRootFilter);
        registration.setUrlPatterns(Collections.singletonList("/*"));
        registration.setName("yyRootFilter");
        registration.setOrder(0);
        return registration;
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientIpPageParameter clientIpPageParameter() {
        return new ClientIpPageParameter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ProtocolPageParameter protocolPageParameter() {
        return new ProtocolPageParameter();
    }

    @Bean
    @ConditionalOnMissingBean
    public ProtocolTypePageParameter protocolTypePageParameter() {
        return new ProtocolTypePageParameter();
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestUriPageParameter requestUriPageParameter() {
        return new RequestUriPageParameter();
    }
}
