package com.duowan.common.web.argresolvers;

import com.duowan.common.utils.RequestUtil;
import com.duowan.common.web.ParamLookupScope;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取用户请求协议， 如http , https  通过 Nginx 请求头： X-HTTPS-Protocol，X-Forwarded-Scheme $scheme
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 18:41
 */
public class ProtocolTypeArgumentResolver extends AbstractArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return "protocolType".equals(parameter.getParameterName());
    }

    @Override
    protected Object resolveArgumentByAutoScope(ParamLookupScope[] lookupScopes, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, HttpServletRequest nativeRequest, WebDataBinderFactory binderFactory) {
        return RequestUtil.getProtocolType(nativeRequest);
    }
}
