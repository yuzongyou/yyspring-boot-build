package com.duowan.common.web.argresolvers;

import com.duowan.common.web.ParamLookupScope;
import com.duowan.common.web.annotations.Param;
import com.duowan.common.web.util.ParamScopeUtil;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 18:09
 */
public abstract class AbstractArgumentResolver implements HandlerMethodArgumentResolver, Ordered {

    private ParamLookupScope[] defaultParamLookupScopes;

    public AbstractArgumentResolver() {
        this.defaultParamLookupScopes = getDefaultParamLookupScopes();
    }

    @Override
    public final Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        ParamLookupScope[] scopes = defaultParamLookupScopes;

        Param param = parameter.getParameterAnnotation(Param.class);
        if (null != param) {
            scopes = param.value();
        }

        if (null == scopes) {
            return null;
        }

        for (ParamLookupScope scope : scopes) {
            Object value;

            if (!ParamLookupScope.AUTO.equals(scope)) {
                value = ParamScopeUtil.resolveArgument(scope, parameter, mavContainer, webRequest, webRequest.getNativeRequest(HttpServletRequest.class), binderFactory);
            } else {
                value = resolveArgumentByAutoScope(scopes, parameter.getParameterName(), parameter, mavContainer, webRequest, webRequest.getNativeRequest(HttpServletRequest.class), binderFactory);
            }

            if (null != value) {
                return value;
            }
        }

        return null;
    }

    protected ParamLookupScope[] getDefaultParamLookupScopes() {
        return new ParamLookupScope[]{ParamLookupScope.AUTO};
    }

    protected abstract Object resolveArgumentByAutoScope(ParamLookupScope[] lookupScopes, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, HttpServletRequest nativeRequest, WebDataBinderFactory binderFactory);

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
