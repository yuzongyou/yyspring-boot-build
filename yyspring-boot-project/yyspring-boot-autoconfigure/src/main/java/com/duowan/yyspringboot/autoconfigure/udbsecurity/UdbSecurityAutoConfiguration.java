package com.duowan.yyspringboot.autoconfigure.udbsecurity;

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
    public UdbLoginRequirePatternProvider udbLoginRequirePatternProvider() {
        return new UdbLoginRequirePatternProvider();
    }

    @Bean
    public UdbSecurityController udbSecurityController(UdbSecurityProperties udbSecurityProperties) {
        return new UdbSecurityController(udbSecurityProperties.getAppid(), udbSecurityProperties.getAppkey());
    }

    @Bean
    public MappedInterceptor mappedInterceptor(UdbSecurityProperties udbSecurityProperties,
                                               @Autowired(required = false) PrivilegeInterceptor privilegeInterceptor,
                                               @Autowired(required = false) List<PatternProvider> patternProviders) {

        Set<String> excludePackagesOrClassNames = new HashSet<String>(Arrays.asList(udbSecurityProperties.getExcludePackagesAndClasses()));
        // 默认派排除 spring 内置的
        excludePackagesOrClassNames.add("org.springframework");

        UdbSecurityInterceptor udbSecurityInterceptor = new UdbSecurityInterceptor(
                udbSecurityProperties.getAppid(),
                udbSecurityProperties.getAppkey(),
                udbSecurityProperties.getDefaultCheckMode(),
                excludePackagesOrClassNames,
                udbSecurityProperties.isStaticSkip());

        if (null != privilegeInterceptor) {
            udbSecurityInterceptor.setPrivilegeInterceptor(privilegeInterceptor);
        }

        return new MappedInterceptor(
                getPathPatterns(udbSecurityProperties, patternProviders),
                getExcludePatterns(udbSecurityProperties, patternProviders),
                udbSecurityInterceptor);
    }

    private String[] getExcludePatterns(UdbSecurityProperties udbSecurityProperties, List<PatternProvider> patternProviders) {
        Set<String> patterns = new HashSet<String>(Arrays.asList(udbSecurityProperties.getExcludePathPatterns()));

        if (patternProviders != null && !patternProviders.isEmpty()) {
            for (PatternProvider patternProvider : patternProviders) {
                Set<String> temp = patternProvider.getExcludePatterns();
                if (null != temp && !temp.isEmpty()) {
                    patterns.addAll(temp);
                }
            }
        }

        return patterns.toArray(new String[patterns.size()]);
    }

    private String[] getPathPatterns(UdbSecurityProperties udbSecurityProperties, List<PatternProvider> patternProviders) {
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

        return patterns.toArray(new String[patterns.size()]);
    }

}
