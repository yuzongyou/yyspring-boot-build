package com.duowan.common.httpclient;

import com.duowan.common.XApi;
import com.duowan.common.util.Util;

import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 15:30
 */
@SuppressWarnings({"unchecked"})
public class XHttpClient {

    private final XApi xApi;
    private boolean logEnabled;
    /**
     * 请求前的拦截器
     **/
    private PreRequestInterceptor<AbstractHcRequestContext> globalPreRequestInterceptor;
    private PreRequestInterceptor<HcPostContext> globalPostPreInterceptor;
    private PreRequestInterceptor<HcGetContext> globalGetPreInterceptor;
    private PreRequestInterceptor<HcDeleteContext> globalDeletePreInterceptor;
    private PreRequestInterceptor<HcPutContext> globalPutPreInterceptor;

    private PreRequestInterceptor<HcPostContext> postPreInterceptor;
    private PreRequestInterceptor<HcGetContext> getPreInterceptor;
    private PreRequestInterceptor<HcDeleteContext> deletePreInterceptor;
    private PreRequestInterceptor<HcPutContext> putPreInterceptor;

    public XHttpClient() {
        this(null, null);
    }

    public XHttpClient(String baseUrl) {
        this(baseUrl, null);
    }

    public XHttpClient(String baseUrl, final PreRequestInterceptor<AbstractHcRequestContext> preRequestInterceptor) {
        xApi = Util.isBlank(baseUrl) ? null : new XApi(baseUrl);
        this.setPreRequestInterceptor(preRequestInterceptor);
    }

    public PreRequestInterceptor<AbstractHcRequestContext> getPreRequestInterceptor() {
        return globalPreRequestInterceptor;
    }

    public void setPreRequestInterceptor(final PreRequestInterceptor<AbstractHcRequestContext> preRequestInterceptor) {
        this.globalPreRequestInterceptor = preRequestInterceptor;

        if (null != this.globalPreRequestInterceptor) {
            this.globalPostPreInterceptor = preRequestInterceptor::preHandle;
            this.globalGetPreInterceptor = preRequestInterceptor::preHandle;
            this.globalDeletePreInterceptor = preRequestInterceptor::preHandle;
            this.globalPutPreInterceptor = preRequestInterceptor::preHandle;
        }
    }

    public boolean isLogEnabled() {
        return logEnabled;
    }

    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    public String getApiUrl(String api) {
        return null == xApi ? api : xApi.getApiUrl(api);
    }

    public PreRequestInterceptor<HcPostContext> getPostPreInterceptor() {
        return postPreInterceptor == null ? globalPostPreInterceptor : postPreInterceptor;
    }

    public void setPostPreInterceptor(PreRequestInterceptor<HcPostContext> postPreInterceptor) {
        this.postPreInterceptor = postPreInterceptor;
    }

    public PreRequestInterceptor<HcGetContext> getGetPreInterceptor() {
        return getPreInterceptor == null ? globalGetPreInterceptor : getPreInterceptor;
    }

    public void setGetPreInterceptor(PreRequestInterceptor<HcGetContext> getPreInterceptor) {
        this.getPreInterceptor = getPreInterceptor;
    }

    public PreRequestInterceptor<HcDeleteContext> getDeletePreInterceptor() {
        return deletePreInterceptor == null ? globalDeletePreInterceptor : deletePreInterceptor;
    }

    public void setDeletePreInterceptor(PreRequestInterceptor<HcDeleteContext> deletePreInterceptor) {
        this.deletePreInterceptor = deletePreInterceptor;
    }

    public PreRequestInterceptor<HcPutContext> getPutPreInterceptor() {
        return putPreInterceptor == null ? globalPutPreInterceptor : putPreInterceptor;
    }

    public void setPutPreInterceptor(PreRequestInterceptor<HcPutContext> putPreInterceptor) {
        this.putPreInterceptor = putPreInterceptor;
    }

    public HcGetContext get(String url) {
        return get(logEnabled, url);
    }

    public HcPostContext post(String url) {
        return post(logEnabled, url);
    }

    public HcDeleteContext delete(String url) {
        return delete(logEnabled, url);
    }

    public HcPutContext put(String url) {
        return put(logEnabled, url);
    }

    public HcGetContext get(boolean logEnabled, String url) {
        return HttpClientUtils.get(logEnabled, getApiUrl(url)).addPreInterceptor(getGetPreInterceptor());
    }

    public HcPostContext post(boolean logEnabled, String url) {
        return HttpClientUtils.post(logEnabled, getApiUrl(url)).addPreInterceptor(getPostPreInterceptor());
    }

    public HcDeleteContext delete(boolean logEnabled, String url) {
        return HttpClientUtils.delete(logEnabled, getApiUrl(url)).addPreInterceptor(getDeletePreInterceptor());
    }

