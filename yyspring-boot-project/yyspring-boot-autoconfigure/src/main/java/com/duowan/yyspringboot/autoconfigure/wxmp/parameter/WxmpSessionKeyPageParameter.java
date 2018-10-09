package com.duowan.yyspringboot.autoconfigure.wxmp.parameter;

import com.duowan.common.utils.CookieUtil;
import com.duowan.common.web.pageparameter.AbstractPageParameter;
import com.duowan.yyspringboot.autoconfigure.udbpage.parameter.DecryptPartnerInfoPageParameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信小程序 sessionKey 页面参数
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/8 16:32
 */
@Component
public class WxmpSessionKeyPageParameter extends AbstractPageParameter {

    @Autowired
    private DecryptPartnerInfoPageParameter decryptPartnerInfoPageParameter;

    @Override
    protected String customGetValue(HttpServletRequest request) {

        String wxmpKey = "wxmp";
        if (StringUtils.isBlank(commonGetValue(request, wxmpKey)) && StringUtils.isBlank(CookieUtil.getCookie(request, wxmpKey))) {
            return null;
        }

        String decryptPartnerInfo = decryptPartnerInfoPageParameter.getValue(request);

        if (StringUtils.isBlank(decryptPartnerInfo)) {
            return null;
        }

        // partnerInfo format : sessionKey##openid##nickname
        String[] arrays = decryptPartnerInfo.split("#+");
        return arrays.length > 0 ? arrays[0] : null;
    }

    @Override
    public String getName() {
        return "wxmpSessionKey";
    }
}
