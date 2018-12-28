package com.duowan.yyspringboot.autoconfigure.udbwxmp.parameter;

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
 * 微信小程序 sessionKey 页面参数
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/8 16:32
 */
public class UdbWxmpSessionKeyArgumentResolver extends AbstractArgumentResolver {

    private final ParamLookupScope[] wxmpLookupScopes;
    private final String udbAppId;

    public UdbWxmpSessionKeyArgumentResolver(String udbAppId, ParamLookupScope[] wxmpLookupScopes) {
        if (null != wxmpLookupScopes && wxmpLookupScopes.length > 0) {
            this.wxmpLookupScopes = wxmpLookupScopes;
        } else {
            this.wxmpLookupScopes = new ParamLookupScope[]{ParamLookupScope.COOKIE, ParamLookupScope.HEADER, ParamLookupScope.ATTRIBUTE, ParamLookupScope.REQUEST};
        }

        if (StringUtil.isBlank(udbAppId)) {
            this.udbAppId = UdbConstants.DEFAULT_UDB_APPID;
        } else {
            this.udbAppId = udbAppId;
        }
    }

    @Override
    protected Object resolveArgumentByAutoScope(ParamLookupScope[] lookupScopes, String name, MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, HttpServletRequest nativeRequest, WebDataBinderFactory binderFactory) {

        Object obj = ParamScopeUtil.resolveArgument(this.wxmpLookupScopes, "wxmp", parameter, mavContainer, webRequest, nativeRequest, binderFactory);

        if (obj == null) {
            return null;
        }

        Object value = ParamScopeUtil.resolveArgument(this.wxmpLookupScopes, "partnerInfo", parameter, mavContainer, webRequest, nativeRequest, binderFactory);
        String encryptData = ConvertUtil.toString(value, null);

        if (StringUtil.isBlank(encryptData)) {
            return null;
        }
        String aesKey = UdbClient.getAesEncryptKey(udbAppId);
        String decryptPartnerInfo = AESHelper.decrypt(encryptData, aesKey);

        if (StringUtil.isBlank(decryptPartnerInfo)) {
            return null;
        }

        // partnerInfo format : sessionKey##openid##nickname
        String[] arrays = decryptPartnerInfo.split("#+");
        return arrays.length > 0 ? arrays[0] : null;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return "udbWxmpSessionKey".equals(parameter.getParameterName());
    }
}
