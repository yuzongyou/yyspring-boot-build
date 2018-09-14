package com.duowan.common.web.pageparameter;

import javax.servlet.http.HttpServletRequest;

/**
 * 页面参数过滤器，即判断某个请求是否禁用默认的页面参数
 *
 * @author Arvin
 */
public interface PageParameterFilter {

    /**
     * 是否支持当前请求的过滤
     *
     * @param request 当前请求
     * @return true 则过滤，false 则不过滤
     */
    boolean support(HttpServletRequest request);

    /**
     * 是否过滤 PageParameter， 如果返回 true 则过滤该 PageParameter
     *
     * @param request 当前请求
     * @param name    当前名称
     * @return true - 过滤， false - 使用框架的PageParameter
     */
    boolean filter(HttpServletRequest request, String name);
}
