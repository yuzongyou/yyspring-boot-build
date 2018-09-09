package com.duowan.common.ipowner;

/**
 * IP 归属信息
 *
 * @author Arvin
 */
public class IpOwnerInfo {

    /**
     * 国家名称
     */
    private String country;
    /**
     * 省份/直辖市
     */
    private String province;
    /**
     * 地级市/省直辖县级行政区
     */
    private String city;
    /**
     * 区县
     */
    private String district;
    /**
     * 运营商
     */
    private String provider;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 国际、火星坐标
     */
    private String location;
    /**
     * 时区、国际协调时间
     */
    private String timeZone;
    /**
     * 行政区划代码
     */
    private String code;
    /**
     * 国家代码
     */
    private String countryCode;
    /**
     * 国际代码
     */
    private String internationalCode;
    /**
     * 县级 行政区划代码
     */
    private String districtCode;
    /**
     * 覆盖方位，IP使用区域半径，单位：千米
     */
    private String districtRadius;
    /**
     * 区县中心点经度
     */
    private String districtLongitude;
    /**
     * 区县中心点纬度
     */
    private String districtLatitude;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getInternationalCode() {
        return internationalCode;
    }

    public void setInternationalCode(String internationalCode) {
        this.internationalCode = internationalCode;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictRadius() {
        return districtRadius;
    }

    public void setDistrictRadius(String districtRadius) {
        this.districtRadius = districtRadius;
    }

    public String getDistrictLongitude() {
        return districtLongitude;
    }

    public void setDistrictLongitude(String districtLongitude) {
        this.districtLongitude = districtLongitude;
    }

    public String getDistrictLatitude() {
        return districtLatitude;
    }

    public void setDistrictLatitude(String districtLatitude) {
        this.districtLatitude = districtLatitude;
    }
}
