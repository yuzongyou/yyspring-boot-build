package com.duowan.common.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/21 14:22
 */
public abstract class AbstractHttpResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHttpResponse.class);

    private HttpResponse httpResponse;
    private Charset charset;
    private Map<String, String> respHeaders;

    public AbstractHttpResponse(HttpResponse httpResponse, Charset charset) {
        this.httpResponse = httpResponse;
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    /**
     * 尝试关闭response
     *
     * @param resp HttpResponse对象
     */
    protected void close(HttpResponse resp) {
        try {
            if (resp == null) {
                return;
            }
            //如果CloseableHttpResponse 是resp的父类，则支持关闭
            if (CloseableHttpResponse.class.isAssignableFrom(resp.getClass())) {
                ((CloseableHttpResponse) resp).close();
            } else {
                EntityUtils.consume(resp.getEntity());
            }
        } catch (IOException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e.getMessage(), e);
            }
        }
    }

    private Map<String, String> createResponseHeaders(HttpResponse response) {

        Header[] headers = response.getAllHeaders();

        if (null == headers || headers.length < 1) {
            return new HashMap<>(0);
        }

        Map<String, String> map = new HashMap<>();
        for (Header header : headers) {
            map.put(header.getName(), header.getValue());
        }
        return map;

    }

    public Map<String, String> headers() {
        if (respHeaders == null) {
            respHeaders = createResponseHeaders(httpResponse);
        }
        return respHeaders;
    }

    public String header(String name) {
        if (respHeaders == null) {
            respHeaders = createResponseHeaders(httpResponse);
        }
        return respHeaders.get(name);
    }
}
