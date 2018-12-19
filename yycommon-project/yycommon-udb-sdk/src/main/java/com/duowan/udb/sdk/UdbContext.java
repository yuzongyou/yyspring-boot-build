package com.duowan.udb.sdk;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 */
public class UdbContext {

    private UdbContext() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取Udb认证
     *
     * @param attrLookupScopes 认证属性搜索作用域
     * @param udbAppId         udbAppId
     * @param udbAppKey        UdbAppKey
     * @param request          当前请求对象
     * @param authLevel        认证级别
     * @return 返回认证对象
     */
    public static UdbOauth getUdbOauth(AttrLookupScope[] attrLookupScopes, String udbAppId, String udbAppKey, HttpServletRequest request, UdbAuthLevel authLevel) {

        if (StringUtils.isBlank(udbAppId) || StringUtils.isBlank(udbAppKey)) {
            udbAppId = UdbConstants.DEFAULT_UDB_APPID;
            udbAppKey = UdbConstants.DEFAULT_UDB_APPKEY;
        }

        String key = buildRequestKey(attrLookupScopes, udbAppId, authLevel);

        Object obj = request.getAttribute(key);

        if (obj instanceof UdbOauth) {
            return (UdbOauth) obj;
        }

        UdbOauth oauth = new UdbOauth(attrLookupScopes, udbAppId, udbAppKey, request, authLevel);
        request.setAttribute(key, oauth);

        return oauth;
    }

    private static String buildRequestKey(AttrLookupScope[] attrLookupScopes, String udbAppId, UdbAuthLevel authLevel) {
        StringBuilder builder = new StringBuilder();

        if (null != attrLookupScopes && attrLookupScopes.length > 0) {
            for (AttrLookupScope scope : attrLookupScopes) {
                builder.append(scope);
            }
            builder.append("-");
        }
        builder.append(udbAppId).append("-").append(authLevel);

        return builder.toString();

    }

}
