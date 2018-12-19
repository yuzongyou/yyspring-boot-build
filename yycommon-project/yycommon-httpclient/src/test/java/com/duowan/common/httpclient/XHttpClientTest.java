package com.duowan.common.httpclient;

import com.duowan.common.utils.EncryptUtil;
import org.junit.Test;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 15:49
 */
public class XHttpClientTest {

    @Test
    public void testXHttpClient() {
        XHttpClient client = new XHttpClient("https://usercenter.yy.com/");
        client.setLogEnabled(true);

        String getUserInfoApi = "/user/UserWebService/getCacheUserByYyid";

        PreRequestInterceptor<AbstractHcRequestContext> interceptor = new PreRequestInterceptor<AbstractHcRequestContext>() {
            @Override
            public void preHandle(AbstractHcRequestContext context) {
                context
                        .cookie("yyuid", "50017681")
                        .cookie("username", "dw_xiajiqiu1")
                        .cookie("udb_oar", "82A594840DEEFCFECFB6163B2351D3879455CBABD79B5692191BB08B82B4E7072186B2B3CB607F99EFD3570970FEE3A9328F30BB2E9B80C361DA210F2C285EF171423AB4B07E9D5FE450A2506C53526399B8F66F46134C7B56F1F812B5321A112F86E675ABDDF85707E82CDA546758F8657A5ACF21B8B4B5B9BD7E6909DD146A84EDA2F3067CFE83DA647B0DEF7FF003E6E1C32A3DF3F114F9A6457C121EEF029627F7B23C129A0FC1A593402AA2AD772C441F542B7DCF18C1F48444E94DCB654B554BF87B47E276D8627EF429DB73BC6295C613CA3C8C988B5D893720950A22390FF8FBE959F4A0C5F2EEE2A49192885654A2EC08EB6F2FC6569A3A43BA70087BC3E15C92548613F827F76CC9DB2C8F22B50A424027314328CF7DFB9C04D4345963948CFCBFFAAE7EEFC3D89835763832873B951792901D902CDA5E4B7A54CA7DEECF28AB167FB7556B88FC833CB3515B2E5B8D8226A58F132D45AF31F9FBFBEC85F24C3499F5AE153FBF4E47255F2FF1A264D6C63805D17E518C84CF4F0410")
                ;
            }
        };

        client.setPreRequestInterceptor(interceptor);

        String responseText = client.get(getUserInfoApi).responseText().asTrimText();

        System.out.println(responseText);
    }


    @Test
    public void testPassport() {

        XHttpClient client = new XHttpClient("http://passport.4366.com/");
        client.setLogEnabled(true);

        String getUidApi = "/channel/te6/getUid.do";
        String appId = "198f81f727af10fb";
        String appKey = "098f6bcd4621d373cade4e832627b4f6";
        String account = "2285334210";

        String responseText = client.get(getUidApi)
                .param("account", account)
                .param("sign", EncryptUtil.toMd5LowerCase(account + appId + appKey))
                .responseText()
                .asObjectForStdJsonResp(String.class);

        System.out.println(responseText);
    }

    @Test
    public void testGw() {
        XHttpClient client = new XHttpClient("http://dev-fcm.duowan.com/");
        client.setLogEnabled(false);

        for (int i = 0; i < 10; ++i) {
            System.out.println(client.get("lt/myip").responseText().asTrimText());
        }
    }
}