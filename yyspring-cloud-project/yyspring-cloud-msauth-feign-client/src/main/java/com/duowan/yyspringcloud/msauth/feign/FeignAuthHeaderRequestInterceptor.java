package com.duowan.yyspringcloud.msauth.feign;

import com.duowan.yyspringcloud.msauth.Constants;
import com.duowan.yyspringcloud.msauth.Signer;
import com.duowan.yyspringcloud.msauth.app.App;
import com.duowan.yyspringcloud.msauth.app.AppReader;
import com.duowan.yyspringcloud.msauth.exception.AuthHeaderConflictException;
import com.duowan.yyspringcloud.msauth.exception.EmptyAppReaderException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Feign 客户端header添加认证信息
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 10:51
 */
public class FeignAuthHeaderRequestInterceptor implements RequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 本服务的APPID
     **/
    private final String appId;

    /**
     * APP 信息读取
     **/
    private final AppReader appReader;

    /**
     * 认证请求头
     **/
    private String authHeader = Constants.DEFAULT_AUTH_HEADER;

    /**
     * 签名key存活时间默认是10分钟
     **/
    private long signLiveSeconds = 600;

    public FeignAuthHeaderRequestInterceptor(String appId, AppReader appReader) {
        if (appId == null || "".equals(appId.trim())) {
            throw new IllegalArgumentException("Local Service appid should not be null");
        }
        if (null == appReader) {
            throw new EmptyAppReaderException("AppReader should not be null");
        }
        this.appReader = appReader;
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public String getAuthHeader() {
        return authHeader;
    }

    public void setAuthHeader(String authHeader) {
        this.authHeader = authHeader;
    }

    public long getSignLiveSeconds() {
        return signLiveSeconds;
    }

    public void setSignLiveSeconds(long signLiveSeconds) {
        this.signLiveSeconds = signLiveSeconds;
    }

    @Override
    public void apply(RequestTemplate template) {

        App app = appReader.read(appId);
        if (null != app) {

            if (template.headers().containsKey(this.authHeader)) {
                logger.error("当前请求包含微服务内部使用认证请求头,uri= " + template.url() + ", authHeader= " + this.authHeader);
                throw new AuthHeaderConflictException();
            }
            String sign = Signer.sign(appId, app.getSecretKey(), signLiveSeconds);
            template.header(this.authHeader, appId + "|" + sign);
        }
    }

}
