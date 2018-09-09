package com.duowan.common.ipowner.seeker;

import com.duowan.common.utils.JsonUtil;
import org.junit.Test;

/**
 * TODO
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/7/27 17:14
 */
public class IpSeekerDistrictProviderTest {

    @Test
    public void districtProvider() {
        IpSeekerDistrictProvider provider = new IpSeekerDistrictProvider();
        provider.init();

        IpSeeker seeker = provider.getSeeker();

        String[] ipInfos = seeker.find("58.248.229.154");

        System.out.println(JsonUtil.toJson(ipInfos));
    }

}