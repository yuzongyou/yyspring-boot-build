package com.duowan.yyspringboot.autoconfigure.ipowner;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/9 17:56
 */
@ConfigurationProperties(prefix = "yyspring.ipowner")
public class IpOwnerProperties {

    /**
     * 区县IP文件下载地址，默认是： http://ipip.yy.com/ip.quxian.datx
     **/
    private String districtIpdatxDownloadUrl = "http://ipip.yy.com/ip.quxian.datx";

    /**
     * 文件存储路径，默认是 /data/wwwdata/${DWPROJECTNO}/districtIp.datx
     **/
    private String districtIpdatxFilePath = "/data/wwwdata/${projectNo}/districtIp.datx";

    /**
     * 是否异步初始化，如果异步初始化的话，未初始化完成之前可能会获取IP信息失败，默认是非异步
     **/
    private boolean districtAsyncInit = false;

    /**
     * 省市IP文件下载地址，默认是： http://ipip.yy.com/ip.datx
     **/
    private String provinceCityIpdatxDownloadUrl = "http://ipip.yy.com/ip.datx";

    /**
     * 文件存储路径，默认是 /data/wwwdata/${DWPROJECTNO}/provinceAndCityIp.datx
     **/
    private String provinceCityIpdatxFilePath = "/data/wwwdata/${projectNo}/provinceAndCityIp.datx";

    /**
     * 是否异步初始化，如果异步初始化的话，未初始化完成之前可能会获取IP信息失败，默认是非异步
     **/
    private boolean provinceCityAsyncInit = false;

    public String getDistrictIpdatxDownloadUrl() {
        return districtIpdatxDownloadUrl;
    }

    public void setDistrictIpdatxDownloadUrl(String districtIpdatxDownloadUrl) {
        this.districtIpdatxDownloadUrl = districtIpdatxDownloadUrl;
    }

    public String getDistrictIpdatxFilePath() {
        return districtIpdatxFilePath;
    }

    public void setDistrictIpdatxFilePath(String districtIpdatxFilePath) {
        this.districtIpdatxFilePath = districtIpdatxFilePath;
    }

    public boolean isDistrictAsyncInit() {
        return districtAsyncInit;
    }

    public void setDistrictAsyncInit(boolean districtAsyncInit) {
        this.districtAsyncInit = districtAsyncInit;
    }

    public String getProvinceCityIpdatxDownloadUrl() {
        return provinceCityIpdatxDownloadUrl;
    }

    public void setProvinceCityIpdatxDownloadUrl(String provinceCityIpdatxDownloadUrl) {
        this.provinceCityIpdatxDownloadUrl = provinceCityIpdatxDownloadUrl;
    }

    public String getProvinceCityIpdatxFilePath() {
        return provinceCityIpdatxFilePath;
    }

    public void setProvinceCityIpdatxFilePath(String provinceCityIpdatxFilePath) {
        this.provinceCityIpdatxFilePath = provinceCityIpdatxFilePath;
    }

    public boolean isProvinceCityAsyncInit() {
        return provinceCityAsyncInit;
    }

    public void setProvinceCityAsyncInit(boolean provinceCityAsyncInit) {
        this.provinceCityAsyncInit = provinceCityAsyncInit;
    }
}
