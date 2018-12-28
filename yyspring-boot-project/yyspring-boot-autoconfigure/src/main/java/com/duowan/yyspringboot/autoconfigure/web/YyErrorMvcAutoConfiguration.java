package com.duowan.yyspringboot.autoconfigure.web;

import com.duowan.common.web.exception.handler.*;
import com.duowan.yyspringboot.autoconfigure.web.error.ExtendErrorAttributes;
import com.duowan.yyspringboot.autoconfigure.web.error.YyBasicErrorController;
import org.springframework.beans.factory.annotation.Autowired;
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
@ConditionalOnClass({Servlet.class, DispatcherServlet.class})
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@EnableConfigurationProperties({ServerProperties.class, ResourceProperties.class, WebMvcProperties.class})
public class YyErrorMvcAutoConfiguration {

    private final ServerProperties serverProperties;

    private final WebMvcProperties webMvcProperties;

    private final List<ErrorViewResolver> errorViewResolvers;

    public YyErrorMvcAutoConfiguration(ServerProperties serverProperties, WebMvcProperties webMvcProperties, List<ErrorViewResolver> errorViewResolvers) {
        this.serverProperties = serverProperties;
        this.webMvcProperties = webMvcProperties;
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
    @ConditionalOnMissingBean
    public CodeExceptionErrorMessageReader codeExceptionErrorMessageReader() {
        return new CodeExceptionErrorMessageReader();
    }

    @Bean
    @ConditionalOnMissingBean
    public ValidationExceptionErrorMessageReader validationExceptionErrorMessageReader() {
        return new ValidationExceptionErrorMessageReader();
    }

    @Bean
    @ConditionalOnMissingBean(AjaxViewExceptionViewResolver.class)
    public AjaxViewExceptionViewResolver ajaxViewExceptionViewResolver(@Autowired(required = false) List<ErrorMessageReader> errorMessageReaderList) {
        return new AjaxViewExceptionViewResolver(errorMessageReaderList, webMvcProperties.isExceptionResolverLogException());
    }

    @Bean
    @ConditionalOnMissingBean(JsonViewExceptionViewResolver.class)
    public JsonViewExceptionViewResolver jsonViewExceptionViewResolver(@Autowired(required = false) List<ErrorMessageReader> errorMessageReaderList) {
        return new JsonViewExceptionViewResolver(errorMessageReaderList, webMvcProperties.isExceptionResolverLogException());
    }

    @Bean
    @ConditionalOnMissingBean(ResponseBodyExceptionViewResolver.class)
    public ResponseBodyExceptionViewResolver responseBodyExceptionViewResolver(@Autowired(required = false) List<ErrorMessageReader> errorMessageReaderList) {
        return new ResponseBodyExceptionViewResolver(errorMessageReaderList, webMvcProperties.isExceptionResolverLogException());
    }

    @Bean
    @ConditionalOnMissingBean(JsonResponseBodyExceptionViewResolver.class)
    public JsonResponseBodyExceptionViewResolver jsonResponseBodyExceptionViewResolver(@Autowired(required = false) List<ErrorMessageReader> errorMessageReaderList) {
        return new JsonResponseBodyExceptionViewResolver(errorMessageReaderList, webMvcProperties.isExceptionResolverLogException());
    }

    @Bean
    @ConditionalOnMissingBean(TextViewExceptionViewResolver.class)
    public TextViewExceptionViewResolver textViewExceptionViewResolver(@Autowired(required = false) List<ErrorMessageReader> errorMessageReaderList) {
        return new TextViewExceptionViewResolver(errorMessageReaderList, webMvcProperties.isExceptionResolverLogException());
    }
}
