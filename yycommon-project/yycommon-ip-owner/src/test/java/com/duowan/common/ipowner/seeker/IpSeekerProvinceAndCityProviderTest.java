package com.duowan.common.ipowner.seeker;

import com.duowan.common.utils.JsonUtil;
import org.junit.Test;

/**
 * 测试 省市的 IPSeeker 提供者
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/7/27 17:07
 */
public class IpSeekerProvinceAndCityProviderTest {

    @Test
    public void provider() {

        IpSeekerProvinceAndCityProvider provider = new IpSeekerProvinceAndCityProvider();
        provider.init();

        IpSeeker seeker = provider.getSeeker();

        String[] ipInfos = seeker.find("58.248.229.154");

        System.out.println(JsonUtil.toJson(ipInfos));


    }
}