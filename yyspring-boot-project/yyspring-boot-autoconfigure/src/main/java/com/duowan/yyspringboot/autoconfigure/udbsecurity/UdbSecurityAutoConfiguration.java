package com.duowan.yyspringboot.autoconfigure.udbsecurity;

import com.duowan.common.utils.ConvertUtil;
import com.duowan.udb.sdk.UdbConstants;
import com.duowan.udb.security.PrivilegeInterceptor;
import com.duowan.udb.sdk.UdbContext;
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
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, UdbSecurityInterceptor.class})
@ConditionalOnExpression("${yyspring.udb.interceptor-enabled:true}")
@EnableConfigurationProperties(UdbProperties.class)
public class UdbSecurityAutoConfiguration {

    public UdbSecurityAutoConfiguration(UdbProperties udbProperties) {
        // 初始化UDB的配置
        UdbContext.setAppid(ConvertUtil.toString(udbProperties.getAppid(), UdbConstants.DEFAULT_UDB_APPID));
        UdbContext.setAppkey(ConvertUtil.toString(udbProperties.getAppkey(), UdbConstants.DEFAULT_UDB_APPKEY));
    }

    @Bean
    public UdbLoginRequirePatternProvider udbLoginRequirePatternProvider() {
        return new UdbLoginRequirePatternProvider();
    }

    @Bean
    public UdbSecurityController udbSecurityController() {
        return new UdbSecurityController();
    }

    @Bean
    public MappedInterceptor mappedInterceptor(UdbProperties udbProperties,
                                               @Autowired(required = false) PrivilegeInterceptor privilegeInterceptor,
                                               @Autowired(required = false) List<PatternProvider> patternProviders) {

        Set<String> excludePackagesOrClassNames = new HashSet<String>(Arrays.asList(udbProperties.getExcludePackagesAndClasses()));
        // 默认派排除 spring 内置的
        excludePackagesOrClassNames.add("org.springframework");

        UdbSecurityInterceptor udbSecurityInterceptor = new UdbSecurityInterceptor(udbProperties.getDefaultCheckMode(),
                excludePackagesOrClassNames,
                udbProperties.isStaticSkip());

        if (null != privilegeInterceptor) {
            udbSecurityInterceptor.setPrivilegeInterceptor(privilegeInterceptor);
        }

        return new MappedInterceptor(
                getPathPatterns(udbProperties, patternProviders),
                getExcludePatterns(udbProperties, patternProviders),
                udbSecurityInterceptor);
    }

    private String[] getExcludePatterns(UdbProperties udbProperties, List<PatternProvider> patternProviders) {
        Set<String> patterns = new HashSet<String>(Arrays.asList(udbProperties.getExcludePathPatterns()));

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

    private String[] getPathPatterns(UdbProperties udbProperties, List<PatternProvider> patternProviders) {
        Set<String> patterns = new HashSet<>(Arrays.asList(udbProperties.getPathPatterns()));

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
