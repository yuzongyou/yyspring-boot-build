package com.duwoan.yyspringcloud.msauth.gateway;

import com.duowan.yyspringcloud.msauth.Constants;
import com.duowan.yyspringcloud.msauth.Signer;
import com.duowan.yyspringcloud.msauth.app.App;
import com.duowan.yyspringcloud.msauth.app.AppReader;
import com.duowan.yyspringcloud.msauth.exception.AuthHeaderConflictException;
import com.duowan.yyspringcloud.msauth.exception.EmptyAppReaderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/19 11:24
 */
public class GatewayAddHeaderGlobalFilter implements GlobalFilter, Ordered {

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

    /** 排序 **/
    private int order;

    public GatewayAddHeaderGlobalFilter(String appId, AppReader appReader) {
        if (null == appId || "".equals(appId.trim())) {
            throw new IllegalArgumentException("Local Service appid should not be null");
        }
        if (null == appReader) {
            throw new EmptyAppReaderException("AppReader should not be null");
        }
        this.appId = appId;
        this.appReader = appReader;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getAppId() {
        return appId;
    }

    public AppReader getAppReader() {
        return appReader;
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
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (exchange.getRequest().getHeaders().containsKey(authHeader)) {
            logger.error("当前请求包含微服务内部使用认证请求头,uri= " + exchange.getRequest().getURI() + ", authHeader= " + authHeader);
            throw new AuthHeaderConflictException();
        }
        App app = appReader.read(appId);
        String sign = Signer.sign(appId, app.getSecretKey(), signLiveSeconds);

        ServerHttpRequest request = null;
        request = exchange.getRequest()
                .mutate()
                .header(authHeader, appId + "|" + sign)
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return order;
    }
}
