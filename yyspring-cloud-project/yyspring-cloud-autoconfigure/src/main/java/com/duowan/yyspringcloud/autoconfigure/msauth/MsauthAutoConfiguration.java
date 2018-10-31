package com.duowan.yyspringcloud.autoconfigure.msauth;

import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspringcloud.msauth.app.ApolloAppReader;
import com.duowan.yyspringcloud.msauth.app.AppReader;
import com.duowan.yyspringcloud.msauth.app.CompositeAppReader;
import com.duowan.yyspringcloud.msauth.app.EnvironmentAppReader;
import com.duowan.yyspringcloud.msauth.feign.FeignAuthHeaderRequestInterceptor;
import com.duowan.yyspringcloud.msauth.web.*;
import com.duwoan.yyspringcloud.msauth.gateway.GatewayAddHeaderGlobalFilter;
import feign.RequestInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.handler.MappedInterceptor;

import java.util.List;

/**
 * 微服务 RPC 调用自动配置
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 11:12
 */
@Configuration
@EnableConfigurationProperties({MsauthProperties.class})
@ConditionalOnClass({AppReader.class})
public class MsauthAutoConfiguration {

    private static String resolveAppId(Environment environment, MsauthProperties properties) {
        if (StringUtils.isNotBlank(properties.getAppId())) {
            return properties.getAppId();
        }

        String appId = environment.getProperty("spring.application.name");
        if (StringUtils.isBlank(appId)) {
            throw new IllegalArgumentException("Ms auth appid should not be null");
        }

        return appId;
    }

    @Bean
    @ConditionalOnExpression("${yyspring.cloud.msauth.enabled-environment-app-reader:true}")
    public EnvironmentAppReader environmentAppReader(Environment environment) {
        return new EnvironmentAppReader(environment);
    }

    @Bean
    @ConditionalOnExpression("${yyspring.cloud.msauth.enabled-apollo-app-reader:true}")
    public ApolloAppReader apolloAppReader(MsauthProperties properties) {
        return new ApolloAppReader(properties.getApolloNamespaces());
    }

    @Bean
    @Primary
    public CompositeAppReader compositeAppReader(List<AppReader> appReaderList) {
        return new CompositeAppReader(appReaderList);
    }

    @Configuration
    @EnableConfigurationProperties({MsauthProperties.class})
    @ConditionalOnClass({GlobalFilter.class})
    public static class MsauthClientGatewayConfiguration {
        @Bean
        @ConditionalOnMissingBean
        public GatewayAddHeaderGlobalFilter gatewayAddHeaderGlobalFilter(MsauthProperties properties, AppReader appReader) {
            GatewayAddHeaderGlobalFilter filter = new GatewayAddHeaderGlobalFilter(properties.getAppId(), appReader);

            filter.setOrder(properties.getHeaderGatewayFilterOrder());
            if (StringUtils.isNotBlank(properties.getAuthHeader())) {
                filter.setAuthHeader(properties.getAuthHeader());
            }

            filter.setSignLiveSeconds(properties.getSignLiveSeconds());

            return filter;
        }
    }

    @Configuration
    @ConditionalOnClass(FeignAuthHeaderRequestInterceptor.class)
    @EnableConfigurationProperties({MsauthProperties.class})
    public static class MsauthClientConfiguration {

        @Configuration
        @EnableConfigurationProperties({MsauthProperties.class})
        @ConditionalOnClass({RequestInterceptor.class})
        public static class MsauthClientFeignConfiguration {

            @Bean
            @ConditionalOnMissingBean
            public FeignAuthHeaderRequestInterceptor feignAuthHeaderRequestInterceptor(MsauthProperties properties, Environment environment, AppReader appReader) {
                FeignAuthHeaderRequestInterceptor interceptor = new FeignAuthHeaderRequestInterceptor(resolveAppId(environment, properties), appReader);

                if (StringUtils.isNotBlank(properties.getAuthHeader())) {
                    interceptor.setAuthHeader(properties.getAuthHeader());
                }

                interceptor.setSignLiveSeconds(properties.getSignLiveSeconds());

                return interceptor;
            }
        }

    }

    @Configuration
    @ConditionalOnClass(SecurityApiAuthServerHandlerInterceptor.class)
    @EnableConfigurationProperties({MsauthProperties.class})
    public static class MsauthServerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public DefaultSecurityApiDecider defaultSecurityApiDecider(MsauthProperties properties) {

            DefaultSecurityApiDecider decider = new DefaultSecurityApiDecider();

            decider.setIncludePatterns(properties.getSecurityIncludePatterns());
            decider.setExcludePatterns(properties.getSecurityExcludePatterns());
            decider.setDev(AppContext.isDev());
            decider.setUncheckForDev(properties.isUncheckForDev());

            return decider;
        }

        @Bean
        @ConditionalOnMissingBean
        public DefaultUnauthorizedHandler defaultUnauthorizedHandler() {
            return new DefaultUnauthorizedHandler();
        }

        @Bean
        @ConditionalOnMissingBean
        public Md5SignSecurityValidator md5SignSecurityValidator(AppReader appReader) {
            return new Md5SignSecurityValidator(appReader);
        }

        @Bean
        @ConditionalOnMissingBean
        public SecurityApiAuthServerHandlerInterceptor securityApiAuthServerHandlerInterceptor(
                MsauthProperties properties,
                SecurityApiDecider decider,
                SecurityValidator validator,
                UnauthorizedHandler unauthorizedHandler) {
            SecurityApiAuthServerHandlerInterceptor interceptor = new SecurityApiAuthServerHandlerInterceptor(decider, validator, unauthorizedHandler);

            if (StringUtils.isNotBlank(properties.getAuthHeader())) {
                interceptor.setAuthHeader(properties.getAuthHeader());
            }

            return interceptor;
        }

        @Bean
        public MappedInterceptor mappedSecurityApiAuthServerHandlerInterceptor(SecurityApiAuthServerHandlerInterceptor interceptor) {
            return new MappedInterceptor(new String[]{"/**"}, interceptor);
        }
    }


}