    public HcPutContext put(boolean logEnabled, String url) {
        return HttpClientUtils.put(logEnabled, getApiUrl(url)).addPreInterceptor(getPutPreInterceptor());
    }

    public String getText(boolean logEnabled, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {
        HcGetContext context = get(logEnabled, url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asText();
    }

    public String getText(String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {
        HcGetContext context = get(url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asText();
    }

    public String getText(String url, Map<String, String> paramMap) {
        return get(url).param(paramMap).responseText().asText();
    }

    public String getText(boolean logEnabled, String url, Map<String, String> paramMap) {
        return get(logEnabled, url).param(paramMap).responseText().asText();
    }

    public String getText(String url) {
        return get(url).responseText().asText();
    }

    public String getText(boolean logEnablde, String url) {
        return get(logEnablde, url).responseText().asText();
    }

    public <T> T getObjectForStdJsonResp(boolean logEnabled, Class<T> requireType, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcGetContext context = get(logEnabled, url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asObjectForStdJsonResp(requireType);

    }

    public <T> T getObjectForStdJsonResp(Class<T> requireType, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcGetContext context = get(url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asObjectForStdJsonResp(requireType);

    }

    public <T> T getObjectForStdJsonResp(Class<T> requireType, String url, Map<String, String> paramMap) {
        return get(url).param(paramMap).responseText().asObjectForStdJsonResp(requireType);
    }

    public <T> T getObjectForStdJsonResp(boolean logEnabled, Class<T> requireType, String url, Map<String, String> paramMap) {
        return get(logEnabled, url).param(paramMap).responseText().asObjectForStdJsonResp(requireType);
    }

    public <T> T getObjectForStdJsonResp(Class<T> requireType, String url) {
        return get(url).responseText().asObjectForStdJsonResp(requireType);
    }

    public <T> T getObjectForStdJsonResp(boolean logEnabled, Class<T> requireType, String url) {
        return get(logEnabled, url).responseText().asObjectForStdJsonResp(requireType);
    }

    public <T> T getObject(boolean logEnabled, Class<T> requireType, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcGetContext context = get(logEnabled, url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asObject(requireType);

    }

    public <T> T getObject(Class<T> requireType, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcGetContext context = get(url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asObject(requireType);

    }

    public <T> T getObject(Class<T> requireType, String url, Map<String, String> paramMap) {
        return get(url).param(paramMap).responseText().asObject(requireType);
    }

    public <T> T getObject(boolean logEnabled, Class<T> requireType, String url, Map<String, String> paramMap) {
        return get(logEnabled, url).param(paramMap).responseText().asObject(requireType);
    }

    public <T> T getObject(Class<T> requireType, String url) {
        return get(url).responseText().asObject(requireType);
    }

    public <T> T getObject(boolean logEnabled, Class<T> requireType, String url) {
        return get(logEnabled, url).responseText().asObject(requireType);
    }

    public Map<String, Object> getMap(boolean logEnabled, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcGetContext context = get(logEnabled, url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asMap();

    }

    public Map<String, Object> getMap(String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcGetContext context = get(url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asMap();

    }

    public Map<String, Object> getMap(String url, Map<String, String> paramMap) {
        return get(url).param(paramMap).responseText().asMap();
    }

    public Map<String, Object> getMap(boolean logEnabled, String url, Map<String, String> paramMap) {
        return get(logEnabled, url).param(paramMap).responseText().asMap();
    }

    public Map<String, Object> getMap(String url) {
        return get(url).responseText().asMap();
    }

    public Map<String, Object> getMap(boolean logEnabled, String url) {
        return get(logEnabled, url).responseText().asMap();
    }

    public Map<String, Object> getMapForStdJsonResp(boolean logEnabled, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcGetContext context = get(logEnabled, url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asMapForStdJsonResp();

    }

    public Map<String, Object> getMapForStdJsonResp(String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcGetContext context = get(url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asMapForStdJsonResp();

    }

    public Map<String, Object> getMapForStdJsonResp(String url, Map<String, String> paramMap) {
        return get(url).param(paramMap).responseText().asMapForStdJsonResp();
    }

    public Map<String, Object> getMapForStdJsonResp(boolean logEnabled, String url, Map<String, String> paramMap) {
        return get(logEnabled, url).param(paramMap).responseText().asMapForStdJsonResp();
    }

    public Map<String, Object> getMapForStdJsonResp(String url) {
        return get(url).responseText().asMapForStdJsonResp();
    }

    public Map<String, Object> getMapForStdJsonResp(boolean logEnabled, String url) {
        return get(logEnabled, url).responseText().asMapForStdJsonResp();
    }

    public String postText(boolean logEnabled, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {
        HcPostContext context = post(logEnabled, url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asText();
    }

    public String postText(String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {
        HcPostContext context = post(url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asText();
    }

    public String postText(String url, Map<String, String> paramMap) {
        return post(url).param(paramMap).responseText().asText();
    }

    public String postText(boolean logEnabled, String url, Map<String, String> paramMap) {
        return post(logEnabled, url).param(paramMap).responseText().asText();
    }

    public String postText(String url) {
        return post(url).responseText().asText();
    }

    public String postText(boolean logEnablde, String url) {
        return post(logEnablde, url).responseText().asText();
    }

    public <T> T postObjectForStdJsonResp(boolean logEnabled, Class<T> requireType, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcPostContext context = post(logEnabled, url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asObjectForStdJsonResp(requireType);

    }

    public <T> T postObjectForStdJsonResp(Class<T> requireType, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcPostContext context = post(url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asObjectForStdJsonResp(requireType);

    }

    public <T> T postObjectForStdJsonResp(Class<T> requireType, String url, Map<String, String> paramMap) {
        return post(url).param(paramMap).responseText().asObjectForStdJsonResp(requireType);
    }

    public <T> T postObjectForStdJsonResp(boolean logEnabled, Class<T> requireType, String url, Map<String, String> paramMap) {
        return post(logEnabled, url).param(paramMap).responseText().asObjectForStdJsonResp(requireType);
    }

    public <T> T postObjectForStdJsonResp(Class<T> requireType, String url) {
        return post(url).responseText().asObjectForStdJsonResp(requireType);
    }

    public <T> T postObjectForStdJsonResp(boolean logEnabled, Class<T> requireType, String url) {
        return post(logEnabled, url).responseText().asObjectForStdJsonResp(requireType);
    }

    public <T> T postObject(boolean logEnabled, Class<T> requireType, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcPostContext context = post(logEnabled, url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asObject(requireType);

    }

    public <T> T postObject(Class<T> requireType, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcPostContext context = post(url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asObject(requireType);

    }

    public <T> T postObject(Class<T> requireType, String url, Map<String, String> paramMap) {
        return post(url).param(paramMap).responseText().asObject(requireType);
    }

    public <T> T postObject(boolean logEnabled, Class<T> requireType, String url, Map<String, String> paramMap) {
        return post(logEnabled, url).param(paramMap).responseText().asObject(requireType);
    }

    public <T> T postObject(Class<T> requireType, String url) {
        return post(url).responseText().asObject(requireType);
    }

    public <T> T postObject(boolean logEnabled, Class<T> requireType, String url) {
        return post(logEnabled, url).responseText().asObject(requireType);
    }

    public Map<String, Object> postMap(boolean logEnabled, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcPostContext context = post(logEnabled, url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asMap();

    }

    public Map<String, Object> postMap(String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcPostContext context = post(url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asMap();

    }

    public Map<String, Object> postMap(String url, Map<String, String> paramMap) {
        return post(url).param(paramMap).responseText().asMap();
    }

    public Map<String, Object> postMap(boolean logEnabled, String url, Map<String, String> paramMap) {
        return post(logEnabled, url).param(paramMap).responseText().asMap();
    }

    public Map<String, Object> postMap(String url) {
        return post(url).responseText().asMap();
    }

    public Map<String, Object> postMap(boolean logEnabled, String url) {
        return post(logEnabled, url).responseText().asMap();
    }

    public Map<String, Object> postMapForStdJsonResp(boolean logEnabled, String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcPostContext context = post(logEnabled, url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asMapForStdJsonResp();

    }

    public Map<String, Object> postMapForStdJsonResp(String url, Map<String, String> paramMap, int connTimeout, int readTimeout) {

        HcPostContext context = post(url);
        context.config().setConnectTimeout(connTimeout).setConnectionRequestTimeout(readTimeout);
        return context.param(paramMap).responseText().asMapForStdJsonResp();

    }

    public Map<String, Object> postMapForStdJsonResp(String url, Map<String, String> paramMap) {
        return post(url).param(paramMap).responseText().asMapForStdJsonResp();
    }

    public Map<String, Object> postMapForStdJsonResp(boolean logEnabled, String url, Map<String, String> paramMap) {
        return post(logEnabled, url).param(paramMap).responseText().asMapForStdJsonResp();
    }

    public Map<String, Object> postMapForStdJsonResp(String url) {
        return post(url).addPreInterceptor().responseText().asMapForStdJsonResp();
    }

    public Map<String, Object> postMapForStdJsonResp(boolean logEnabled, String url) {
        return post(logEnabled, url).responseText().asMapForStdJsonResp();
    }


}
