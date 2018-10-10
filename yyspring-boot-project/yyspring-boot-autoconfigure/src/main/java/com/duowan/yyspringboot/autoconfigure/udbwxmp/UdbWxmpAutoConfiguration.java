package com.duowan.yyspringboot.autoconfigure.udbwxmp;

import com.duowan.udb.sdk.UdbClient;
import com.duowan.udb.sdk.UdbOauth;
import com.duowan.wxmpsdk.client.WxmpClient;
import com.duowan.yyspringboot.autoconfigure.udbwxmp.parameter.UdbWxmpSessionKeyArgumentResolver;
import com.duowan.yyspringboot.autoconfigure.web.YyWebMvcArgumentResolverAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/8 16:48
 */
@Configuration
@ConditionalOnClass({UdbOauth.class, WxmpClient.class, UdbClient.class})
@EnableConfigurationProperties({UdbWxmpProperties.class})
@AutoConfigureBefore({YyWebMvcArgumentResolverAutoConfiguration.class})
public class UdbWxmpAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public UdbWxmpSessionKeyArgumentResolver wxmpSessionKeyArgumentResolver(UdbWxmpProperties udbWxmpProperties) {
        return new UdbWxmpSessionKeyArgumentResolver(udbWxmpProperties.getUdbAppId(), udbWxmpProperties.getWxmpLookupScopes());
    }

}

