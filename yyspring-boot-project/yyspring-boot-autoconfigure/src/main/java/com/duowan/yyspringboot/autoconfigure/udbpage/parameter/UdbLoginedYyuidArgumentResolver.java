package com.duowan.yyspringboot.autoconfigure.udbpage.parameter;

import com.duowan.common.utils.ConvertUtil;
import com.duowan.common.web.ParamLookupScope;
import com.duowan.common.web.argresolvers.AbstractArgumentResolver;
import com.duowan.udb.sdk.AuthAttrLookupScope;
import com.duowan.udb.sdk.UdbAuthLevel;
import com.duowan.udb.sdk.UdbConstants;
import com.duowan.udb.sdk.UdbOauth;
import com.duowan.yyspringboot.autoconfigure.udbpage.annotations.UdbLoginCheck;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 20:42
 */
public class UdbLoginedYyuidArgumentResolver extends AbstractArgumentResolver {

    private final String udbAppId;
    private final String udbAppKey;

    public UdbLoginedYyuidArgumentResolver(String udbAppId, String udbAppKey) {
        if (StringUtils.isBlank(udbAppId) || StringUtils.isBlank(udbAppKey)) {
            this.udbAppId = UdbConstants.DEFAULT_UDB_APPID;
            this.udbAppKey = UdbConstants.DEFAULT_UDB_APPKEY;
        } else {
            this.udbAppId = udbAppId;
            this.udbAppKey = udbAppKey;
        }
    }

    @Override
    protected Object resolveArgumentByAutoScope(ParamLookupScope[] lookupScopes, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, HttpServletRequest nativeRequest, WebDataBinderFactory binderFactory) {
        String parameterName = parameter.getParameterName();
        UdbAuthLevel authLevel = null;
        AuthAttrLookupScope[] attrLookupScopes = null;
        if ("strongYyuid".equals(parameterName)) {
            authLevel = UdbAuthLevel.STRONG;
        } else if ("weakYyuid".equals(parameterName)) {
            authLevel = UdbAuthLevel.LOCAL;
        } else {
            UdbLoginCheck udbLoginCheck = parameter.getMethodAnnotation(UdbLoginCheck.class);
            if (null != udbLoginCheck) {
                authLevel = udbLoginCheck.value();
                attrLookupScopes = udbLoginCheck.scopes();
            }
        }
        if (authLevel == null) {
            authLevel = UdbAuthLevel.LOCAL;
        }

        UdbOauth udbOauth = new UdbOauth(attrLookupScopes, udbAppId, udbAppKey, nativeRequest, authLevel);

        return ConvertUtil.toLong(udbOauth.getYyuid(), -1L);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        String parameterName = parameter.getParameterName();
        return "strongYyuid".equals(parameterName) || "weakYyuid".equals(parameterName) || "loginYyuid".equals(parameterName);
    }
}