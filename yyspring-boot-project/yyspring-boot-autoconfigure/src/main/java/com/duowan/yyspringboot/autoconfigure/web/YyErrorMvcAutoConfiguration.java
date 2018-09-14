package com.duowan.yyspringboot.autoconfigure.web;

import com.duowan.common.web.error.YyBasicErrorController;
import com.duowan.common.web.exception.ExtendErrorAttributes;
import com.duowan.common.web.exception.handler.*;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/10 9:28
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, YyBasicErrorController.class})
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class})
public class YyErrorMvcAutoConfiguration {

    private final ServerProperties serverProperties;

    private final List<ErrorViewResolver> errorViewResolvers;

    public YyErrorMvcAutoConfiguration(ServerProperties serverProperties, List<ErrorViewResolver> errorViewResolvers) {
        this.serverProperties = serverProperties;
        this.errorViewResolvers = errorViewResolvers;
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorController.class, search = SearchStrategy.CURRENT)
    public YyBasicErrorController basicErrorController(ErrorAttributes errorAttributes) {
        return new YyBasicErrorController(errorAttributes, this.serverProperties.getError(),
                this.errorViewResolvers);
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    public ExtendErrorAttributes errorAttributes(List<ExceptionViewResolver> exceptionViewResolverList) {
        return new ExtendErrorAttributes(exceptionViewResolverList);
    }

    @Bean
    @ConditionalOnMissingBean(AjaxViewExceptionViewResolver.class)
    public AjaxViewExceptionViewResolver ajaxViewExceptionViewResolver() {
        return new AjaxViewExceptionViewResolver();
    }

    @Bean
    @ConditionalOnMissingBean(JsonViewExceptionViewResolver.class)
    public JsonViewExceptionViewResolver jsonViewExceptionViewResolver() {
        return new JsonViewExceptionViewResolver();
    }

    @Bean
    @ConditionalOnMissingBean(ResponseBodyExceptionViewResolver.class)
    public ResponseBodyExceptionViewResolver responseBodyExceptionViewResolver() {
        return new ResponseBodyExceptionViewResolver();
    }

    @Bean
    @ConditionalOnMissingBean(TextViewExceptionViewResolver.class)
    public TextViewExceptionViewResolver textViewExceptionViewResolver() {
        return new TextViewExceptionViewResolver();
    }
}
