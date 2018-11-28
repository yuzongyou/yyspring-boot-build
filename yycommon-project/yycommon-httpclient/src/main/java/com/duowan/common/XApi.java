package com.duowan.common;

import com.duowan.common.exception.InvalidHttpUrlException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/28 15:30
 */
public class XApi {

    private final String baseUrl;

    public XApi(String baseUrl) {
        this.baseUrl = resolveBaseUrl(baseUrl);
    }

    private String resolveBaseUrl(String baseUrl) {

        if (StringUtils.isBlank(baseUrl)) {
            throw new InvalidHttpUrlException("Api base url should not be null");
        }

        return baseUrl.replaceAll("/+$", "");
    }

    public String getApiUrl(String api) {
        if (api == null) {
            return null;
        }
        if (api.matches("^(?i)http(s)?:.*")) {
            return api;
        }
        return baseUrl + "/" + api.replaceAll("^/+", "");
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
