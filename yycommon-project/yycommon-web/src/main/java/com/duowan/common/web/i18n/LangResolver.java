package com.duowan.common.web.i18n;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/14 16:18
 */
public interface LangResolver {

    /**
     * 获取语言
     *
     * @param request 当前请求
     * @return 返回语言
     */
    String resolveLang(HttpServletRequest request);
}
