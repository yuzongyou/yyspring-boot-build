package com.duowan.yyspringboot.autoconfigure.udbpage.parameter;

import com.duowan.common.utils.CookieUtil;
import com.duowan.common.web.pageparameter.AbstractPageParameter;
import com.duowan.udb.sdk.AESHelper;
import com.duowan.udb.sdk.UdbClient;
import com.duowan.udb.sdk.UdbContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * UDB Cookie 中的 thirdCookie 解密后的数据
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 19:42
 */
@Component
public class DecryptOauthCookiePageParameter extends AbstractPageParameter {

    @Override
    protected String customGetValue(HttpServletRequest request) {

        String encryptData = CookieUtil.getCookie(request, "thirdCookie");

        if (encryptData == null) {
            encryptData = commonGetValue(request, "thirdCookie");
        }

        if (StringUtils.isBlank(encryptData)) {
            return null;
        }

        String aesKey = UdbClient.getAesEncryptKey(UdbContext.getAppid());
        return AESHelper.decrypt(encryptData, aesKey);
    }


    @Override
    public String getName() {
        return "decryptOauthCookie";
    }
}
