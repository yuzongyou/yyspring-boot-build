package com.duowan.common.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.duowan.common.converter.ConverterContext;
import com.duowan.common.exception.HttpInvokeException;
import com.duowan.common.exception.HttpResponseConvertException;
import com.duowan.common.util.Util;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/21 14:18
 */
public class HttpTextResponse extends AbstractHttpResponse {

    private String responseText;

    public HttpTextResponse(HttpResponse httpResponse, Charset charset) {
        super(httpResponse, charset);

        try {
            this.responseText = EntityUtils.toString(httpResponse.getEntity(), charset);
        } catch (Exception e) {
            throw new HttpInvokeException(e);
        } finally {
            close(httpResponse);
        }
    }

    public String asText() {
        return responseText;
    }

    public String asTrimText() {
        return null == responseText ? null : responseText.trim();
    }

    public Integer asInt() {
        return Integer.parseInt(asTrimText());
    }

    public Long asLong() {
        return Long.parseLong(asTrimText());
    }

    public String[] asStringArray(String separator) {
        String text = asText();
        if (null == text) {
            return null;
        }
        return text.split(separator);
    }

    public <T> T asObject(Class<T> requireType) {
        String json = asTrimText();
        if (Util.isBlank(json)) {
            return null;
        }

        return JSON.parseObject(json, requireType);
    }

    /**
     * 标准格式的响应，类似：
     * <pre>
     * {
     *   "status": 200,
     *   "message": null,
     *   "data": {
     *     "xxx": 4
     *   }
     * }
     * </pre>
     *
     * @param requireType 目标结果
     * @param <T>         响应类型
     * @return 对象
     */
    public <T> T asObjectForStdJsonResp(Class<T> requireType) {
        String json = asTrimText();
        if (Util.isBlank(json)) {
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(json);
        int status = jsonObject.getIntValue("status");
        if (status == 200) {
            return ConverterContext.convertOneObject(requireType, jsonObject, "data");
        }
        throw new HttpResponseConvertException(status, jsonObject.getString("message"));
    }

    public <T> T[] asArrayForStdJsonResp(Class<T> requireType) {
        String json = asTrimText();
        if (Util.isBlank(json)) {
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(json);
        int status = jsonObject.getIntValue("status");
        if (status == 200) {
            return ConverterContext.convertToArray(requireType, jsonObject, "data");
        }
        throw new HttpResponseConvertException(status, jsonObject.getString("message"));
    }

    public Map<String, Object> asMap() {
        String json = asTrimText();
        if (Util.isBlank(json)) {
            return null;
        }

        return JSON.parseObject(json);
    }

    public Map<String, Object> asMapForStdJsonResp() {
        String json = asTrimText();
        if (Util.isBlank(json)) {
            return null;
        }

        JSONObject jsonObject = JSON.parseObject(json);
        int status = jsonObject.getIntValue("status");
        if (status == 200) {
            return jsonObject.getJSONObject("data");
        }
        throw new HttpResponseConvertException(status, jsonObject.getString("message"));
    }

}
