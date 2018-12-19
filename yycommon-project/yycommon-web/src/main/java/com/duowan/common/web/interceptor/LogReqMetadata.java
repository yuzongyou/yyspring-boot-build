package com.duowan.common.web.interceptor;

import com.duowan.common.web.annotations.LogRequestInfo;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/20 14:10
 */
public class LogReqMetadata {

    private static Map<String, LogReqMetadata> metadataMap = new HashMap<>();

    private static final LogReqMetadata EMPTY_LOG_REQ_METADATA = new LogReqMetadata(null, null);

    private static final String PATTERN_ALL = "*";
    private static final String PATTERN_SPLIT = ",";
    private static final Set<String> EMPTY_SET = new HashSet<>(0);

    private boolean includeParamAll;
    private boolean excludeParamAll;
    private boolean includeCookieAll;
    private boolean excludeCookieAll;
    private boolean includeHeaderAll;
    private boolean excludeHeaderAll;
    private Set<String> includeParams;
    private Set<String> excludeParams;
    private Set<String> includeCookies;
    private Set<String> excludeCookies;
    private Set<String> includeHeaders;
    private Set<String> excludeHeaders;

    private LogReqMetadata(HttpServletRequest request, LogRequestInfo logRequestInfo) {
        if (null == logRequestInfo || request == null) {
            initAsEmpty();
        } else {
            ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
            if (context == null) {
                initAsEmpty();
                return;
            }
            Environment environment = context.getEnvironment();

            initParamMetadata(environment, logRequestInfo);
            initCookieMetadata(environment, logRequestInfo);
            initHeaderMetadata(environment, logRequestInfo);
        }
    }

    private String resolveProperty(Environment environment, String text) {
        if (StringUtils.isEmpty(text)) {
            return text;
        }
        return environment.resolvePlaceholders(text);
    }

    private void initHeaderMetadata(Environment environment, LogRequestInfo logRequestInfo) {
        String tempExcludeParams = resolveProperty(environment, logRequestInfo.excludeHeaders());
        if (PATTERN_ALL.equals(tempExcludeParams)) {
            this.excludeHeaderAll = true;
            return;
        }
        String tempIncludeParams = resolveProperty(environment, logRequestInfo.includeHeaders());
        this.includeHeaderAll = PATTERN_ALL.equals(tempIncludeParams);

        this.excludeHeaders = toParamNameSet(tempExcludeParams);
        if (!this.includeHeaderAll) {
            this.includeHeaders = toParamNameSet(tempIncludeParams);
        }
        boolean hadIncludeHeader = this.includeHeaders != null && !this.includeHeaders.isEmpty();
        if (!this.includeHeaderAll && !hadIncludeHeader) {
            this.excludeHeaderAll = true;
        }
    }

    private void initCookieMetadata(Environment environment, LogRequestInfo logRequestInfo) {
        String tempExcludeParams = resolveProperty(environment, logRequestInfo.excludeCookies());
        if (PATTERN_ALL.equals(tempExcludeParams)) {
            this.excludeCookieAll = true;
            return;
        }
        String tempIncludeParams = resolveProperty(environment, logRequestInfo.includeCookies());
        this.includeCookieAll = PATTERN_ALL.equals(tempIncludeParams);

        this.excludeCookies = toParamNameSet(tempExcludeParams);
        if (!this.includeCookieAll) {
            this.includeCookies = toParamNameSet(tempIncludeParams);
        }

        boolean hadInclude = this.includeCookies != null && !this.includeCookies.isEmpty();
        if (!this.includeCookieAll && !hadInclude) {
            this.excludeCookieAll = true;
        }
    }

    private void initParamMetadata(Environment environment, LogRequestInfo logRequestInfo) {
        String tempExcludeParams = resolveProperty(environment, logRequestInfo.excludeParams());
        if (PATTERN_ALL.equals(tempExcludeParams)) {
            this.excludeParamAll = true;
            return;
        }
        String tempIncludeParams = resolveProperty(environment, logRequestInfo.includeParams());
        this.includeParamAll = PATTERN_ALL.equals(tempIncludeParams);

        this.excludeParams = toParamNameSet(tempExcludeParams);
        if (!this.includeParamAll) {
            this.includeParams = toParamNameSet(tempIncludeParams);
        }

        boolean hadInclude = this.includeParams != null && !this.includeParams.isEmpty();
        if (!this.includeParamAll && !hadInclude) {
            this.excludeParamAll = true;
        }
    }

    private Set<String> toParamNameSet(String params) {
        if (StringUtils.isEmpty(params)) {
            return EMPTY_SET;
        }
        return new HashSet<>(Arrays.asList(params.split(PATTERN_SPLIT)));
    }

    private void initAsEmpty() {
        this.excludeParamAll = true;
        this.excludeCookieAll = true;
        this.excludeHeaderAll = true;
    }

    public static LogReqMetadata get(HttpServletRequest request, LogRequestInfo logRequestInfo) {
        if (request == null || logRequestInfo == null) {
            return EMPTY_LOG_REQ_METADATA;
        }

        String requestMethod = request.getMethod();
        String requestUri = request.getRequestURI();

        return metadataMap.computeIfAbsent(requestMethod + " " + requestUri, key -> new LogReqMetadata(request, logRequestInfo));
    }

    public boolean isIncludeParamAll() {
        return includeParamAll && !excludeParamAll;
    }

    public boolean isExcludeParamAll() {
        return excludeParamAll;
    }

    public boolean isIncludeCookieAll() {
        return includeCookieAll && !excludeCookieAll;
    }

    public boolean isExcludeCookieAll() {
        return excludeCookieAll;
    }

    public boolean isIncludeHeaderAll() {
        return includeHeaderAll && !excludeHeaderAll;
    }

    public boolean isExcludeHeaderAll() {
        return excludeHeaderAll;
    }

    public boolean includeParam(String name) {
        if (isIncludeParamAll()) {
            return true;
        }
        if (null != excludeParams && excludeParams.contains(name)) {
            return false;
        }
        return null != includeParams && includeParams.contains(name);
    }

    public boolean includeCookie(String name) {
        if (isIncludeCookieAll()) {
            return true;
        }
        if (null != excludeCookies && excludeCookies.contains(name)) {
            return false;
        }
        return null != includeCookies && includeCookies.contains(name);
    }

    public boolean includeHeader(String name) {
        if (isIncludeHeaderAll()) {
            return true;
        }
        if (null != excludeHeaders && excludeHeaders.contains(name)) {
            return false;
        }
        return null != includeHeaders && includeHeaders.contains(name);
    }
}
