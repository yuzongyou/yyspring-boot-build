package com.duowan.yyspringcloud.autoconfigure.eureka;

import com.netflix.appinfo.EurekaInstanceConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
        public EurekaClientInstanceConfigBeanPostProcessor eurekaClientInstanceConfigBeanPostProcessor(Environment environment, EurekaClientProperties properties) {

            String domain = resolveDomain(environment, properties);

            return new EurekaClientInstanceConfigBeanPostProcessor(80, domain);
        }

        private String resolveDomain(Environment environment, EurekaClientProperties properties) {
            if (StringUtils.isNotBlank(properties.getDomain())) {
                return properties.getDomain();
            }

            String domain = environment.getProperty("eureka.instance.hostname");
            if (StringUtils.isBlank(domain)) {
                throw new IllegalArgumentException("注册域名未知，请设置： yyspring.cloud.eureka.client.domain");
            }

            return domain;

        }

    }

}
