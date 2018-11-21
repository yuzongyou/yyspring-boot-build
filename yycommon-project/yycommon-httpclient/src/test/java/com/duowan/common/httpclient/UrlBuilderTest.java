package com.duowan.common.httpclient;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/21 9:22
 */
public class UrlBuilderTest {

    @Test
    public void testUrlBuilder() throws IOException {
        HttpGet request = new HttpGet("http://example.com/?var=1&var=2");
        URIBuilder newBuilder = new URIBuilder(request.getURI());

        System.out.println(request.getURI().getPath());
        System.out.println(request.getURI().getRawPath());
        System.out.println(request.getURI().getRawQuery());

        // 获取键值对列表
        List<NameValuePair> params = newBuilder.getQueryParams();
        //转换为键值对字符串
        String str = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));

        System.out.println(str);
    }
}
