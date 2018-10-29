package com.duowan.yyspringcloud.autoconfigure.eureka;

import com.netflix.appinfo.EurekaInstanceConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/22 10:27
 */
@Configuration
@ConditionalOnClass({EurekaInstanceConfig.class, EurekaInstanceConfigBean.class})
public class YyEurekaAutoConfiguration {

    @Configuration
    @EnableConfigurationProperties(EurekaClientProperties.class)
    @ConditionalOnProperty(value = "yyspring.cloud.eureka.client.register-by-domain", matchIfMissing = false)
    public static class YyEurekaClientAutoConfiguration {

        @Bean
        public EurekaClientInstanceConfigBeanPostProcessor eurekaClientInstanceConfigBeanPostProcessor(EurekaClientProperties properties) {
            return new EurekaClientInstanceConfigBeanPostProcessor(80, properties.getDomain());
        }

    }

}
