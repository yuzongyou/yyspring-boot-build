package com.duowan.common.ipowner.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Arvin
 */
public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 获取存储基路径
     */
    public static final String IP_DATA_FILE_STORE_FOLDER = getIpDataStoreFolder();

    private static String getIpDataStoreFolder() {

        // 先读取JVM参数

        String key = "DWPROJECTNO";
        String projectNo = System.getProperty(key);
        if (StringUtils.isBlank(projectNo)) {
            projectNo = System.getenv(key);
        }

        if (StringUtils.isBlank(projectNo)) {
            return "/data2/log/resin/";
        }

        return "/data/wwwdata/" + projectNo + "/";
    }

    /**
     * Cache-Control 请求头, 30min
     */
    public static final String DEFAULT_CACHE_CONTROL_MAX_AGE = "max-age=" + 60 * 30;

    /**
     * 未知的省份
     */
    public static final String UNKNOWN_PROVINCE_NAME = "UNKNOWN";

    /**
     * 默认 省市 ip.datx 文件路径
     */
    public static final String DEFAULT_IP_PROVINCE_CITY_FILE_PATH = IP_DATA_FILE_STORE_FOLDER + "provinceAndCityIp.datx";

    /**
     * 默认 省市 ip.datx 文件下载路径
     */
    public static final String DEFAULT_IP_PROVINCE_CITY_DOWNLOAD_URL = "http://ipip.yy.com/ip.datx";

    /**
     * 默认 区县 ip.datx 文件路径
     */
    public static final String DEFAULT_IP_DISTRICT_FILE_PATH = IP_DATA_FILE_STORE_FOLDER + "districtIp.datx";

    /**
     * 默认 区县 ip.datx 文件下载路径
     */
    public static final String DEFAULT_IP_DISTRICT_DOWNLOAD_URL = "http://ipip.yy.com/ip.quxian.datx";

    /**
     * 是否默认异步加载
     */
    public static final String DEFAULT_ASYNC_INIT = "false";
}
