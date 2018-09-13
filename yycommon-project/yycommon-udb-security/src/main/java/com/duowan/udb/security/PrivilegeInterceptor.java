package com.duowan.udb.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截接口定义
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 14:21
 */
public interface PrivilegeInterceptor {

    /**
     * 是否有权限
     *
     * @param username 用户通行证
     * @param request  当前请求
     * @param response 响应对象
     * @param handler  处理方法对象
     * @return 返回是否有权限
     */
    boolean checkPrivilege(String username, HttpServletRequest request, HttpServletResponse response, Object handler);
}
