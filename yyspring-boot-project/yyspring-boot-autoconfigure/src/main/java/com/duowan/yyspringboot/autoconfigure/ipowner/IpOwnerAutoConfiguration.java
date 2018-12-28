package com.duowan.yyspringboot.autoconfigure.ipowner;

import com.duowan.common.ipowner.IpOwnerServiceImpl;
import com.duowan.common.ipowner.seeker.IpSeekerDistrictProvider;
import com.duowan.common.ipowner.seeker.IpSeekerProvinceAndCityProvider;
import com.duowan.common.utils.StringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/1
 */
@Configuration
@ConditionalOnClass({com.duowan.common.ipowner.IpOwnerService.class})
@EnableConfigurationProperties(IpOwnerProperties.class)
public class IpOwnerAutoConfiguration {

    /**
     * 不存在 IpSeekerDistrictProvider Bean 的时候才会创建
     *
     * @param environment 当前运行环境
     * @param properties  IP归属属性配置
     * @return 返回实例
     */
    @Bean(initMethod = "init")
    @ConditionalOnMissingBean(IpSeekerDistrictProvider.class)
    public IpSeekerDistrictProvider ipSeekerDistrictProvider(IpOwnerProperties properties, Environment environment) {

        IpSeekerDistrictProvider provider = new IpSeekerDistrictProvider();

        provider.setAsyncInit(properties.isDistrictAsyncInit());

        if (StringUtil.isNotBlank(properties.getDistrictIpdatxDownloadUrl())) {
            provider.setIpdatxDownloadUrl(environment.resolvePlaceholders(properties.getDistrictIpdatxDownloadUrl()));
        }

        if (StringUtil.isNotBlank(properties.getDistrictIpdatxFilePath())) {
            provider.setIpdatxFilePath(environment.resolvePlaceholders(properties.getDistrictIpdatxFilePath()));
        }

        return provider;
    }

    /**
     * 不存在 IpSeekerProvinceAndCityProvider Bean 的时候才会创建
     *
     * @param environment 当前运行环境
     * @param properties  IP归属属性配置
     * @return 返回实例
     */
    @Bean(initMethod = "init")
    @ConditionalOnMissingBean(IpSeekerProvinceAndCityProvider.class)
    public IpSeekerProvinceAndCityProvider ipSeekerProvinceAndCityProvider(IpOwnerProperties properties, Environment environment) {
        IpSeekerProvinceAndCityProvider provider = new IpSeekerProvinceAndCityProvider();

        provider.setAsyncInit(properties.isProvinceCityAsyncInit());

        if (StringUtil.isNotBlank(properties.getProvinceCityIpdatxDownloadUrl())) {
            provider.setIpdatxDownloadUrl(environment.resolvePlaceholders(properties.getProvinceCityIpdatxDownloadUrl()));
        }

        if (StringUtil.isNotBlank(properties.getProvinceCityIpdatxFilePath())) {
            provider.setIpdatxFilePath(environment.resolvePlaceholders(properties.getProvinceCityIpdatxFilePath()));
        }

        return provider;
    }

    @Bean
    @ConditionalOnBean({IpSeekerDistrictProvider.class, IpSeekerProvinceAndCityProvider.class})
    public IpOwnerServiceImpl ipOwnerServiceImpl(IpSeekerDistrictProvider ipSeekerDistrictProvider, IpSeekerProvinceAndCityProvider ipSeekerProvinceAndCityProvider) {
        IpOwnerServiceImpl ipOwnerService = new IpOwnerServiceImpl();
        ipOwnerService.setIpDistrictSeekerProvider(ipSeekerDistrictProvider);
        ipOwnerService.setIpProvinceAndCitySeekerProvider(ipSeekerProvinceAndCityProvider);
        return ipOwnerService;
    }

}
