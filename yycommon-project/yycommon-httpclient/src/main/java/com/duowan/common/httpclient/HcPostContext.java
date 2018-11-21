package com.duowan.common.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/20 21:31
 */
public class HcPostContext extends AbstractHcHttpTextResponseContext<HcPostContext, HttpPost> {

    public HcPostContext(String url, CloseableHttpClient httpClient, RequestConfig defaultRequestConfig) {
        super(url, httpClient, defaultRequestConfig);
    }

    @Override
    protected HttpPost createHttpRequest(String noParamUrl, UrlEncodedFormEntity formEntity) {

        HttpPost httpPost = new HttpPost(noParamUrl);

        if (null != formEntity) {
            httpPost.setEntity(formEntity);
        }

        return httpPost;

    }

}
