package com.duowan.common.thrift.server;

import com.duowan.common.thrift.server.exporter.ThriftServiceExporter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 17:48
 */
public class ThriftServiceExporterScheduler implements ApplicationContextAware, EnvironmentAware, InitializingBean {

    private ThriftServiceExporter exporter;

    private ApplicationContext applicationContext;

    private Environment environment;

    private String[] thriftServiceBeanNames;

    public ThriftServiceExporterScheduler(ThriftServiceExporter exporter, String[] thriftServiceBeanNames) {
        this.exporter = exporter;
        this.thriftServiceBeanNames = thriftServiceBeanNames;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.exporter.export(lookupThriftServices(), applicationContext, environment);
    }

    private List<Object> lookupThriftServices() {

        List<Object> instanceList = new ArrayList<>(thriftServiceBeanNames.length);

        for (String beanName : thriftServiceBeanNames) {
            instanceList.add(applicationContext.getBean(beanName));
        }

        return instanceList;

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
