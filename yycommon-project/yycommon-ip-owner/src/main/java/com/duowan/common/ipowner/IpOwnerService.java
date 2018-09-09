package com.duowan.common.ipowner;

/**
 * IP 信息业务接口
 *
 * @author Arvin
 */
public interface IpOwnerService {

    /**
     * 获取IP归属省信息
     *
     * @param ip           ip 地址
     * @param fillProvince 填充省信息
     * @param fillCity     填充市信息
     * @param fillDistrict 填充区县信息
     * @return 返回ip信息
     */
    IpOwnerInfo getIpOwnerInfo(String ip, boolean fillProvince, boolean fillCity, boolean fillDistrict);
}
