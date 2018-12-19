package com.duowan.common.admincenter.service.impl;

import com.duowan.common.admincenter.annotations.NoPrivilege;
import com.duowan.common.admincenter.context.PrivilegeContext;
import com.duowan.common.admincenter.context.UserPrivilegeContext;
import com.duowan.common.admincenter.dao.AdmincenterDao;
import com.duowan.common.admincenter.exception.NoPrivilegeException;
import com.duowan.common.admincenter.model.Privilege;
import com.duowan.common.admincenter.service.AdmincenterService;
import com.duowan.common.utils.CookieUtil;
import com.duowan.common.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 8:16
 */
public class AdmincenterInterceptorService extends HandlerInterceptorAdapter implements AdmincenterService {

    private static final Logger logger = LoggerFactory.getLogger(AdmincenterInterceptorService.class);

    private AdmincenterDao admincenterDao;

    private PrivilegeContext privilegeContext;

    public AdmincenterInterceptorService(AdmincenterDao admincenterDao, PrivilegeContext privilegeContext) {
        this.admincenterDao = admincenterDao;
        this.privilegeContext = privilegeContext;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!privilegeContext.needCheckPrivilege()) {
            return true;
        }
        String username = CookieUtil.getCookie(request, "username", null);
        if (StringUtils.isBlank(username)) {
            logger.warn("用户没有登录");
            throw new NoPrivilegeException();
        }
        if (!hasPrivilege(username, request, response, handler)) {
            logger.warn("用户[{}] 没有权限操作[{}]", username, request.getRequestURI());
            throw new NoPrivilegeException();
        }
        return true;
    }

    @Override
    public List<Privilege> getPrivileges(String username, HttpServletRequest request) {
        if (!privilegeContext.needCheckPrivilege()) {
            return privilegeContext.getAllPrivileges();
        }
        UserPrivilegeContext userPrivilegeContext = getUserPrivilegeContext(username, request);

        if (null == userPrivilegeContext) {
            return new ArrayList<>();
        }
        return userPrivilegeContext.getPrivileges();
    }

    @Override
    public boolean hasPrivilege(String username, HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!privilegeContext.needCheckPrivilege()) {
            return true;
        }

        Privilege privilege = privilegeContext.detectPrivilege(request);
        if (null == privilege) {
            return true;
        }

        if (isIgnoredPrivilege(username, request, response, handler)) {
            return true;
        }

        UserPrivilegeContext userPrivilegeContext = getUserPrivilegeContext(username, request);

        if (null == userPrivilegeContext) {
            return false;
        }

        return userPrivilegeContext.hasPrivilege(privilege);
    }

    private UserPrivilegeContext getUserPrivilegeContext(String username, HttpServletRequest request) {

        String key = "UserPrivilegeContext_" + username;

        UserPrivilegeContext context = SessionUtil.get(request, key, UserPrivilegeContext.class);

        if (null != context) {
            return context;
        }

        context = new UserPrivilegeContext();
        context.setUsername(username);

        context.setPrivileges(privilegeContext.getPrivilegesByIds(admincenterDao.getPrivilegeIds(username)));

        if (!context.getPrivileges().isEmpty()) {
            // 有权限的时候才放到 session 中去
            SessionUtil.set(request, key, context);
        }

        return context;
    }

    private boolean isIgnoredPrivilege(String username, HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (handler == null) {
            return true;
        }

        if (handler instanceof HandlerMethod) {

            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class<?> beanType = handlerMethod.getBeanType();

            if (null != beanType.getAnnotation(NoPrivilege.class)) {
                return true;
            }

            NoPrivilege noPrivilege = handlerMethod.getMethodAnnotation(NoPrivilege.class);

            if (null != noPrivilege) {
                return true;
            }

        }

        return false;
    }
}
