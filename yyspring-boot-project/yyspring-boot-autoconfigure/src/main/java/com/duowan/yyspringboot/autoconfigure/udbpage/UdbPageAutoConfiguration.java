package com.duowan.yyspringboot.autoconfigure.udbpage;

import com.duowan.udb.sdk.UdbOauth;
import com.duowan.yyspringboot.autoconfigure.udbpage.parameter.*;
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
 * @since 2018/9/29 18:40
 */
@Configuration
@ConditionalOnClass({UdbOauth.class})
@EnableConfigurationProperties({UdbPageProperties.class})
@AutoConfigureBefore({YyWebMvcArgumentResolverAutoConfiguration.class})
public class UdbPageAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    public UdbLoginedYyuidArgumentResolver udbLoginedYyuidArgumentResolver(UdbPageProperties udbPageProperties) {
        return new UdbLoginedYyuidArgumentResolver(udbPageProperties.getUdbAppId(), udbPageProperties.getUdbAppKey());
    }

    @Bean
    @ConditionalOnMissingBean
    public UdbLoginedPassportArgumentResolver udbLoginedPassportArgumentResolver(UdbPageProperties udbPageProperties) {
        return new UdbLoginedPassportArgumentResolver(udbPageProperties.getUdbAppId(), udbPageProperties.getUdbAppKey());
    }

    @Bean
    @ConditionalOnMissingBean
    public UdbLoginedStatusArgumentResolver udbLoginedStatusArgumentResolver(UdbPageProperties udbPageProperties) {
        return new UdbLoginedStatusArgumentResolver(udbPageProperties.getUdbAppId(), udbPageProperties.getUdbAppKey());
    }

    @Bean
    @ConditionalOnMissingBean
    public DecryptPartnerInfoArgumentResolver decryptPartnerInfoArgumentResolver(UdbPageProperties udbPageProperties) {
        return new DecryptPartnerInfoArgumentResolver(udbPageProperties.getUdbAppId(), udbPageProperties.getPartnerInfoLookupScopes());
    }

    @Bean
    @ConditionalOnMissingBean
    public DecryptOauthArgumentResolver decryptOauthArgumentResolver(UdbPageProperties udbPageProperties) {
        return new DecryptOauthArgumentResolver(udbPageProperties.getUdbAppId(), udbPageProperties.getThirdCookieLookupScopes());
    }
}
