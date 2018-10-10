package com.duowan.common.web.argresolvers;

import com.duowan.common.utils.RequestUtil;
import com.duowan.common.web.ParamLookupScope;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 18:17
 */
public class ClientIpArgumentResolver extends AbstractArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return "clientIp".equals(parameter.getParameterName());
    }

    @Override
    protected Object resolveArgumentByAutoScope(ParamLookupScope[] lookupScopes, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, HttpServletRequest nativeRequest, WebDataBinderFactory binderFactory) {
        return RequestUtil.getClientIp(nativeRequest);
    }
}
