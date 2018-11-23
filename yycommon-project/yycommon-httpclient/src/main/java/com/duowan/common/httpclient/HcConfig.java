package com.duowan.common.httpclient;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/21 10:29
 */
public class HcConfig {

    public static final String KEY_ENV_VAR_NAME = "hcenv.var.name";
    public static final String KEY_ENV_DEV = "hcenv.dev";
    public static final String KEY_ENV_TEST = "hcenv.test";
    public static final String KEY_ENV_PROD = "hcenv.prod";
    public static final String KEY_MANAGER_MAX_TOTAL = "hc.pool.manager.maxTotal";
    public static final String KEY_MANAGER_DEFAULT_MAX_PER_ROUTE = "hc.pool.manager.defaultMaxPerRoute";
    public static final String KEY_MANAGER_DEFAULT_VALIDATE_AFTER_INACTIVITY = "hc.pool.manager.validateAfterInactivity";
    public static final String KEY_CONNECTION_REQUEST_TIMEOUT = "hc.request.config.default.connectionRequestTimeout";
    public static final String KEY_CONNECTION_TIMEOUT = "hc.request.config.default.connectTimeout";
    public static final String KEY_SOCKET_TIMEOUT = "hc.request.config.default.socketTimeout";
    public static final String KEY_STALE_CONNECTION_CHECK_ENABLED = "hc.request.config.default.staleConnectionCheckEnabled";
    public static final String KEY_REDIRECTS_ENABLED = "hc.request.config.default.redirectsEnabled";
    public static final String KEY_MAX_REDIRECTS = "hc.request.config.default.maxRedirects";
    public static final String KEY_RELATIVE_REDIRECTS_ALLOWED = "hc.request.config.default.relativeRedirectsAllowed";
    public static final String KEY_AUTHENTICATION_ENABLED = "hc.request.config.default.authenticationEnabled";
    public static final String KEY_CONTENT_COMPRESSION_ENABLED = "hc.request.config.default.contentCompressionEnabled";
    public static final String KEY_CONNECTION_MANAGER_SHARED = "hc.connectionManagerShared";
    public static final String KEY_RETRY_TIMES = "hc.retryTimes";
    public static final String KEY_LOG_ENABLED = "hc.logEnabled";

    private String[] envVarNames = new String[]{"ENV", "DWENV"};

    private String envDev = "dev";
    private String envTest = "test";
    private String envProd = "prod";

    private String currentEnv = "dev";

    /**
     * 最大连接数
     **/
    private int maxTotal = 600;
    /**
     * 每个路由最大连接数，需要小于等于 maxTotal
     **/
    private int defaultMaxPerRoute = 200;
    /**
     * 是否共享连接池管理器
     **/
    private boolean connectionManagerShared = false;
    /**
     * 在从连接池获取连接时，连接不活跃多长时间需要进行一次验证，默认是 2 秒, 单位是毫秒
     **/
    private int validateAfterInactivity = 5000;

    private int connectionRequestTimeout = 2000;
    private int connectTimeout = 3000;
    private int socketTimeout = 5000;

    private boolean staleConnectionCheckEnabled = false;
    private boolean redirectsEnabled = true;
    private int maxRedirects = 50;
    private boolean relativeRedirectsAllowed = true;
    private boolean authenticationEnabled = true;
    private boolean contentCompressionEnabled = true;

    /** 是否开启日志 **/
    private boolean logEnabled = true;

    /**
     * 重试次数
     **/
    private int retryTimes = 0;


    public String[] getEnvVarNames() {
        return envVarNames;
    }

    public void setEnvVarNames(String[] envVarNames) {
        this.envVarNames = envVarNames;
    }

    public String getEnvDev() {
        return envDev;
    }

    public void setEnvDev(String envDev) {
        this.envDev = envDev;
    }

    public String getEnvTest() {
        return envTest;
    }

    public void setEnvTest(String envTest) {
        this.envTest = envTest;
    }

    public String getEnvProd() {
        return envProd;
    }

    public void setEnvProd(String envProd) {
        this.envProd = envProd;
    }

    public String getCurrentEnv() {
        return currentEnv;
    }

    public void setCurrentEnv(String currentEnv) {
        this.currentEnv = currentEnv;
    }

    public boolean isDev() {
        return envDev.equalsIgnoreCase(currentEnv);
    }

    public boolean isProd() {
        return envProd.equalsIgnoreCase(currentEnv);
    }

    public boolean isTest() {
        return envTest.equalsIgnoreCase(currentEnv);
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public boolean isConnectionManagerShared() {
        return connectionManagerShared;
    }

    public void setConnectionManagerShared(boolean connectionManagerShared) {
        this.connectionManagerShared = connectionManagerShared;
    }

    public int getValidateAfterInactivity() {
        return validateAfterInactivity;
    }

    public void setValidateAfterInactivity(int validateAfterInactivity) {
        this.validateAfterInactivity = validateAfterInactivity;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public boolean isStaleConnectionCheckEnabled() {
        return staleConnectionCheckEnabled;
    }

    public void setStaleConnectionCheckEnabled(boolean staleConnectionCheckEnabled) {
        this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
    }

    public boolean isRedirectsEnabled() {
        return redirectsEnabled;
    }

    public void setRedirectsEnabled(boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
    }

    public int getMaxRedirects() {
        return maxRedirects;
    }

    public void setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
    }

    public boolean isRelativeRedirectsAllowed() {
        return relativeRedirectsAllowed;
    }

    public void setRelativeRedirectsAllowed(boolean relativeRedirectsAllowed) {
        this.relativeRedirectsAllowed = relativeRedirectsAllowed;
    }

    public boolean isAuthenticationEnabled() {
        return authenticationEnabled;
    }

    public void setAuthenticationEnabled(boolean authenticationEnabled) {
        this.authenticationEnabled = authenticationEnabled;
    }

    public boolean isContentCompressionEnabled() {
        return contentCompressionEnabled;
    }

    public void setContentCompressionEnabled(boolean contentCompressionEnabled) {
        this.contentCompressionEnabled = contentCompressionEnabled;
    }

    public boolean isLogEnabled() {
        return logEnabled;
    }

    public void setLogEnabled(boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }
}
