package com.duowan.yyspringboot.autoconfigure.udbpage.parameter;

import com.duowan.common.utils.ConvertUtil;
import com.duowan.common.web.ParamLookupScope;
import com.duowan.common.web.argresolvers.AbstractArgumentResolver;
import com.duowan.common.web.util.ParamScopeUtil;
import com.duowan.udb.sdk.AESHelper;
import com.duowan.udb.sdk.UdbClient;
import com.duowan.udb.sdk.UdbConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * UDB Cookie 中的 parnerInfo 解密后的数据
 * QQ登录： "##UID_92A99368530D91C46CC59BFE004E2BD3##Arvin##"
 * 微信扫码登录：
 * 微信小程序登录： sessionKey##openid##nickname
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 19:42
 */
public class DecryptPartnerInfoArgumentResolver extends AbstractArgumentResolver {

    private final String udbAppId;

    private final ParamLookupScope[] partnerInfoLookupScopes;

    public DecryptPartnerInfoArgumentResolver(String udbAppId, ParamLookupScope[] partnerInfoLookupScopes) {
        if (StringUtils.isBlank(udbAppId)) {
            this.udbAppId = UdbConstants.DEFAULT_UDB_APPID;
        } else {
            this.udbAppId = udbAppId;
        }

        if (null == partnerInfoLookupScopes) {
            this.partnerInfoLookupScopes = new ParamLookupScope[]{ParamLookupScope.COOKIE, ParamLookupScope.HEADER, ParamLookupScope.REQUEST};
        } else {
            this.partnerInfoLookupScopes = partnerInfoLookupScopes;
        }
    }

    @Override
    protected ParamLookupScope[] getDefaultParamLookupScopes() {
        return new ParamLookupScope[]{ParamLookupScope.AUTO};
    }

    @Override
    protected Object resolveArgumentByAutoScope(ParamLookupScope[] lookupScopes, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, HttpServletRequest nativeRequest, WebDataBinderFactory binderFactory) {

        Object value = ParamScopeUtil.resolveArgument(this.partnerInfoLookupScopes, "partnerInfo", parameter, mavContainer, webRequest, nativeRequest, binderFactory);
        String encryptData = ConvertUtil.toString(value, null);

        if (StringUtils.isBlank(encryptData)) {
            value = ParamScopeUtil.resolveArgument(this.partnerInfoLookupScopes, "parnerInfo", parameter, mavContainer, webRequest, nativeRequest, binderFactory);
            encryptData = ConvertUtil.toString(value, null);
        }
        if (StringUtils.isBlank(encryptData)) {
            return null;
        }
        String aesKey = UdbClient.getAesEncryptKey(udbAppId);
        return AESHelper.decrypt(encryptData, aesKey);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return "decryptPartnerInfo".equals(parameter.getParameterName());
    }
}
