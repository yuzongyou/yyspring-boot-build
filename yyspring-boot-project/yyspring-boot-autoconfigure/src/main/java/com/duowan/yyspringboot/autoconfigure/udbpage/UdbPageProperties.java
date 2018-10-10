package com.duowan.yyspringboot.autoconfigure.udbpage;

import com.duowan.common.web.ParamLookupScope;
import com.duowan.udb.sdk.UdbConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/9 17:41
 */
@ConfigurationProperties(prefix = "yyspring.udbpage")
public class UdbPageProperties {

    /** UDB APPID **/
    private String udbAppId = UdbConstants.DEFAULT_UDB_APPID;

    /** UDB APPKEY **/
    private String udbAppKey = UdbConstants.DEFAULT_UDB_APPKEY;

    /** UDB partnerInfo 属性搜索作用域 **/
    private ParamLookupScope[] partnerInfoLookupScopes = new ParamLookupScope[]{ParamLookupScope.COOKIE, ParamLookupScope.HEADER, ParamLookupScope.REQUEST};

    /** UDB thirdCookie 属性搜索作用域 **/
    private ParamLookupScope[] thirdCookieLookupScopes = new ParamLookupScope[]{ParamLookupScope.COOKIE, ParamLookupScope.HEADER, ParamLookupScope.REQUEST};

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

    public ParamLookupScope[] getPartnerInfoLookupScopes() {
        return partnerInfoLookupScopes;
    }

    public void setPartnerInfoLookupScopes(ParamLookupScope[] partnerInfoLookupScopes) {
        this.partnerInfoLookupScopes = partnerInfoLookupScopes;
    }

    public ParamLookupScope[] getThirdCookieLookupScopes() {
        return thirdCookieLookupScopes;
    }

    public void setThirdCookieLookupScopes(ParamLookupScope[] thirdCookieLookupScopes) {
        this.thirdCookieLookupScopes = thirdCookieLookupScopes;
    }
}
