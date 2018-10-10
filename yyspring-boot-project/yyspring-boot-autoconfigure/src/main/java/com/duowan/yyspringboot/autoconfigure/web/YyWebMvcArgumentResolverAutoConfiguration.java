package com.duowan.yyspringboot.autoconfigure.web;

import com.duowan.common.web.YyServletContextInitializer;
import com.duowan.common.web.argresolvers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Servlet;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 18:21
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, YyServletContextInitializer.class})
public class YyWebMvcArgumentResolverAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ClientIpArgumentResolver clientIpArgumentResolver() {
        return new ClientIpArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public ProtocolArgumentResolver protocolArgumentResolver() {
        return new ProtocolArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public ProtocolTypeArgumentResolver protocolTypeArgumentResolver() {
        return new ProtocolTypeArgumentResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestUriArgumentResolver requestUriArgumentResolver() {
        return new RequestUriArgumentResolver();
    }

    @Configuration
    public class ArgumentResolverAutoConfiguration implements WebMvcConfigurer {

        @Autowired(required = false)
        private List<AbstractArgumentResolver> customArgumentResolvers;

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            if (null != customArgumentResolvers && !customArgumentResolvers.isEmpty()) {

                // 排序
                AnnotationAwareOrderComparator.sort(customArgumentResolvers);

                for (HandlerMethodArgumentResolver resolver : customArgumentResolvers) {
                    argumentResolvers.add(resolver);
                }
            }
        }
    }
}
