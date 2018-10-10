package com.duowan.common.web.argresolvers;

import com.duowan.common.utils.ConvertUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 19:23
 */
public class HeaderScopeResolver implements ParamScopeResolver {

    @Override
    public Object resolveArgument(String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, HttpServletRequest request, WebDataBinderFactory binderFactory) {
        return request.getHeader(ConvertUtil.toString(name, parameter.getParameterName()));
    }
}
