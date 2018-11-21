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

    private String passport = "qq_7z2icq5ucu54";
    private long yyuid = 2189254541L;
    private String oauthCookieOrUdbOar = "28B335BF71385CA88ED39DF22DF7F735BDFE40B3F1B6097C972E419ABF1EB604E8816E38B4E93F7844B8DD2F2739C63DF380E83351C3F7095D5174D5147B833722ECCB7A9C00F78A30E4EE93940316CD05E65C231B085256E4B2AE71899D4418FD8399629E31FE59F5C93FDBD2A45E48EF501496722384D792B45F4D16A027EB63E24A1F47F9FE39936A52471424BE01F590B38DF2D358C95FAFACB6D22351F90966D03C862F20CAEF9EB0C7153D8AEFECFFFB07F304BD368B4265B4477842DE271D023A0E713321B10B278E4C9CA995B010158D973E06BDDE8A35B3657D0A9E3E3F6A1C8C504F891C5EEE166878A6783F143D4A6AD2ED480B65167D359D88285FB44A9B346A59D5BD0559871F465A31AD497AF48138FF97C2188396A6104D59FFAF963A2430163E5141FCA5D658B87724C5A845A3BB2F94213DAAEEE5003969";

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