package com.duowan.yyspringboot.autoconfigure.admincenter;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 15:46
 */
@ConfigurationProperties(prefix = "yyspring.admincenter")
public class AdmincenterProperties {

    /**
     * 退出地址
     **/
    private String logoutUrl = "/udb/logout.do";

    /**
     * 在Admincenter中申请的产品ID
     **/
    private String productId;

    /**
     * 在Admincenter中申请的时候分配的key
     **/
    private String productKey;

    /**
     * 产品名称
     **/
    private String productName;

    /**
     * 权限文件路径,classpath 或者绝对路径，或者url
     **/
    private String privilegeXmlPath = "classpath:/static/privilege.xml";

    /**
     * 获取权限URL
     **/
    private String fetchPrivilegeUrl;

    /**
     * 要忽略的请求连接, 默认忽略 /privilege.xml,/admin/privileges.do
     **/
    private String[] excludePathPatterns = new String[0];

    /**
     * 没有权限的跳转页面url，默认是 /admin/views/403.html
     **/
    private String forbiddenUrl = "/admin/views/403.html";

    /**
     * 是否需要检查权限，默认根据环境来，开发环境则不需要
     **/
    private Boolean needCheckPrivilege;

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPrivilegeXmlPath() {
        return privilegeXmlPath;
    }

    public void setPrivilegeXmlPath(String privilegeXmlPath) {
        this.privilegeXmlPath = privilegeXmlPath;
    }

    public String getFetchPrivilegeUrl() {
        return fetchPrivilegeUrl;
    }

    public void setFetchPrivilegeUrl(String fetchPrivilegeUrl) {
        this.fetchPrivilegeUrl = fetchPrivilegeUrl;
    }

    public String[] getExcludePathPatterns() {
        return excludePathPatterns;
    }

    public void setExcludePathPatterns(String[] excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }

    public String getForbiddenUrl() {
        return forbiddenUrl;
    }

    public void setForbiddenUrl(String forbiddenUrl) {
        this.forbiddenUrl = forbiddenUrl;
    }

    public Boolean getNeedCheckPrivilege() {
        return needCheckPrivilege;
    }

    public void setNeedCheckPrivilege(Boolean needCheckPrivilege) {
        this.needCheckPrivilege = needCheckPrivilege;
    }
}
