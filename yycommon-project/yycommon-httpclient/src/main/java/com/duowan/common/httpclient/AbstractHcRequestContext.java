package com.duowan.common.httpclient;

import com.duowan.common.exception.HttpInvokeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/21 8:49
 */
public abstract class AbstractHcRequestContext<S extends AbstractHcRequestContext, T extends HttpRequestBase> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 默认编码
     **/
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    private S self;

    private String url;

    private CloseableHttpClient httpClient;

    private Charset charset = DEFAULT_CHARSET;

    /**
     * Cookie
     **/
    private Map<String, String> cookieMap;

    /**
     * Header
     **/
    private Map<String, String> headerMap;

    /**
     * 参数列表
     **/
    private Map<String, String> paramsMap;

    private RequestConfig defaultRequestConfig;

    private RequestConfig.Builder customRequestConfigBuilder;

    private List<PreRequestInterceptor<S>> preRequestInterceptors;

    @SuppressWarnings({"unchecked"})
    public AbstractHcRequestContext(String url, CloseableHttpClient httpClient, RequestConfig defaultRequestConfig) {
        if (StringUtils.isBlank(url)) {
            throw new HttpInvokeException("GET request url should not be null!");
        }
        self = (S) this;
        this.httpClient = httpClient;
        this.defaultRequestConfig = defaultRequestConfig;
        extractUrl(url);
    }

    private void extractUrl(String url) {
        int index = url.indexOf("?");
        if (index > -1) {
            this.url = url.substring(0, index);
            this.paramsMap = extractParamsAsMap(url.substring(index + 1));
        } else {
            this.url = url;
        }
    }

    private static Map<String, String> extractParamsAsMap(String rawQuery) {
        Map<String, String> map = new TreeMap<>();
        if (null == rawQuery || "".equals(rawQuery.trim())) {
            return map;
        }

        String[] keyValues = rawQuery.split("&");
        for (String keyValue : keyValues) {
            if (null == keyValue || "".equals(keyValue.trim())) {
                continue;
            }
            if (!keyValue.contains("=")) {
                map.put(keyValue, null);
            } else {
                String[] array = keyValue.split("=");
                if (array.length == 1) {
                    map.put(array[0], "");
                } else {
                    map.put(array[0], array[1]);
                }
            }
        }
        return map;
    }

    public S getSelf() {
        return self;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public S encoding(String encoding) {
        this.charset = Charset.forName(encoding);
        return self;
    }

    public Charset getCharset() {
        return charset;
    }

    public RequestConfig.Builder config() {
        if (null == customRequestConfigBuilder) {
            customRequestConfigBuilder = defaultRequestConfig == null ? RequestConfig.custom() : RequestConfig.copy(defaultRequestConfig);
        }

        return customRequestConfigBuilder;
    }

    public S addPreInterceptor(PreRequestInterceptor<S>... preRequestInterceptors) {
        if (null == preRequestInterceptors || preRequestInterceptors.length < 1) {
            return self;
        }
        if (null == this.preRequestInterceptors) {
            this.preRequestInterceptors = new ArrayList<>();
        }
        this.preRequestInterceptors.addAll(Arrays.asList(preRequestInterceptors));
        return self;
    }

    /**
     * 设置 header
     *
     * @param name  header name
     * @param value header value
     * @return context
     */
    public S header(String name, String value) {
        if (headerMap == null) {
            headerMap = new HashMap<>();
        }
        this.headerMap.put(name, value);
        return self;
    }

    public S header(Map<String, String> headers) {

        if (headers == null || headers.isEmpty()) {
            return self;
        }

        if (headerMap == null) {
            headerMap = new HashMap<>();
        }

        this.headerMap.putAll(headers);
        return self;
    }


    /**
     * 设置 Cookie
     *
     * @param name  cookie name
     * @param value cookie value
     * @return context
     */
    public S cookie(String name, String value) {
        if (this.cookieMap == null) {
            this.cookieMap = new HashMap<>();
        }
        this.cookieMap.put(name, value);
        return self;
    }

    public S cookie(Map<String, String> cookies) {

        if (cookies == null || cookies.isEmpty()) {
            return self;
        }

        if (cookieMap == null) {
            cookieMap = new HashMap<>();
        }

        this.cookieMap.putAll(cookies);
        return self;
    }

    public S param(String name, String value) {
        if (this.paramsMap == null) {
            this.paramsMap = new HashMap<>();
        }

        this.paramsMap.put(name, value);
        return self;
    }

    public S param(Map<String, String> params) {

        if (params == null || params.isEmpty()) {
            return self;
        }

        if (paramsMap == null) {
            paramsMap = new HashMap<>();
        }

        this.paramsMap.putAll(params);
        return self;
    }

    protected void setCookie(T httpRequest) {

        if (null != cookieMap && !cookieMap.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                String value = entry.getValue() == null ? "" : entry.getValue();
                builder.append(entry.getKey()).append("=").append(value).append("; ");
            }
            builder.setLength(builder.length() - 2);

            httpRequest.setHeader("Cookie", builder.toString());
        }

    }

    protected void setHeader(T httpRequest) {

        if (null != headerMap && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                httpRequest.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    public HttpResponse request() {

        try {

            // 前置拦截器
            if (null != this.preRequestInterceptors && !this.preRequestInterceptors.isEmpty()) {
                for (PreRequestInterceptor<S> interceptor : this.preRequestInterceptors) {
                    interceptor.preHandle(self);
                }
            }

            T httpRequest = createHttpRequest(url, buildFormEntity());

            setHeader(httpRequest);
            setCookie(httpRequest);

            if (customRequestConfigBuilder == null) {
                httpRequest.setConfig(defaultRequestConfig);
            } else {
                httpRequest.setConfig(customRequestConfigBuilder.build());
            }

            if (logger.isDebugEnabled()) {
                logHttpRequestInfo(httpRequest);
            }

            return this.httpClient.execute(httpRequest);
        } catch (IOException e) {
            throw new HttpInvokeException(e);
        }
    }

    protected void logHttpRequestInfo(T httpRequest) {

        logger.debug(httpRequest.getRequestLine().toString());
        Header[] headers = httpRequest.getAllHeaders();
        if (null != headers) {
            for (Header header : headers) {
                logger.debug(header.getName() + ": " + header.getValue());
            }
        }

        logger.debug(httpRequest.getConfig().toString());
    }

    protected UrlEncodedFormEntity buildFormEntity() {
        if (null == paramsMap || paramsMap.isEmpty()) {
            return null;
        }

        List<NameValuePair> list = new ArrayList<>();
        for (String key : paramsMap.keySet()) {
            list.add(new BasicNameValuePair(key, String.valueOf(paramsMap.get(key))));
        }

        return new UrlEncodedFormEntity(list, getCharset());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public Map<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(Map<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public RequestConfig getDefaultRequestConfig() {
        return defaultRequestConfig;
    }

    public void setDefaultRequestConfig(RequestConfig defaultRequestConfig) {
        this.defaultRequestConfig = defaultRequestConfig;
    }

    public RequestConfig.Builder getCustomRequestConfigBuilder() {
        return customRequestConfigBuilder;
    }

    public void setCustomRequestConfigBuilder(RequestConfig.Builder customRequestConfigBuilder) {
        this.customRequestConfigBuilder = customRequestConfigBuilder;
    }

    /**
     * 创建 http 请求对象
     *
     * @param noParamUrl 不带参数的 url
     * @param formEntity 表单参数
     * @return 返回 http 请求对象
     */
    protected abstract T createHttpRequest(String noParamUrl, UrlEncodedFormEntity formEntity);
}
