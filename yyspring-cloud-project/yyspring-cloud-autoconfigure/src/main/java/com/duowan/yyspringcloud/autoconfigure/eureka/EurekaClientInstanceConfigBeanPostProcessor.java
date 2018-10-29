package com.duowan.yyspringcloud.autoconfigure.eureka;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/22 10:04
 */
public class EurekaClientInstanceConfigBeanPostProcessor implements BeanPostProcessor {

    private final int nonSecurePort;
    private final String hostname;

    public EurekaClientInstanceConfigBeanPostProcessor(int nonSecurePort, String hostname) {
        this.nonSecurePort = nonSecurePort;
        this.hostname = hostname;
        if (StringUtils.isBlank(hostname)) {
            throw new IllegalArgumentException("必须填写注册到Eureka的服务域名");
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if(bean instanceof EurekaInstanceConfigBean) {
            EurekaInstanceConfigBean configBean = (EurekaInstanceConfigBean) bean;
            configBean.setNonSecurePort(nonSecurePort);
            configBean.setHostname(hostname);
        }

        return bean;
    }
}
