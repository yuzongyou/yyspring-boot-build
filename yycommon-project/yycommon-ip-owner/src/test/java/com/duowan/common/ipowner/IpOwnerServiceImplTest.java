package com.duowan.common.ipowner;

import com.duowan.common.ipowner.seeker.IpSeekerDistrictProvider;
import com.duowan.common.ipowner.seeker.IpSeekerProvinceAndCityProvider;
import com.duowan.common.utils.JsonUtil;
import org.junit.Test;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/9 17:36
 */
public class IpOwnerServiceImplTest {
    @Test
    public void getIpOwnerInfo() {

        IpOwnerServiceImpl ipInfoService = new IpOwnerServiceImpl();

        IpSeekerDistrictProvider ipSeekerDistrictProvider = new IpSeekerDistrictProvider();
        ipSeekerDistrictProvider.init();

        IpSeekerProvinceAndCityProvider ipSeekerProvinceAndCityProvider = new IpSeekerProvinceAndCityProvider();
        ipSeekerProvinceAndCityProvider.init();

        ipInfoService.setIpDistrictSeekerProvider(ipSeekerDistrictProvider);
        ipInfoService.setIpProvinceAndCitySeekerProvider(ipSeekerProvinceAndCityProvider);

        String ip = "58.248.229.154";

        IpOwnerInfo ipInfo = ipInfoService.getIpOwnerInfo(ip, true, true, true);
        System.out.println(JsonUtil.toPrettyJson(ipInfo));
    }

}