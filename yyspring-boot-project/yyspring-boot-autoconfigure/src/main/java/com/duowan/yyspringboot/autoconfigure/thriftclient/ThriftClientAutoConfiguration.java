package com.duowan.yyspringboot.autoconfigure.thriftclient;

import com.duowan.common.thrift.client.ThriftResourceBeanPostProcessor;
import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.factory.ThriftClientFactoryBean;
import com.duowan.common.thrift.client.util.ThriftClientRegister;
import com.duowan.yyspringboot.autoconfigure.AbstractAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 16:58
 */
@Configuration
@ConditionalOnClass({ThriftClientFactoryBean.class, TClientConfig.class, ThriftResourceBeanPostProcessor.class})
public class ThriftClientAutoConfiguration extends AbstractAutoConfiguration {

    private List<TClientConfig> clientConfigList;

    @Autowired(required = false)
    public ThriftClientAutoConfiguration(List<TClientConfig> clientConfigList, ApplicationContext applicationContext, Environment environment) {
        super(applicationContext, environment);
        this.clientConfigList = clientConfigList;

        doAutoConfiguration(applicationContext, getRegistry(), environment);
    }

    /**
     * 自动注入 ThriftResource 的Bean
     *
     * @return 返回自动注入Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public ThriftResourceBeanPostProcessor thriftResourceBeanPostProcessor() {
        return new ThriftResourceBeanPostProcessor();
    }

    protected void doAutoConfiguration(ApplicationContext applicationContext, BeanDefinitionRegistry registry, Environment environment) {
        if (null == clientConfigList || clientConfigList.isEmpty()) {
            logger.warn("引入了 yycommon-thrift-client, 但是沒有配置任何的 Thrift 服务, 请检查是否有缺漏或者多余引入！");
            return;
        }

        // 注册Bean
        ThriftClientRegister.registerThriftClientBeanDefinitions(clientConfigList, registry, environment);
    }
}
