package com.duowan.common.web.util;

import com.duowan.common.web.ParamLookupScope;
import com.duowan.common.web.argresolvers.*;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 19:25
 */
public class ParamScopeUtil {

    private ParamScopeUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static Map<ParamLookupScope, ParamScopeResolver> resolverMap = new EnumMap<>(ParamLookupScope.class);

    static {
        resolverMap.put(ParamLookupScope.REQUEST, new RequestScopeResolver());
        resolverMap.put(ParamLookupScope.ATTRIBUTE, new AttributeScopeResolver());
        resolverMap.put(ParamLookupScope.HEADER, new HeaderScopeResolver());
        resolverMap.put(ParamLookupScope.COOKIE, new CookieScopeResolver());
    }

    public static Object resolveArgument(ParamLookupScope scope, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, HttpServletRequest request, WebDataBinderFactory binderFactory) {

        ParamScopeResolver resolver = resolverMap.get(scope);

        if (null == resolver) {
            return null;
        }

        return resolver.resolveArgument(parameter.getParameterName(), parameter, mavContainer, webRequest, request, binderFactory);
    }

    public static Object resolveArgument(ParamLookupScope[] scopes, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, HttpServletRequest request, WebDataBinderFactory binderFactory) {

        if (null == scopes) {
            return null;
        }

        for (ParamLookupScope scope : scopes) {

            ParamScopeResolver resolver = resolverMap.get(scope);

            if (null == resolver) {
                return null;
            }

            Object value = resolver.resolveArgument(name, parameter, mavContainer, webRequest, request, binderFactory);
            if (null != value) {
                return value;
            }
        }
        return null;
    }
}
