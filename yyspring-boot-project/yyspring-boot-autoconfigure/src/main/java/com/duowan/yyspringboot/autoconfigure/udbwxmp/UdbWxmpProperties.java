package com.duowan.yyspringboot.autoconfigure.udbwxmp;

import com.duowan.common.web.ParamLookupScope;
import com.duowan.udb.sdk.UdbConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 21:25
 */
@ConfigurationProperties(prefix = "yyspring.udbwxmp")
public class UdbWxmpProperties {

    /**
     * UDB APPID
     **/
    private String udbAppId = UdbConstants.DEFAULT_UDB_APPID;

    /**
     * UDB APPKEY
     **/
    private String udbAppKey = UdbConstants.DEFAULT_UDB_APPKEY;

    /**
     * 属性搜索作用域
     **/
    private ParamLookupScope[] wxmpLookupScopes = new ParamLookupScope[]{ParamLookupScope.COOKIE, ParamLookupScope.HEADER, ParamLookupScope.ATTRIBUTE, ParamLookupScope.REQUEST};

    public String getUdbAppId() {
        return udbAppId;
    }

    public void setUdbAppId(String udbAppId) {
        this.udbAppId = udbAppId;
    }

    public String getUdbAppKey() {
        return udbAppKey;
    }

    public void setUdbAppKey(String udbAppKey) {
        this.udbAppKey = udbAppKey;
    }

    public ParamLookupScope[] getWxmpLookupScopes() {
        return wxmpLookupScopes;
    }

    public void setWxmpLookupScopes(ParamLookupScope[] wxmpLookupScopes) {
        this.wxmpLookupScopes = wxmpLookupScopes;
    }
}
