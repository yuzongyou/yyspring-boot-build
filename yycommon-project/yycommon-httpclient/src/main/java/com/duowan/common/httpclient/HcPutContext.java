package com.duowan.common.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/20 21:31
 */
public class HcPutContext extends AbstractHcHttpTextResponseContext<HcPutContext, HttpPut> {

    public HcPutContext(boolean logEnabled, String url, CloseableHttpClient httpClient, RequestConfig defaultRequestConfig) {
        super(logEnabled, url, httpClient, defaultRequestConfig);
    }

    @Override
    protected HttpPut createHttpRequest(String noParamUrl, UrlEncodedFormEntity formEntity) {

        HttpPut httpPut = new HttpPut(noParamUrl);

        if (null != formEntity) {
            httpPut.setEntity(formEntity);
        }

        return httpPut;

    }

}
