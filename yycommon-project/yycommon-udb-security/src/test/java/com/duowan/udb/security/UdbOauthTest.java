package com.duowan.udb.security;

import com.duowan.udb.sdk.UdbAuthLevel;
import com.duowan.udb.sdk.UdbOauth;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * PS： 测试之前，请先去进行YY登录，获得对应的 passport 和 yyuid 参数，以及 oauthCookie 或 udb_oar cookie
 * <p/>
 * 所有测试参数都可以在 cookie 中获取到
 *
 * @author Arvin
 */
public class UdbOauthTest {

    private String appid = "5182";
    private String appkey = "4KPxcdNkKAjCC3YqARzgBIEnSuWMGZFf";

    private String passport = "dw_xiajiqiu1";
    private long yyuid = 50017681L;
    private String oauthCookieOrUdbOar = "4A055309C4F3A433EE0CA83C53125EF1DE3F4B2945A991DF26071679D4B8FE279BE36CFF2C60C5B6836190ECB6E79B004D193D4704FAFBF668A150A150BAC11C7FFB152DC7CC503EC94C9F322908D327633CDB2ECEBEFB5091A7B7A5B2FBC2C391135A30F80401AAEE3C47AD6297C212C22A455956133FBF3809C616DB86DB33269DC5014658E2DBF0AF00C4B25961E3F01D511BDAAEE322C6084015F81459687A8F95F4F4B18B6FA592754F714E2D91570C83D3F28FE5C261213045C41F80D0AFC51985C15D811F823FDE71D72D80E2917F8F16A7FD7EB983B1EAB59CD8CD74385FE9F91AAD29FCF2162431A4B030F9B488A179FE64387867DE3FAD806116F69D6FBCCBBE71271A14FDA5D611540A5CFE76C587DEBB82C0F9DF12DF7E710E90C812A9F555364225CE452661B675720B5E229D6335AE63EB8EA5BD66A2406638A1B7A1089D8E686899EA99697FA2C4DE755949C9EF3DCF3A275B8942B0899787FC7250B9B01FBAC2FC0EAFB06E7E34A19891C172A6DC3268DE0F8393FBC40F7B";

    private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

    @Before
    public void prepare() {

        Mockito.when(request.getCookies()).thenReturn(new Cookie[]{
                new Cookie("oauthCookie", oauthCookieOrUdbOar),
                new Cookie("udb_oar", oauthCookieOrUdbOar),
                new Cookie("username", passport),
                new Cookie("yyuid", String.valueOf(yyuid))
        });

    }

    /**
     * 测试构造函数 1
     */
    @Test
    public void testConstructor1() {

        UdbOauth udbOauth = new UdbOauth(appid, appkey, passport, oauthCookieOrUdbOar, UdbAuthLevel.LOCAL);

        assertEquals(udbOauth.getPassport(), passport);
        assertEquals(udbOauth.getYyuid().longValue(), yyuid);

        assertTrue(udbOauth.isLogin());
        assertTrue(udbOauth.isWeakLogin());
        assertTrue(udbOauth.isStrongLogin());
    }

    /**
     * 测试构造函数 2
     */
    @Test
    public void testConstructor2() {

        UdbOauth udbOauth = new UdbOauth(appid, appkey, yyuid, oauthCookieOrUdbOar, UdbAuthLevel.LOCAL);

        assertEquals(udbOauth.getPassport(), passport);
        assertEquals(udbOauth.getYyuid().longValue(), yyuid);

        assertTrue(udbOauth.isLogin());
        assertTrue(udbOauth.isWeakLogin());
        assertTrue(udbOauth.isStrongLogin());
    }

    /**
     * 测试构造函数 3
     */
    @Test
    public void testConstructor3() {

        UdbOauth udbOauth = new UdbOauth(null, appid, appkey, request, UdbAuthLevel.LOCAL);

        assertEquals(udbOauth.getPassport(), passport);
        assertEquals(udbOauth.getYyuid().longValue(), yyuid);

        assertTrue(udbOauth.isLogin());
        assertTrue(udbOauth.isWeakLogin());
        assertTrue(udbOauth.isStrongLogin());
    }

}