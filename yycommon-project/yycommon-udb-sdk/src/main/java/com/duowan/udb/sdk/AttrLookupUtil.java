package com.duowan.udb.sdk;

import com.duowan.common.utils.ConvertUtil;
import com.duowan.common.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 20:52
 */
public class AttrLookupUtil {

    public static String lookupAttr(AttrLookupScope[] lookupScopes, HttpServletRequest request, String name) {

        if (null == lookupScopes || lookupScopes.length < 1) {
            return null;
        }

        for (AttrLookupScope lookupScope : lookupScopes) {
            String value = lookupAttr(lookupScope, request, name);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }

        return null;
    }

    public static String lookupAttr(AttrLookupScope lookupScope, HttpServletRequest request, String name) {
        if (null == lookupScope) {
            return null;
        }
        switch (lookupScope) {

            case REQUEST:
                return request.getParameter(name);
            case ATTRIBUTE:
                return ConvertUtil.toString(request.getAttribute(name));
            case HEADER:
                return request.getHeader(name);
            case COOKIE:
                return CookieUtil.getCookie(request, name);
            default:
                break;
        }
        return null;
    }
}
