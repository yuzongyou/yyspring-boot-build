package com.duowan.udb.security;

import com.duowan.common.exception.CodeException;
import com.duowan.common.utils.CookieUtil;
import com.duowan.common.utils.JsonUtil;
import com.duowan.common.utils.SessionUtil;
import com.duowan.common.utils.UrlUtil;
import com.duowan.common.web.view.AjaxView;
import com.duowan.common.web.view.JsonView;
import com.duowan.common.web.view.TextView;
import com.duowan.udb.sdk.UdbAuthLevel;
import com.duowan.udb.sdk.UdbConstants;
import com.duowan.udb.sdk.UdbOauth;
import com.duowan.udb.security.annotations.IgnoredUdbCheck;
import com.duowan.udb.security.annotations.UdbCheck;
import com.duowan.udb.security.exception.UdbUnLoginException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * @author dw_xiajiqiu1
 */
public class UdbSecurityInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CheckMode defaultCheckMode;

    private final String udbAppId;
    private final String udbAppKey;

    /**
     * 要忽略的包名或者类名
     */
    private Set<String> excludePackagesOrClassNames;

    /**
     * 跳过静态资源
     **/
    private final boolean staticSkip;

    private PrivilegeInterceptor privilegeInterceptor;

    public UdbSecurityInterceptor(String udbAppId, String udbAppKey, CheckMode defaultCheckMode) {
        this(udbAppId, udbAppKey, defaultCheckMode, new HashSet<String>());
    }

    public UdbSecurityInterceptor(String udbAppId, String udbAppKey, CheckMode defaultCheckMode, Set<String> excludePackagesOrClassNames) {
        this(udbAppId, udbAppKey, defaultCheckMode, excludePackagesOrClassNames, false);
    }

    public UdbSecurityInterceptor(String udbAppId, String udbAppKey, CheckMode defaultCheckMode, Set<String> excludePackagesOrClassNames, boolean staticSkip) {
        this.defaultCheckMode = defaultCheckMode;
        this.excludePackagesOrClassNames = excludePackagesOrClassNames;
        this.staticSkip = staticSkip;

        if (StringUtils.isBlank(udbAppId) || StringUtils.isBlank(udbAppKey)) {
            this.udbAppId = UdbConstants.DEFAULT_UDB_APPID;
            this.udbAppKey = UdbConstants.DEFAULT_UDB_APPKEY;
        } else {
            this.udbAppId = udbAppId;
            this.udbAppKey = udbAppKey;
        }
        logger.info("初始化 UdbSecurityInterceptor 拦截器, 跳过静态资源: " + staticSkip + ", 默认拦截模式： " + defaultCheckMode + ", 排除的包名和类： " + JsonUtil.toJson(excludePackagesOrClassNames));
    }

    public PrivilegeInterceptor getPrivilegeInterceptor() {
        return privilegeInterceptor;
    }

    public void setPrivilegeInterceptor(PrivilegeInterceptor privilegeInterceptor) {
        this.privilegeInterceptor = privilegeInterceptor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean isStatic = isStaticRequest(request, response, handler);

        if (isStatic && this.staticSkip) {
            return true;
        }

        CheckMode checkMode = detectUdbCheckMode(request, response, handler, isStatic);

        if (null == checkMode || CheckMode.NONE.equals(checkMode)) {
            // 不需要做登录控制
            return true;
        }

        String username = CookieUtil.getCookie(request, "username", null);
        if (StringUtils.isBlank(username)) {
            toUdbUnloginView(request, response, handler);
            return false;
        }
        String udbOauthKey = "UdbOauth_" + checkMode + "_" + username;
        UdbOauth udbOauth = SessionUtil.get(request, udbOauthKey, UdbOauth.class);
        if (null == udbOauth || !username.equals(udbOauth.getPassport())) {
            // 需要重新计算
            udbOauth = new UdbOauth(udbAppId, udbAppKey, request, CheckMode.STRONG.equals(checkMode) ? UdbAuthLevel.STRONG : UdbAuthLevel.LOCAL);
            SessionUtil.set(request, udbOauthKey, udbOauth);
        }

        if (null != udbOauth && udbOauth.isLogin()) {
            // 检查权限
            if (privilegeInterceptor != null) {
                if (!privilegeInterceptor.checkPrivilege(username, request, response, handler)) {
                    return false;
                }
            }
            return true;
        } else {
            toUdbUnloginView(request, response, handler);
            return false;
        }
    }

    private CheckMode detectUdbCheckMode(HttpServletRequest request, HttpServletResponse response, Object handler, boolean isStatic) {

        CheckMode checkMode;
        if (isStatic) {
            checkMode = defaultCheckMode;
        } else {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class<?> beanType = handlerMethod.getBeanType();

            if (!isRequestNeedUdbCheck(request, beanType, handlerMethod)) {
                return null;
            }

            checkMode = getUdbCheckMode(request, beanType, handlerMethod);
        }

        return checkMode;
    }

    /**
     * 是否是静态资源请求
     */
    private boolean isStaticRequest(HttpServletRequest request, HttpServletResponse response, Object handler) {

        return handler == null || !(handler instanceof HandlerMethod);
    }

    private boolean isRequestNeedUdbCheck(HttpServletRequest request, Class<?> beanType, HandlerMethod handlerMethod) {

        // 不带 @Controller 注解的不拦截
        Controller controller = beanType.getAnnotation(Controller.class);
        if (null == controller) {
            return false;
        }

        // spring 开头的不拦截
        if (null != this.excludePackagesOrClassNames) {
            String beanName = beanType.getName();
            for (String excludePackageOrClassName : this.excludePackagesOrClassNames) {
                if (beanName.startsWith(excludePackageOrClassName)) {
                    return false;
                }
            }
        }

        return true;
    }

    private CheckMode getUdbCheckMode(HttpServletRequest request, Class<?> beanType, HandlerMethod handlerMethod) {

        if (null != handlerMethod.getMethodAnnotation(IgnoredUdbCheck.class)) {
            return CheckMode.NONE;
        }

        UdbCheck udbCheck = handlerMethod.getMethodAnnotation(UdbCheck.class);
        if (null == udbCheck) {
            udbCheck = beanType.getAnnotation(UdbCheck.class);
        }
        if (udbCheck == null) {
            if (null != beanType.getAnnotation(IgnoredUdbCheck.class)) {
                return CheckMode.NONE;
            }
            return this.defaultCheckMode;
        }

        CheckMode checkMode = udbCheck.value();
        if (CheckMode.NONE.equals(checkMode)) {
            return null;
        }
        if (CheckMode.DEFAULT.equals(checkMode)) {
            return this.defaultCheckMode;
        }
        return checkMode;
    }

    /**
     * 跳转到 YY 登录页面
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理方法
     */
    private void toUdbUnloginView(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!needForwardToUdbLoginUI(request, response, handler)) {
            throw new UdbUnLoginException();
        }

        forwardToUdbLoginUI(request, response);
    }

    private boolean needForwardToUdbLoginUI(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class<?> returnType = handlerMethod.getMethod().getReturnType();
            if (isCustomView(returnType)) {
                return false;
            }

            ResponseBody responseBodyAnn = handlerMethod.getMethod().getAnnotation(ResponseBody.class);
            if (null != responseBodyAnn) {
                return false;
            }

            RestController restControllerAnn = handlerMethod.getBeanType().getAnnotation(RestController.class);
            if (null != restControllerAnn) {
                return false;
            }

        }

        return true;

    }

    private static Class<?>[] customViewTypes = new Class[]{AjaxView.class, JsonView.class, TextView.class};

    private boolean isCustomView(Class<?> returnType) {

        if (returnType == null) {
            return false;
        }

        for (Class<?> viewType : customViewTypes) {
            if (viewType.equals(returnType) || returnType.isAssignableFrom(viewType)) {
                return true;
            }
        }

        return false;
    }

    private void forwardToUdbLoginUI(HttpServletRequest request, HttpServletResponse response) {

        String domain = getDomain(request);
        String url = domain + request.getRequestURI();
        if (StringUtils.isNotEmpty(request.getQueryString())) {
            url += "?" + UrlUtil.encodeUrl(request.getQueryString());
        }

        String contextPath = request.getContextPath();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            PrintWriter out = response.getWriter();
            out.append(UdbLoginBox.getLoginBoxHtml(url, domain, contextPath));
            out.flush();
            out.close();
        } catch (Exception e) {
            throw new CodeException(e.getMessage(), e);
        }
    }

    /**
     * 获取域名.
     *
     * @param request
     * @return
     */
    public static String getDomain(HttpServletRequest request) {
        String serverName = getServerName(request);
        return "//" + serverName;
    }

    /**
     * 获取主机名.
     */
    public static String getServerName(HttpServletRequest request) {
        String serverName = request.getHeader("host");
        if (StringUtils.isEmpty(serverName)) {
            serverName = request.getServerName();
        }
        return serverName;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
