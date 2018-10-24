package com.duowan.common.utils;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/24 16:31
 */
public class UriBuilderTest {

    @Test
    public void testUriHandler() {

        String url = "http://www.baidu.com/s.do?jumpUrl=http%252F25253D2%26domainList%3D5153.com&domainList=duowan.com#hasdata";

        // param replace
        assertEquals(UriBuilder.fromHttpUrl(url).param("domainList", "yy.com").build(),
                "http://www.baidu.com/s.do?jumpUrl=http%252F25253D2%26domainList%3D5153.com&domainList=yy.com#hasdata");

        // host replace
        assertEquals(UriBuilder.fromHttpUrl(url).host("wan.yy.com").build(),
                "http://wan.yy.com/s.do?jumpUrl=http%252F25253D2%26domainList%3D5153.com&domainList=duowan.com#hasdata");

        // param remove
        assertEquals(UriBuilder.fromHttpUrl(url).removeParam("domainList").build(),
                "http://www.baidu.com/s.do?jumpUrl=http%252F25253D2%26domainList%3D5153.com#hasdata");

        // fragment remove
        assertEquals(UriBuilder.fromHttpUrl(url).fragment("").build(),
                "http://www.baidu.com/s.do?jumpUrl=http%252F25253D2%26domainList%3D5153.com&domainList=duowan.com");

        // schema
        assertEquals(UriBuilder.fromHttpUrl("//www.baidu.com/s.do?jumpUrl=http%252F25253D2%26domainList%3D5153.com&domainList=duowan.com#hasdata")
                        .fragment("").build(),
                "//www.baidu.com/s.do?jumpUrl=http%252F25253D2%26domainList%3D5153.com&domainList=duowan.com");

        // userinfo
        assertEquals(UriBuilder.fromHttpUrl("http://wan.yy.com/hello.do?name=xxx#hashdata")
                        .userinfo("user:admin").build(),
                "http://user:admin@wan.yy.com/hello.do?name=xxx#hashdata");

        // delete userinfo
        assertEquals(UriBuilder.fromHttpUrl("http://user:admin@wan.yy.com/hello.do?name=xxx#hashdata")
                        .userinfo("").build(),
                "http://wan.yy.com/hello.do?name=xxx#hashdata");

    }

    @Test
    public void testUserinfo() throws URISyntaxException {

        URI uri = new URI("http://user:admin@wan.yy.com/hello.do?name=xxx#hashdata");
        System.out.println("Authority: " + uri.getAuthority());
        System.out.println("RawUserInfo: " + uri.getRawUserInfo());
        System.out.println("Host: " + uri.getHost());
    }
}