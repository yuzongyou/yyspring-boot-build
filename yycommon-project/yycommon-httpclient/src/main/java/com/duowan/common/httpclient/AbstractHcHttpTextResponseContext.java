package com.duowan.common.httpclient;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/20 21:31
 */
public abstract class AbstractHcHttpTextResponseContext<S extends AbstractHcHttpTextResponseContext, T extends HttpRequestBase> extends AbstractHcRequestContext<S, T> {

    public AbstractHcHttpTextResponseContext(String url, CloseableHttpClient httpClient, RequestConfig defaultRequestConfig) {
        super(url, httpClient, defaultRequestConfig);
    }

    public HttpTextResponse responseText() {
        return new HttpTextResponse(request(), getCharset());
    }
}
