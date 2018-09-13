package com.duowan.common.admincenter.service;

import com.duowan.common.admincenter.model.Privilege;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 8:09
 */
public interface AdmincenterService {

    /**
     * 是否有权限
     *
     * @param username 用户通行证
     * @param request  当前请求
     * @param response 响应对象
     * @param handler  处理方法对象
     * @return 返回是否有权限
     */
    boolean hasPrivilege(String username, HttpServletRequest request, HttpServletResponse response, Object handler);

    /**
     * 获取当前登录用户的权限
     *
     * @param username 用户通行证
     * @param request  当前请求
     * @return 返回用户权限
     */
    List<Privilege> getPrivileges(String username, HttpServletRequest request);
}
