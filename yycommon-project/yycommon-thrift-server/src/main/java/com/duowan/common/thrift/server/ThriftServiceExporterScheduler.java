package com.duowan.common.thrift.server;

import com.duowan.common.thrift.server.exporter.ThriftServiceExporter;
import com.duowan.common.thrift.server.exporter.ThriftServiceWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 17:48
 */
public class ThriftServiceExporterScheduler implements ApplicationContextAware, EnvironmentAware, InitializingBean {

    private ThriftServiceExporter exporter;

    private ApplicationContext applicationContext;

    private Environment environment;

    public ThriftServiceExporterScheduler(ThriftServiceExporter exporter) {
        this.exporter = exporter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.exporter.export(lookupThriftServices(), applicationContext, environment);
    }

    private List<ThriftServiceWrapper> lookupThriftServices() {

        List<ThriftServiceSearcher> searchers = findSearchers();

        if (null == searchers || searchers.isEmpty()) {
            return new ArrayList<>();
        }

        List<ThriftServiceWrapper> instanceList = new ArrayList<>();

        for (ThriftServiceSearcher searcher : searchers) {
            List<ThriftServiceWrapper> subInstances = searcher.searchThriftServices(applicationContext, environment);
            if (null != subInstances && !subInstances.isEmpty()) {
                instanceList.addAll(subInstances);
            }
        }

        return instanceList;

    }

    private List<ThriftServiceSearcher> findSearchers() {

        try {
            SelfOnlyThriftServiceSearcher searcher = applicationContext.getBean(SelfOnlyThriftServiceSearcher.class);
            if (null != searcher) {
                return Collections.singletonList(searcher);
            }
        } catch (BeansException ignored) {
        }

        try {

            Map<String, ThriftServiceSearcher> exporterMap = applicationContext.getBeansOfType(ThriftServiceSearcher.class);

            if (exporterMap == null || exporterMap.isEmpty()) {
                return new ArrayList<>();
            }

            return new ArrayList<>(exporterMap.values());
        } catch (BeansException ignored) {
            return new ArrayList<>();
        }

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
