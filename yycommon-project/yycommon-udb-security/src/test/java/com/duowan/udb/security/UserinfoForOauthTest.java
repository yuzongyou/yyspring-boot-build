package com.duowan.udb.security;

import com.duowan.udb.auth.QueryAPIDIServiceHelper;
import com.duowan.udb.auth.UserinfoForOauth;
import com.duowan.udb.sdk.UdbConstants;
import com.duowan.universal.login.client.YYSecCenterOpenWSInvoker;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * // isDiscardAESCookie 0 or 1
 * // http://lgn.yy.com/message/isDiscardAESCookie.do?appid=5182
 *
 * @author Arvin
 */
public class UserinfoForOauthTest {

    private String appid = UdbConstants.DEFAULT_UDB_APPID;
    private String appkey = UdbConstants.DEFAULT_UDB_APPKEY;


    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

    /**
     * 使用 Http request 进行验证，其实是 cookie 验证，验证的逻辑为：
     */
    @Test
    public void testVerifyByHttp() {
        String udboar = "692942316F48438A2F86C76CFA3F30225475B9D2B4BEB778F6E5DCB8410E7349F8930B7821C39B2968B9669BA2FC8D4801E7E19F0BDFFEF8F4BFDB36C571196D16BF88ADF03BC350A0EC9F944CFC36CC654B1E095339228AA0E13AC9132C94BEF6F593A548509B689A436890A4314F36A7A72E8ED549DE4F2BE924F827EEF47C39E62528AB1ABF0E97F67C6A8E315584217EF698FE92843B83324D161B28CC01EE01267887C6AFDF963EC6FE7023701B3B30967CAC3C593CC3586F898DD88EBB8768C2D9576AAF8DEBE46C42902326FEF752658CC9F7C88226DF6E3804DAF2C6EE8AB8E59D1EB86E57E6F9624254D26A387DFE875AD623C194681ED09FA27C9E5737C31B57BE7A74FCC489023F2169ED420CD007D55DF080288C1CFACF1553187CBD532D7C2B0FE3E186107C181F91455A98442F4A15C0C81CB8BFC16D2C136C";

        String oauthCookie = "BE09B16FF5A1B5A133D398A01705AD22D43827EE7418545ECBC8F53F1CEC4E8DCC73FB238134F7ECA1B4171A65A48FEB2E9B5B5512EE8A1F4D84C8332233266015C0428D5A5D65D03FB5945C67428CAA4D41743CD88A9DE7A6F352DB01FE159204A7CF9C5D83A832172F385BC813A034399F0DAA9536F12EC2A3B6915B4D4F60E29D72F7AFD4812DA940D75D942E2E9FE6D723C6FABBFB2DC60587414855E1A915F95A8837AB3241D5F0D00372D87D3BB02182EA18E2100C8F2588939FB963B343E6F46B718B7B3530D977F84AB9C234193CC5460F1D9BA2F18389D16B54551F6CD57B01D76904DB0DB135737E0E2D92099548C94B0878F79D3C746C0802A7DB00757546FF5DE33F21161E16E63D88A6";

        Mockito.when(request.getCookies()).thenReturn(new Cookie[]{
                new Cookie("oauthCookie", oauthCookie),
                new Cookie("udb_oar", udboar),
                new Cookie("username", "dw_xiajiqiu1")
        });

        UserinfoForOauth userinfoForOauth = new UserinfoForOauth(request, null, appid, appkey);
        System.out.println(userinfoForOauth.validate());

        System.out.println(userinfoForOauth.getUsername());

    }

    /**
     * 不使用 Http 相关 request 检查验证
     */
    @Test
    public void testWithoutHttp() {

        String passport1 = "game_084bpuvyf0";
        String passport2 = "dw_xiajiqiu1";

        String udboar = "A96C83FEFC8A9D4798E3CBE3F6F77E9BC6F09E9A94BF4FBA8A747CA62C2B47F3AD090C2DB62FA0F928E121952A42DE20D7724A82EBF487106E74EBF9448DF510A0A801AF3203EE36A5179900216A49F98F006F7EF424BD78A20C6EFE25940B1E296C6ED0AC36728EF198725A66C136CB254D8F8CE408F7C7C05F5EC4845BF5068FCF1163F03F542199B2022B1FC463F609F3D1CBF2CF6053E8A086E23D39533ED3A900C73763EB17F7E514FF56673DD1CFA68A4CB40CD6D94890C6F8A676DFE863F20E1EE1EA6EFDA65A1D7A5AC408079966B8591DFA47EBD6DD48172A5E3FF39804B7964C430BCFA957503AB7072C8197931B2796A52E691EB9A9D956AB3B517BC85EDFC40CCA2D4A2E13AEEA029BCB25C93A35F2188856C3306BA9C714B214C5525FDE71196BD184D3815D6AD193A078D3F169A59FD865472CB637CC638FC3";
        String oauthCookie = "BE09B16FF5A1B5A133D398A01705AD22D43827EE7418545ECBC8F53F1CEC4E8DCC73FB238134F7ECA1B4171A65A48FEB2E9B5B5512EE8A1F4D84C8332233266015C0428D5A5D65D03FB5945C67428CAA4D41743CD88A9DE7A6F352DB01FE159204A7CF9C5D83A832172F385BC813A034399F0DAA9536F12EC2A3B6915B4D4F60E29D72F7AFD4812DA940D75D942E2E9FE6D723C6FABBFB2DC60587414855E1A915F95A8837AB3241D5F0D00372D87D3BB02182EA18E2100C8F2588939FB963B343E6F46B718B7B3530D977F84AB9C234193CC5460F1D9BA2F18389D16B54551F6CD57B01D76904DB0DB135737E0E2D92099548C94B0878F79D3C746C0802A7DB00757546FF5DE33F21161E16E63D88A6";

        UserinfoForOauth userinfoForOauth = new UserinfoForOauth(passport2, -1, null, udboar, appid, appkey);

        System.out.println(userinfoForOauth.validate());
        System.out.println(userinfoForOauth.getAccesstoken());

        userinfoForOauth = new UserinfoForOauth(passport2, -1, udboar, udboar, appid, appkey);

        System.out.println(userinfoForOauth.validate());
        System.out.println(userinfoForOauth.getAccesstoken());


    }

