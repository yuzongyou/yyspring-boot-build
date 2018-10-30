package com.duowan.yyspringboot.autoconfigure.web;

import com.duowan.common.web.WebContext;
import com.duowan.common.web.YyServletContextInitializer;
import com.duowan.common.web.converter.YyDateConverter;
import com.duowan.common.web.filter.YyRootFilter;
import com.duowan.common.web.view.AjaxView;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

import javax.servlet.Servlet;
import java.util.Collections;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 10:27
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, YyServletContextInitializer.class})
@EnableConfigurationProperties({WebMvcProperties.class})
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

    @Bean
    @ConditionalOnMissingBean
    public YyDateConverter yyDateConverter() {
        return new YyDateConverter();
    }

    @Bean
    @ConditionalOnMissingBean
    public YyRootFilter yyRootFilter() {
        return new YyRootFilter();
    }

    @Bean
    @ConditionalOnBean(YyRootFilter.class)
    public FilterRegistrationBean filterRegistrationBean(YyRootFilter yyRootFilter) {
        FilterRegistrationBean<YyRootFilter> registration = new FilterRegistrationBean<>(yyRootFilter);
        registration.setUrlPatterns(Collections.singletonList("/*"));
        registration.setName("yyRootFilter");
        registration.setOrder(0);
        return registration;
    }

    @Configuration
    @ConditionalOnProperty(value = "yyspring.mvc.jsonp.enabled", matchIfMissing = true)
    @EnableConfigurationProperties({WebMvcProperties.class})
    public static class JsonpConfiguration {

        @ControllerAdvice
        public class YyJsonpAdvice extends AbstractJsonpResponseBodyAdvice {

            public YyJsonpAdvice(WebMvcProperties mvcProperties) {
                super(mvcProperties.getJsonpCallbackVars());
            }
        }
    }
}
