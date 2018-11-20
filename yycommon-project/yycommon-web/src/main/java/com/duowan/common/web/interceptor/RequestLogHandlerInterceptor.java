package com.duowan.common.web.interceptor;

import com.duowan.common.utils.JsonUtil;
import com.duowan.common.web.annotations.LogRequestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/6 20:53
 */
public class RequestLogHandlerInterceptor implements HandlerInterceptor {

    private static final String REQ_INFO_KEY = "RequestLogHandlerInterceptor.REQ_INFO";

    private static final String PATTERN_ALL = "*";
    private static final String PATTERN_SPLIT = ",";
    private static final Set<String> EMPTY_SET = new HashSet<>(0);

    private final Logger logger = LoggerFactory.getLogger(getClass());


    class ReqInfo {
        long startTime;
        Map<String, Object> reqInfoMap;

        ReqInfo(long startTime, Map<String, Object> reqInfoMap) {
            this.startTime = startTime;
            this.reqInfoMap = reqInfoMap;
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                LogRequestInfo logRequestInfo = handlerMethod.getMethodAnnotation(LogRequestInfo.class);
                if (null == logRequestInfo) {
                    logRequestInfo = handlerMethod.getBeanType().getAnnotation(LogRequestInfo.class);
                }
                if (logRequestInfo == null) {
                    return true;
                }

                doLogRequestInfo(request, logRequestInfo);

                return true;
            }
        } catch (Exception e) {
            logger.info("Log request check failed: " + e.getMessage());
        }
        return true;
    }

    private void doLogRequestInfo(HttpServletRequest request, LogRequestInfo logRequestInfo) {

        Map<String, Object> logMap = new HashMap<>();

        fillParam(logRequestInfo, logMap, request);

        fillHeader(logRequestInfo, logMap, request);

        fillCookie(logRequestInfo, logMap, request);

        if (!logMap.isEmpty()) {
            request.setAttribute(REQ_INFO_KEY, new ReqInfo(System.currentTimeMillis(), logMap));
        }

    }

    private void fillCookie(LogRequestInfo logRequestInfo, Map<String, Object> logMap, HttpServletRequest request) {

        String excludeParams = logRequestInfo.excludeCookies();
        if (PATTERN_ALL.equals(excludeParams)) {
            return;
        }
        String includeParams = logRequestInfo.includeCookies();
        boolean includeAll = PATTERN_ALL.equals(includeParams);

        Set<String> excludeNames = toParamNameSet(excludeParams);
        Set<String> includeNames = null;
        if (!includeAll) {
            includeNames = toParamNameSet(includeParams);
        }

        Map<String, Object> map = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {

                String name = cookie.getName();

                if (excludeNames.contains(name)) {
                    continue;
                }
                if (includeAll || includeNames.contains(name)) {
                    map.put(name, cookie.getValue());
                }

            }
        }
        if (!map.isEmpty()) {
            logMap.put("cookie", map);
        }

    }

    private void fillHeader(LogRequestInfo logRequestInfo, Map<String, Object> logMap, HttpServletRequest request) {

        String excludeParams = logRequestInfo.excludeHeaders();
        if (PATTERN_ALL.equals(excludeParams)) {
            return;
        }
        String includeParams = logRequestInfo.includeHeaders();
        boolean includeAll = PATTERN_ALL.equals(includeParams);

        Set<String> excludeNames = toParamNameSet(excludeParams);
        Set<String> includeNames = null;
        if (!includeAll) {
            includeNames = toParamNameSet(includeParams);
        }

        Map<String, Object> map = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (excludeNames.contains(name)) {
                continue;
            }
            if (includeAll || includeNames.contains(name)) {
                String value = request.getHeader(name);
                map.put(name, value);
            }
        }
        if (!map.isEmpty()) {
            logMap.put("header", map);
        }

    }

    private Set<String> toParamNameSet(String params) {
        if (StringUtils.isEmpty(params)) {
            return EMPTY_SET;
        }
        return new HashSet<>(Arrays.asList(params.split(PATTERN_SPLIT)));
    }

    private void fillParam(LogRequestInfo logRequestInfo, Map<String, Object> logMap, HttpServletRequest request) {

        String excludeParams = logRequestInfo.excludeParams();
        if (PATTERN_ALL.equals(excludeParams)) {
            return;
        }
        String includeParams = logRequestInfo.includeParams();
        boolean includeAll = PATTERN_ALL.equals(includeParams);

        Set<String> excludeNames = toParamNameSet(excludeParams);
        Set<String> includeNames = null;
        if (!includeAll) {
            includeNames = toParamNameSet(includeParams);
        }

        Map<String, Object> map = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();

            if (excludeNames.contains(name)) {
                continue;
            }
            if (includeAll || includeNames.contains(name)) {
                String value = request.getParameter(name);
                map.put(name, value);
            }
        }
        if (!map.isEmpty()) {
            logMap.put("param", map);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            Object obj = request.getAttribute(REQ_INFO_KEY);
            if (obj instanceof ReqInfo) {
                ReqInfo reqInfo = (ReqInfo) obj;
                long takeTime = System.currentTimeMillis() - reqInfo.startTime;
                logger.info("Req: takeTime=" + takeTime + ", info=" + JsonUtil.toJson(reqInfo.reqInfoMap, false) + (ex == null ? "" : ", error=" + ex.getMessage()));
            }
        } catch (Exception e) {
            logger.warn("Log request info error: " + e.getMessage());
        }
    }
}
