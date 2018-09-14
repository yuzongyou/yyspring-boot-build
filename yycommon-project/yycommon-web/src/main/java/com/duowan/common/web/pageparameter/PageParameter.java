package com.duowan.common.web.pageparameter;

import javax.servlet.http.HttpServletRequest;

/**
 * Request 页面参数定义
 *
 * @author Arvin
 */
public interface PageParameter {

    /**
     * 获取参数名称
     *
     * @return 返回request参数名称
     */
    String getName();

    /**
     * 获取参数值
     *
     * @param request 当前请求
     * @return 返回指定参数的值
     */
    String getValue(HttpServletRequest request);
}
