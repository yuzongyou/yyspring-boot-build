package com.duowan.yyspringboot.autoconfigure.udbsecurity;

import com.duowan.udb.security.NeedForwardLoginUIDecider;
import com.duowan.udb.security.PrivilegeInterceptor;
import com.duowan.udb.security.UdbSecurityInterceptor;
import com.duowan.udb.security.controller.UdbSecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.handler.MappedInterceptor;

import javax.servlet.Servlet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/22 11:09
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, UdbSecurityInterceptor.class})
@ConditionalOnExpression("${yyspring.udbsecurity.interceptor-enabled:true}")
@EnableConfigurationProperties(UdbSecurityProperties.class)
public class UdbSecurityAutoConfiguration {

    @Bean
    public WebJsonViewNeedForwardLoginUIDecider webJsonViewNeedForwardLoginUIDecider() {
        return new WebJsonViewNeedForwardLoginUIDecider();
    }

    @Bean
    public UdbLoginRequirePatternProvider udbLoginRequirePatternProvider() {
        return new UdbLoginRequirePatternProvider();
    }

    @Bean
    public UdbSecurityController udbSecurityController(UdbSecurityProperties udbSecurityProperties) {
        return new UdbSecurityController(udbSecurityProperties.getAppid(), udbSecurityProperties.getAppkey());
    }

    @Bean
    public MappedInterceptor udbSecurityMappedInterceptor(UdbSecurityProperties udbSecurityProperties,
                                                          @Autowired(required = false) PrivilegeInterceptor privilegeInterceptor,
                                                          @Autowired(required = false) List<PatternProvider> patternProviders,
                                                          @Autowired(required = false) List<NeedForwardLoginUIDecider> needForwardLoginUIDeciders) {

        Set<String> excludePackagesOrClassNames = new HashSet<String>(Arrays.asList(udbSecurityProperties.getExcludePackagesAndClasses()));
        // 默认派排除 spring 内置的
        UdbSecurityInterceptor udbSecurityInterceptor = new UdbSecurityInterceptor(
                udbSecurityProperties.getAppid(),
                udbSecurityProperties.getAppkey(),
                udbSecurityProperties.getDefaultCheckMode(),
                getPathPatterns(udbSecurityProperties, patternProviders),
                getExcludePatterns(udbSecurityProperties, patternProviders),
                excludePackagesOrClassNames,
                udbSecurityProperties.isStaticSkip());

        if (null != privilegeInterceptor) {
            udbSecurityInterceptor.setPrivilegeInterceptor(privilegeInterceptor);
        }
        // 设置决定是否调整登录页面
        udbSecurityInterceptor.setNeedForwardLoginUIDeciders(needForwardLoginUIDeciders);

        return new MappedInterceptor(new String[]{"/**"}, udbSecurityInterceptor);
    }

    private Set<String> getExcludePatterns(UdbSecurityProperties udbSecurityProperties, List<PatternProvider> patternProviders) {
        Set<String> patterns = new HashSet<String>(Arrays.asList(udbSecurityProperties.getExcludePathPatterns()));

        if (patternProviders != null && !patternProviders.isEmpty()) {
            for (PatternProvider patternProvider : patternProviders) {
                Set<String> temp = patternProvider.getExcludePatterns();
                if (null != temp && !temp.isEmpty()) {
                    patterns.addAll(temp);
                }
            }
        }

        return patterns;
    }

    private Set<String> getPathPatterns(UdbSecurityProperties udbSecurityProperties, List<PatternProvider> patternProviders) {
        Set<String> patterns = new HashSet<>(Arrays.asList(udbSecurityProperties.getPathPatterns()));

        if (patternProviders != null && !patternProviders.isEmpty()) {
            for (PatternProvider patternProvider : patternProviders) {
                Set<String> temp = patternProvider.getIncludePatterns();
                if (null != temp && !temp.isEmpty()) {
                    patterns.addAll(temp);
                }
            }
        }

        if (patterns.isEmpty()) {
            patterns.add("/admin/**");
        }

        return patterns;
    }

}
