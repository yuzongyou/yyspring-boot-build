package com.duowan.yyspringcloud.msauth.web;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import com.duowan.yyspringcloud.msauth.web.annotations.SecurityApi;

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

    @Override
    public boolean needSecurityProtected(HttpServletRequest request, HttpServletResponse response, Object handler) {

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
        if (patterns == null || patterns.isEmpty() || StringUtils.isBlank(requestUri)) {
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
