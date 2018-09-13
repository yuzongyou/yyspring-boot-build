package com.duowan.yyspringboot.autoconfigure.udb.page;

import com.duowan.udb.security.UdbContext;
import com.duowan.udb.security.UdbOauth;
import com.duowan.yyspringboot.autoconfigure.web.pageparameter.AbstractPageParameter;
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
