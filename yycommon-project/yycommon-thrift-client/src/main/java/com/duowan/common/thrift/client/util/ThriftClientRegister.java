package com.duowan.common.thrift.client.util;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.factory.TProtocolFactory;
import com.duowan.common.thrift.client.factory.ThriftClientFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * Thrift 客户端注册
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 9:42
 */
public class ThriftClientRegister {

    private static final Logger logger = LoggerFactory.getLogger(ThriftClientRegister.class);

    /**
     * 注册 Thrift Client 相关的Bean， 主要是 TServiceClient，Iface， AsyncIface 等对象
     *
     * @param clientConfigList Thrift Client 配置列表
     * @param registry         注册器
     * @param environment      运行环境
     * @return 返回注册的BeanMap
     */
    public static Map<String, BeanDefinition> registerThriftClientBeanDefinitions(
            List<TClientConfig> clientConfigList,
            BeanDefinitionRegistry registry,
            Environment environment) {

        Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

        if (null == clientConfigList || clientConfigList.isEmpty()) {
            return beanDefinitionMap;
        }

        for (TClientConfig clientConfig : clientConfigList) {
            Map<String, BeanDefinition> subBeanDefinitionMap = registerThriftClientBeanDefinition(clientConfig, registry, environment);
            if (null != subBeanDefinitionMap && !subBeanDefinitionMap.isEmpty()) {
                beanDefinitionMap.putAll(subBeanDefinitionMap);
            }
        }

        return beanDefinitionMap;
    }

    /**
     * 注册单个 Thrift Client 相关的Bean， 主要是 TServiceClient，Iface， AsyncIface 等对象
     *
     * @param clientConfig Thrift Client 配置对象
     * @param registry     注册器
     * @param environment  运行环境
     * @return 返回注册的BeanMap
     */
    public static Map<String, BeanDefinition> registerThriftClientBeanDefinition(TClientConfig clientConfig, BeanDefinitionRegistry registry, Environment environment) {
        Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

        List<TProtocolFactory> protocolFactories = clientConfig.getProtocolFactories();
        for (TProtocolFactory protocolFactory : protocolFactories) {
            String router = protocolFactory.router();
            Set<ClientType> clientTypes = protocolFactory.clientTypes();
            if (clientTypes == null || clientTypes.isEmpty()) {
                clientTypes = new HashSet<>(Collections.singletonList(ClientType.IFACE));
            }

            Class<?> serviceClass = protocolFactory.getServiceClass();

            for (ClientType clientType : clientTypes) {
                String beanName = ThriftUtil.buildDefaultThriftClientBeanName(serviceClass, router, clientType);

                logger.info("注册ThriftBean： " + beanName + ", serviceClass=" + serviceClass.getSimpleName() + ", router=[" + router + "], clientType=" + clientType);

                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
                beanDefinition.setBeanClass(ThriftClientFactoryBean.class);
                beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, clientConfig);
                beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, router);
                beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(2, clientType);

                registry.registerBeanDefinition(beanName, beanDefinition);

            }

        }

        return beanDefinitionMap;
    }
}
