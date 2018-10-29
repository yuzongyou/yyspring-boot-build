package com.duowan.yyspringboot.autoconfigure.swagger2;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/19 18:09
 */
@ConfigurationProperties(prefix = "yyspring.swagger2")
public class Swagger2Properties {

    /** 标题 **/
    private String title;

    /** 内容描述 **/
    private String description;

    /** 版本号 **/
    private String version = "1.0";

    /** 联系人 **/
    private String contactName;

    /** 联系地址 **/
    private String contactUrl;

    /** 联系邮箱 **/
    private String contactEmail;

    /** 团队服务地址 **/
    private String termsOfServiceUrl;

    /** 扫描包名，默认是 根据启动类进行自动计算 **/
    private String basePackage;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactUrl() {
        return contactUrl;
    }

    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }
}
