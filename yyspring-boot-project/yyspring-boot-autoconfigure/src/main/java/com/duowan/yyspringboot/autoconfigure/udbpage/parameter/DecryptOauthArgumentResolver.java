package com.duowan.yyspringboot.autoconfigure.udbpage.parameter;

import com.duowan.common.utils.ConvertUtil;
import com.duowan.common.utils.StringUtil;
import com.duowan.common.web.ParamLookupScope;
import com.duowan.common.web.argresolvers.AbstractArgumentResolver;
import com.duowan.common.web.util.ParamScopeUtil;
import com.duowan.udb.sdk.AESHelper;
import com.duowan.udb.sdk.UdbClient;
import com.duowan.udb.sdk.UdbConstants;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * UDB Cookie 中的 thirdCookie 解密后的数据
 * QQ登录： newqq##UID_92A99368530D91C46CC59BFE004E2BD3##Arvin##newqq_i5fhiwk_l##2180589693######Grw4
 * 微信登录： qq##o6mRE0gYA6fVKxrRQcpEmFr7NqWc##在风中##qq_7z2icq5ucu54##2189254541####2189254541##NtPv
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 19:42
 */
public class DecryptOauthArgumentResolver extends AbstractArgumentResolver {

    private final String udbAppId;

    private final ParamLookupScope[] thirdCookieLookupScopes;

    public DecryptOauthArgumentResolver(String udbAppId, ParamLookupScope[] thirdCookieLookupScopes) {
        if (StringUtil.isBlank(udbAppId)) {
            this.udbAppId = UdbConstants.DEFAULT_UDB_APPID;
        } else {
            this.udbAppId = udbAppId;
        }

        if (null == thirdCookieLookupScopes) {
            this.thirdCookieLookupScopes = new ParamLookupScope[]{ParamLookupScope.COOKIE, ParamLookupScope.HEADER, ParamLookupScope.REQUEST};
        } else {
            this.thirdCookieLookupScopes = thirdCookieLookupScopes;
        }
    }

    @Override
    protected Object resolveArgumentByAutoScope(ParamLookupScope[] lookupScopes, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, HttpServletRequest nativeRequest, WebDataBinderFactory binderFactory) {

        Object value = ParamScopeUtil.resolveArgument(this.thirdCookieLookupScopes, "thirdCookie", parameter, mavContainer, webRequest, nativeRequest, binderFactory);
        String encryptData = ConvertUtil.toString(value, null);

        if (StringUtil.isBlank(encryptData)) {
            return null;
        }

        String aesKey = UdbClient.getAesEncryptKey(udbAppId);

        return AESHelper.decrypt(encryptData, aesKey);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return "decryptOauthCookie".equals(parameter.getParameterName()) || "decryptThirdCookie".equals(parameter.getParameterName());
    }
}
