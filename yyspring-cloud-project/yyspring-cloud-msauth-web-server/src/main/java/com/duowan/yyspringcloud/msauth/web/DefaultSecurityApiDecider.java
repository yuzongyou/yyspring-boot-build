package com.duowan.yyspringcloud.msauth.web;

import com.duowan.common.utils.StringUtil;
import com.duowan.yyspringcloud.msauth.web.annotations.SecurityApi;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/17 19:50
 */
public class DefaultSecurityApiDecider implements SecurityApiDecider {

    /**
     * 资源路径匹配
     **/
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 要进行安全检查的URL匹配
     **/
    private Set<String> includePatterns;
    /**
     * 不需要进行安全检查的URL匹配规则，比includePatterns优先
     **/
    private Set<String> excludePatterns;

    /** 当前是否是开发环境 **/
    private boolean isDev = false;

    /** 开发环境下是否不进行检查 **/
    private boolean uncheckForDev = true;

    public Set<String> getIncludePatterns() {
        return includePatterns;
    }

    public void setIncludePatterns(Set<String> includePatterns) {
        this.includePatterns = includePatterns;
    }

    public Set<String> getExcludePatterns() {
        return excludePatterns;
    }

    public void setExcludePatterns(Set<String> excludePatterns) {
        this.excludePatterns = excludePatterns;
    }

    public boolean isDev() {
        return isDev;
    }

    public void setDev(boolean dev) {
        isDev = dev;
    }

    public boolean isUncheckForDev() {
        return uncheckForDev;
    }

    public void setUncheckForDev(boolean uncheckForDev) {
        this.uncheckForDev = uncheckForDev;
    }

    @Override
    public boolean needSecurityProtected(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 开发环境下不需要进行检查
        if (isDev && uncheckForDev) {
            return false;
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            SecurityApi securityApi = lookupSecurityApi(hm);
            if (null != securityApi && securityApi.value()) {
                // 最高优先级
                return true;
            }
        }

        String requestUri = request.getRequestURI();

        // 检查是否是在忽略的URL地址内，如果是的话就不进行拦截
        if (isMatchPatterns(requestUri, excludePatterns)) {
            return false;
        }

        // 检查是否是在需要拦截的URL地址内，如果是的话就需要拦截
        return isMatchPatterns(requestUri, includePatterns);
    }

    private boolean isMatchPatterns(String requestUri, Set<String> patterns) {
        if (patterns == null || patterns.isEmpty() || StringUtil.isBlank(requestUri)) {
            return false;
        }
        for (String pattern : patterns) {
            if (pathMatcher.match(pattern, requestUri)) {
                return true;
            }
        }
        return false;
    }

    private SecurityApi lookupSecurityApi(HandlerMethod hm) {

        SecurityApi securityApi = hm.getMethodAnnotation(SecurityApi.class);
        if (null == securityApi) {
            securityApi = hm.getBeanType().getAnnotation(SecurityApi.class);
        }

        return securityApi;
    }
}
