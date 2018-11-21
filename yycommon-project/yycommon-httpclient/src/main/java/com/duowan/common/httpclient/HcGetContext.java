package com.duowan.common.httpclient;

import com.duowan.common.exception.HttpInvokeException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/20 21:31
 */
public class HcGetContext extends AbstractHcHttpTextResponseContext<HcGetContext, HttpGet> {

    public HcGetContext(boolean logEnabled, String url, CloseableHttpClient httpClient, RequestConfig defaultRequestConfig) {
        super(logEnabled, url, httpClient, defaultRequestConfig);
    }

    @Override
    protected HttpGet createHttpRequest(String noParamUrl, UrlEncodedFormEntity formEntity) {

        String finalUrl = noParamUrl;
        if (formEntity != null) {
            try {
                finalUrl = finalUrl + "?" + EntityUtils.toString(formEntity);
            } catch (IOException e) {
                throw new HttpInvokeException("Build query param string failed: " + e.getMessage(), e);
            }
        }

        return new HttpGet(finalUrl);

    }

}
