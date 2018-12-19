package com.duowan.common.ipowner.util;

import com.duowan.common.ipowner.IpOwnerInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * IpOwnerInfo 工具类
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/7/27 17:27
 */
public class IpOwnerInfoUtil {

    private IpOwnerInfoUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * <pre>
     * 根据 省市的 查询结果填充
     * // result: [中国] [广东] [广州] [] [联通] [23.125178] [113.280637] [Asia/Shanghai] [UTC+8] [440100] [86] [CN] [AP]
     * // ["中国","广东","广州","","联通","23.125178","113.280637","Asia/Shanghai","UTC+8","440100","86","CN","AP"]
     * // array[0]:    国家/地区
     * // array[1]:    省份/直辖市
     * // array[2]:    地级市/省直辖县级行政区
     * // array[3]:    区县
     * // array[4]:    网络运营商
     * // array[5]:    经度
     * // array[6]:    纬度
     * // array[7]:    国际、火星坐标
     * // array[8]:    时区、国际协调时间
     * // array[9]:    行政区划代码
     * // array[10]:   国家代码
     * // array[11]:   国际代码
     * </pre>
     *
     * @param ipOwnerInfo iP信息
     * @param array       查询结果值
     * @return 返回 IpOwnerInfo
     */
    public static IpOwnerInfo fillByProvinceAndCityResult(IpOwnerInfo ipOwnerInfo, String[] array) {

        if (ipOwnerInfo == null) {
            ipOwnerInfo = new IpOwnerInfo();
        }

        ipOwnerInfo.setCountry(array[0]);
        ipOwnerInfo.setProvince(array[1]);
        ipOwnerInfo.setCity(array[2]);
        ipOwnerInfo.setDistrict(array[3]);
        ipOwnerInfo.setProvider(array[4]);
        ipOwnerInfo.setLongitude(array[5]);
        ipOwnerInfo.setLatitude(array[6]);
        ipOwnerInfo.setLocation(array[7]);
        ipOwnerInfo.setTimeZone(array[8]);
        ipOwnerInfo.setCode(array[9]);
        ipOwnerInfo.setCountryCode(array[10]);
        ipOwnerInfo.setInternationalCode(array[11]);

        return ipOwnerInfo;
    }

    /**
     * <pre>
     * 根据 区县 的 查询结果填充
     * // result: [中国] [广东] [广州] [番禺区] [440113] [0.5] [113.38397] [22.93599]
     * // ["中国","广东","广州","番禺区","440113","9.4","113.38397","22.93599"]
     * // array[0]:    国家/地区
     * // array[1]:    省份/直辖市
     * // array[2]:    地级市/省直辖县级行政区
     * // array[3]:    区县
     * // array[4]:    中国行政区划代码
     * // array[5]:    覆盖方位，IP使用区域半径，单位：千米
     * // array[6]:    区县中心点经度
     * // array[6]:    区县中心点纬度
     * </pre>
     *
     * @param ipOwnerInfo iP信息
     * @param array       查询结果值
     * @return 返回 IpOwnerInfo
     */
    public static IpOwnerInfo fillByDistrictResult(IpOwnerInfo ipOwnerInfo, String[] array) {

        if (ipOwnerInfo == null) {
            ipOwnerInfo = new IpOwnerInfo();
        }

        if (StringUtils.isBlank(ipOwnerInfo.getCountry())) {
            ipOwnerInfo.setCountry(array[0]);
        }
        if (StringUtils.isBlank(ipOwnerInfo.getProvince())) {
            ipOwnerInfo.setProvince(array[1]);
        }
        if (StringUtils.isBlank(ipOwnerInfo.getCity())) {
            ipOwnerInfo.setCity(array[2]);
        }
        if (StringUtils.isBlank(ipOwnerInfo.getDistrict())) {
            ipOwnerInfo.setDistrict(array[3]);
        }

        ipOwnerInfo.setDistrictCode(array[4]);
        ipOwnerInfo.setDistrictRadius(array[5]);
        ipOwnerInfo.setDistrictLongitude(array[6]);
        ipOwnerInfo.setDistrictLatitude(array[7]);

        return ipOwnerInfo;
    }

}
