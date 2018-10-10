package com.duowan.common.web.argresolvers;

import com.duowan.common.web.ParamLookupScope;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 18:44
 */
public class RequestUriArgumentResolver extends AbstractArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return "requestUri".equals(parameter.getParameterName());
    }

    @Override
    protected Object resolveArgumentByAutoScope(ParamLookupScope[] lookupScopes, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, HttpServletRequest nativeRequest, WebDataBinderFactory binderFactory) {
        return nativeRequest.getRequestURI();
    }
}
