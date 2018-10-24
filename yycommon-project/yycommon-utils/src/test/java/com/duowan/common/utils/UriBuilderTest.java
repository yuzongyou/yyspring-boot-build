package com.duowan.common.utils;

import org.junit.Test;

import java.net.URISyntaxException;

import static org.junit.Assert.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/24 16:31
 */
public class UriBuilderTest {

    @Test
    public void testUriHandler() throws URISyntaxException {

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

    }
}