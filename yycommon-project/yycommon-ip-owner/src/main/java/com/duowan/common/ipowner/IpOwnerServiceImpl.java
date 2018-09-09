package com.duowan.common.ipowner;

import com.duowan.common.ipowner.seeker.IpSeeker;
import com.duowan.common.ipowner.seeker.IpSeekerProvider;
import com.duowan.common.ipowner.util.IpOwnerInfoUtil;
import com.duowan.common.utils.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Ip 信息查询业务实现类
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/7/27 16:31
 */
public class IpOwnerServiceImpl implements IpOwnerService {

    private static final Logger logger = LoggerFactory.getLogger(IpOwnerServiceImpl.class);

    private IpSeekerProvider ipProvinceAndCitySeekerProvider;

    private IpSeekerProvider ipDistrictSeekerProvider;

    public IpSeekerProvider getIpProvinceAndCitySeekerProvider() {
        return ipProvinceAndCitySeekerProvider;
    }

    public void setIpProvinceAndCitySeekerProvider(IpSeekerProvider ipProvinceAndCitySeekerProvider) {
        this.ipProvinceAndCitySeekerProvider = ipProvinceAndCitySeekerProvider;
    }

    public IpSeekerProvider getIpDistrictSeekerProvider() {
        return ipDistrictSeekerProvider;
    }

    public void setIpDistrictSeekerProvider(IpSeekerProvider ipDistrictSeekerProvider) {
        this.ipDistrictSeekerProvider = ipDistrictSeekerProvider;
    }

    @Override
    public IpOwnerInfo getIpOwnerInfo(String ip, boolean fillProvince, boolean fillCity, boolean fillDistrict) {

        IpOwnerInfo ipInfo = null;

        if (fillProvince || fillCity || fillDistrict) {
            AssertUtil.assertNotNull(this.ipProvinceAndCitySeekerProvider, "没有提供省市IpSeekerProvider, 请通过 setIpProvinceAndCitySeekerProvider 设置");
            IpSeeker seeker = this.ipProvinceAndCitySeekerProvider.getSeeker();
            if (seeker != null) {
                String[] values = seeker.find(ip.trim());

                if (logger.isDebugEnabled()) {
                    logger.debug("ip --> provinceAndCity, [" + ip + "] = " + Arrays.toString(values));
                }
                ipInfo = IpOwnerInfoUtil.fillByProvinceAndCityResult(new IpOwnerInfo(), values);
            } else {
                logger.warn("IpProvinceAndCitySeekerProvider 还未初始化完成！");
            }
        }

        if (fillDistrict) {
            AssertUtil.assertNotNull(this.ipDistrictSeekerProvider, "没有提供区县IpSeekerProvider, 请通过 setIpDistrictSeekerProvider 设置");

            IpSeeker seeker = this.ipDistrictSeekerProvider.getSeeker();
            if (seeker != null) {
                String[] values = seeker.find(ip.trim());

                if (logger.isDebugEnabled()) {
                    logger.debug("ip --> district, [" + ip + "] = " + Arrays.toString(values));
                }
                ipInfo = IpOwnerInfoUtil.fillByDistrictResult(ipInfo, values);
            } else {
                logger.warn("IpDistrictSeekerProvider 还未初始化完成！");
            }
        }

        return ipInfo;
    }

}