    /**
     * UDB 的 AccessToken 强验证， 先进行本地验证后才会进行强验证
     * <p/>
     * 如果你在多个浏览器登录了同一个帐号，每个浏览器的本地验证都是通过的，但是只有最后一次登录的强验证才会通过
     */
    @Test
    public void testAccessTokenVerify() {
        String passport = "dw_xiajiqiu1";

        String udboar = "08A17384942AA031E9871044AD38D9550B17DC162FE7112D2D53A3F76C013DD458BAED620154D809D2C17008AA30AD528AF542EFF834021C275E0FE2CDA047FC1374D511F63108DC4C50D12CA4C98AE0C6EE1BDE40548B68D0B51357AEA6C6DD0E4FA249A2098471AD77AB8785BA956B7C3B16950D500CA618D9B6E6B4CECA4694ED35D1A39D363F4B9B7F48A7320E4D87037F31A87285338343D6124453E23091A11A4C5FD75A1221682FD970DC7453644307E2A3BE5AFE5BA9B263632AF661A22C5C4E28172CA97C326996BB7502E9978805112BBD5939358BFEB313C4FE84757291B2E291C0EC9BFE0896CBF196C4416C70831B96F35D7689DDC87345288C43A38EBD184A774462187369ECFD4FB458C0DA652558627FDE4984BB33D32041DA3EAAD10E48A4A2EB72389899E63CF0BD6488030A447D8DAE5290A1D13B8068A1B7A1089D8E686899EA99697FA2C4DE755949C9EF3DCF3A275B8942B0899787FC7250B9B01FBAC2FC0EAFB06E7E34A19891C172A6DC3268DE0F8393FBC40F7B";

        //String udboar = "5BB4DCECEF415C1BD434EECBBABCD288A6A8277DED6FC3A9CA3BD70E17FE704FFA915669FD46701689D41C2012A57DBA70CE4CA60819B2FBAC1C588FD8FE466D83B8E8F2C249392F0729E5AC433E4E3F6A6C44AA0C34C99989246664B35FBC196725EC2FF2CAF605F06443F67E158A55B34EDD3210F9FCEB196468FEAB8F90BA0A576C4788E713E87A9E21BDE4613608D8DD7591F03EA7467F6F13A28B648CCC763E44CE53918647124D1D1896125DB150E12B46084AA4E6B9DEBF251462CA8E278BA6348816FD864A1D47049564E6CDFC3AA300B55DC93069640849295860FE0275B79290D2159FFF5DA9608F4EC3E3D671697D36E91F7AEFEC3FB458B1CA4F3183C455C5758977038A51C4D693A8C38FE09226360EB67718E79A09CADEC9F36EAF290103E71F7F4CF15F45D85799FC2BB1A2C9E3F50F9C76D228D7FECD355EA1B7A1089D8E686899EA99697FA2C4DE755949C9EF3DCF3A275B8942B0899787FC7250B9B01FBAC2FC0EAFB06E7E34A19891C172A6DC3268DE0F8393FBC40F7B";

        UserinfoForOauth userinfoForOauth = new UserinfoForOauth(passport, -1, udboar, udboar, appid, appkey);

        System.out.println(userinfoForOauth.validate());
        System.out.println(userinfoForOauth.getAccesstoken());

        String accessToken = userinfoForOauth.getAccesstoken();
        String tokenSecret = userinfoForOauth.getTokensecret();
        String yyuid = userinfoForOauth.getYyuid();

        System.out.println("AccessToken: " + accessToken);
        System.out.println("TokenSecret: " + tokenSecret);

        boolean isValid = YYSecCenterOpenWSInvoker.validAccessTokenByYyuid(appid, appkey, accessToken, tokenSecret, yyuid);

        System.out.println(isValid);

    }

    @Test
    public void testForceCheckAccesstokenInfo() {
        String[] array = QueryAPIDIServiceHelper.getForceCheckAccesstokenInfo("5075");

        if (array != null && array.length > 0) {
            for (String item : array) {
                System.out.println(item + " ");
            }
        } else {
            System.out.println("结果为空！");
        }
    }

}
