package com.duowan.yyspringboot.autoconfigure.udbsecurity;

import com.duowan.udb.security.CheckMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/22 11:05
 */
@ConfigurationProperties(prefix = "yyspring.udbsecurity")
public class UdbSecurityProperties {

    /**
     * UDB 提供的APPID，登录验证任意UDB的UDBID即可默认是5048
     **/
    private String appid = "5048";

    /**
     * udb 的 appkey 值
     **/
    private String appkey = "83562A55E929F86B5E5D5FBF50EEC45C";

    /**
     * 是否启用UDB登录拦截，默认启用，如果不想使用拦截而只是使用UDB的认证功能，可设置为false
     **/
    private boolean interceptorEnabled = true;

    /**
     * 是否拦截静态资源，默认是不拦截
     **/
    private boolean staticSkip = true;

    /**
     * 默认检查模式，可选择为： WEAK: 弱验证，即本地Cookie验证 STRONG: 强验证，远程UDB验
     **/
    private CheckMode defaultCheckMode = CheckMode.WEAK;

    /**
     * 要拦截的请求，默认是拦截全部，中间使用英文逗号分隔, 默认是 /admin/**
     **/
    private String[] pathPatterns = new String[]{"/admin/**"};

    /**
     * 要忽略的请求连接，中间用英文逗号分隔，一定会忽略： /udb/getSdkAuthReq4LayerClose.do,/udb/callback.do,/udb/logout.do
     **/
    private String[] excludePathPatterns = new String[0];

    /**
     * 要忽略的包名或者类名称列表，中间用英文逗号分隔
     **/
    private String[] excludePackagesAndClasses = new String[]{"org.springframework"};

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public boolean isInterceptorEnabled() {
        return interceptorEnabled;
    }

    public void setInterceptorEnabled(boolean interceptorEnabled) {
        this.interceptorEnabled = interceptorEnabled;

    }

    public boolean isStaticSkip() {
        return staticSkip;
    }

    public void setStaticSkip(boolean staticSkip) {
        this.staticSkip = staticSkip;
    }

    public CheckMode getDefaultCheckMode() {
        return defaultCheckMode;
    }

    public void setDefaultCheckMode(CheckMode defaultCheckMode) {
        this.defaultCheckMode = defaultCheckMode;
    }

    public String[] getPathPatterns() {
        return pathPatterns;
    }

    public void setPathPatterns(String[] pathPatterns) {
        this.pathPatterns = pathPatterns;
    }

    public String[] getExcludePathPatterns() {
        return excludePathPatterns;
    }

    public void setExcludePathPatterns(String[] excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }

    public String[] getExcludePackagesAndClasses() {
        return excludePackagesAndClasses;
    }

    public void setExcludePackagesAndClasses(String[] excludePackagesAndClasses) {
        this.excludePackagesAndClasses = excludePackagesAndClasses;
    }
}
