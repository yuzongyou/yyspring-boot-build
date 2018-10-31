package com.duowan.yyspringcloud.autoconfigure.msauth;

import com.duowan.yyspringcloud.msauth.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 11:19
 */
@ConfigurationProperties(prefix = "yyspring.cloud.msauth")
public class MsauthProperties {

    /**
     * 要进行安全检查的URL匹配
     **/
    private Set<String> securityIncludePatterns;
    /**
     * 不需要进行安全检查的URL匹配规则，比includePatterns优先
     **/
    private Set<String> securityExcludePatterns;

    /**
     * 微服务应用ID
     **/
    private String appId;

    /**
     * 是否允许使用 EnvironmentAppReader
     **/
    private boolean enabledEnvironmentAppReader = true;

    /**
     * 是否允许使用 ApolloAppReader
     **/
    private boolean enabledApolloAppReader = true;

    /**
     * Apollo 配置中心 app reader namespaces
     **/
    private String[] apolloNamespaces = new String[]{"bizSupportGroup.ms-rpc-auth"};

    /**
     * 认证请求头
     **/
    private String authHeader = Constants.DEFAULT_AUTH_HEADER;

    /**
     * 签名key存活时间默认是10分钟, -1 表示不过期
     **/
    private long signLiveSeconds = 600;

    /**
     * API gateway header 过滤器排序
     **/
    private int headerGatewayFilterOrder = 0;

    /** 开发环境下不需要进行安全认证，默认是不检查 **/
    private boolean uncheckForDev = true;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Set<String> getSecurityIncludePatterns() {
        return securityIncludePatterns;
    }

    public void setSecurityIncludePatterns(Set<String> securityIncludePatterns) {
        this.securityIncludePatterns = securityIncludePatterns;
    }

    public Set<String> getSecurityExcludePatterns() {
        return securityExcludePatterns;
    }

    public void setSecurityExcludePatterns(Set<String> securityExcludePatterns) {
        this.securityExcludePatterns = securityExcludePatterns;
    }

    public boolean isEnabledEnvironmentAppReader() {
        return enabledEnvironmentAppReader;
    }

    public void setEnabledEnvironmentAppReader(boolean enabledEnvironmentAppReader) {
        this.enabledEnvironmentAppReader = enabledEnvironmentAppReader;
    }

    public boolean isEnabledApolloAppReader() {
        return enabledApolloAppReader;
    }

    public void setEnabledApolloAppReader(boolean enabledApolloAppReader) {
        this.enabledApolloAppReader = enabledApolloAppReader;
    }

    public String[] getApolloNamespaces() {
        return apolloNamespaces;
    }

    public void setApolloNamespaces(String[] apolloNamespaces) {
        this.apolloNamespaces = apolloNamespaces;
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

    public int getHeaderGatewayFilterOrder() {
        return headerGatewayFilterOrder;
    }

    public void setHeaderGatewayFilterOrder(int headerGatewayFilterOrder) {
        this.headerGatewayFilterOrder = headerGatewayFilterOrder;
    }

    public boolean isUncheckForDev() {
        return uncheckForDev;
    }

    public void setUncheckForDev(boolean uncheckForDev) {
        this.uncheckForDev = uncheckForDev;
    }
}
