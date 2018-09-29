package com.duowan.yyspringboot.autoconfigure.udbpage.parameter;

import com.duowan.common.web.pageparameter.AbstractPageParameter;
import com.duowan.udb.sdk.UdbOauth;
import com.duowan.udb.sdk.UdbContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @author dw_xiajiqiu1
 */
@Component
public class StrongPassportPageParameter extends AbstractPageParameter {

    @Override
    public String getName() {
        return "strongPassport";
    }

    @Override
    protected String customGetValue(HttpServletRequest request) {
        UdbOauth oauth = UdbContext.getStrongOauth(request);
        return null == oauth || !oauth.isLogin() ? null : oauth.getPassport();
    }
}
