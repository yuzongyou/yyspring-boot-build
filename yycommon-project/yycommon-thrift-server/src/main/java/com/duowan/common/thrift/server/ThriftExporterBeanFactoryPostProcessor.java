package com.duowan.common.thrift.server;

import com.duowan.common.thrift.server.annotation.ThriftService;
import com.duowan.common.thrift.server.exception.ThriftServiceExportException;
import com.duowan.common.thrift.server.exporter.TThreadPoolServerExporter;
import com.duowan.common.thrift.server.exporter.ThriftServiceExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 17:17
 */
public class ThriftExporterBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        if (registry instanceof DefaultListableBeanFactory) {
            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) registry;

            // ThriftService 类定义
            String[] thriftServiceBeanDefinitionNames = beanFactory.getBeanNamesForAnnotation(ThriftService.class);
            boolean existsThriftService = thriftServiceBeanDefinitionNames != null && thriftServiceBeanDefinitionNames.length > 0;

            if (!existsThriftService) {
                logger.info("Spring Bean 容器中没有使用 @ThriftService 标识的 Thrift 服务，不需要发布Thrift！");
                return;
            }
            // 获取 ThriftServiceExporter 的 Bean 定义
            String[] beanDefinitionNames = beanFactory.getBeanNamesForType(ThriftServiceExporter.class);
            String exporterBeanName = null;

            if (beanDefinitionNames == null || beanDefinitionNames.length < 1) {
                // 如果没有定义的话，默认注册一个 com.duowan.common.thrift.server.exporter.TThreadPoolServerExporter Thrift 服务发布实例
                exporterBeanName = registerDefaultThriftServiceExporterBeanDefinition(beanFactory);
            } else {
                if (beanDefinitionNames.length > 1) {
                    throw new ThriftServiceExportException("一个SpringBean容器中不允许有多个 ThriftServiceExporter Bean 定义实例！");
                }
                exporterBeanName = beanDefinitionNames[0];
            }

            // 注册默认的 Thrift 服务实例搜索Bean
            registerDefaultThriftServiceSearcherBeanDefinition(registry, thriftServiceBeanDefinitionNames);

            // 注册 调度器
            registerThriftServiceExporterSchedulerBeanDefinition(beanFactory, exporterBeanName);

        }

    }

    private void registerDefaultThriftServiceSearcherBeanDefinition(BeanDefinitionRegistry registry, String[] thriftServiceBeanDefinitionNames) {

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(BeanNamesThriftServiceSearcher.class);
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, new HashSet<>(Arrays.asList(thriftServiceBeanDefinitionNames)));

        registry.registerBeanDefinition(BeanNamesThriftServiceSearcher.class.getSimpleName(), beanDefinition);
    }

    private void registerThriftServiceExporterSchedulerBeanDefinition(BeanDefinitionRegistry registry, String exporterBeanName) {

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(ThriftServiceExporterScheduler.class);
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, new RuntimeBeanReference(exporterBeanName));

        registry.registerBeanDefinition(ThriftServiceExporterScheduler.class.getSimpleName(), beanDefinition);
    }

    private String registerDefaultThriftServiceExporterBeanDefinition(BeanDefinitionRegistry registry) {

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(TThreadPoolServerExporter.class);

        String beanName = "defaultThriftServiceExporter";

        logger.info("没有设置 ThriftServiceExporter， 注册默认的 ThriftServiceExporter [" + TThreadPoolServerExporter.class.getName() + "]");

        registry.registerBeanDefinition(beanName, beanDefinition);

        return beanName;

    }

}
