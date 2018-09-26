package com.duowan.yyspringboot.autoconfigure.thriftserver;

import com.duowan.common.thrift.server.ThriftExporterBeanFactoryPostProcessor;
import com.duowan.common.thrift.server.exporter.*;
import com.duowan.yyspringboot.autoconfigure.AbstractAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 19:10
 */
@Configuration
@ConditionalOnClass({ThriftServiceExporter.class, ThriftExporterBeanFactoryPostProcessor.class})
@EnableConfigurationProperties({
        THsHaProperties.class,
        TNonblockingProperties.class,
        TThreadPoolProperties.class,
        TThreadedSelectorProperties.class
})
public class ThriftServiceExporterAutoConfiguration extends AbstractAutoConfiguration {

    public ThriftServiceExporterAutoConfiguration(ApplicationContext applicationContext, Environment environment) {
        super(applicationContext, environment);
    }

    @Bean
    @ConditionalOnMissingBean
    public TThreadPoolServerExporter defaultThreadPoolServerExporter(TThreadPoolProperties threadPoolProperties) {
        return createThreadPoolServerExporter(threadPoolProperties);
    }

    @Bean
    @ConditionalOnExpression("'${yyspring.thrift.exporter.type}' eq 'threadpool'")
    @ConditionalOnMissingBean
    public TThreadPoolServerExporter threadPoolServerExporter(TThreadPoolProperties threadPoolProperties) {
        return createThreadPoolServerExporter(threadPoolProperties);
    }

    private TThreadPoolServerExporter createThreadPoolServerExporter(TThreadPoolProperties threadPoolProperties) {
        TThreadPoolServerExporter exporter = new TThreadPoolServerExporter();

        exporter.setMaxWorkerThreads(threadPoolProperties.getMaxWorkerThreads());
        exporter.setMinWorkerThreads(threadPoolProperties.getMinWorkerThreads());
        exporter.setRequestTimeoutMillis(threadPoolProperties.getRequestTimeoutMillis());

        return exporter;
    }

    @Bean
    @ConditionalOnExpression("'${yyspring.thrift.exporter.type}' eq 'hsha'")
    public THsHaServerExporter hsHaServerExporter(THsHaProperties hsHaProperties) {
        THsHaServerExporter exporter = new THsHaServerExporter();
        exporter.setKeepAliveMinute(hsHaProperties.getKeepAliveMinute());
        exporter.setMaxWorkerThreads(hsHaProperties.getMaxWorkerThreads());
        exporter.setMinWorkerThreads(hsHaProperties.getMinWorkerThreads());
        exporter.setWorkerQueueCapacity(hsHaProperties.getWorkerQueueCapacity());
        return exporter;
    }

    @Bean
    @ConditionalOnExpression("'${yyspring.thrift.exporter.type}' eq 'threadedselector'")
    public TThreadedSelectorServerExporter threadedSelectorServerExporter(TThreadedSelectorProperties threadedSelectorProperties) {
        TThreadedSelectorServerExporter exporter = new TThreadedSelectorServerExporter();

        exporter.setAcceptQueueSizePerThread(threadedSelectorProperties.getAcceptQueueSizePerThread());
        exporter.setKeepAliveMinute(threadedSelectorProperties.getKeepAliveMinute());
        exporter.setMaxWorkerThreads(threadedSelectorProperties.getMaxWorkerThreads());
        exporter.setMinWorkerThreads(threadedSelectorProperties.getMinWorkerThreads());
        exporter.setSelectorThreads(threadedSelectorProperties.getSelectorThreads());
        exporter.setWorkerQueueCapacity(threadedSelectorProperties.getWorkerQueueCapacity());

        return exporter;
    }

    @Bean
    @ConditionalOnExpression("'${yyspring.thrift.exporter.type}' eq 'nonblocking'")
    public TNonblockingServerExporter nonblockingServerExporter(TNonblockingProperties nonblockingProperties) {
        return new TNonblockingServerExporter();
    }

    @Bean
    public static ThriftExporterBeanFactoryPostProcessor thriftExporterBeanFactoryPostProcessor() {
        return new ThriftExporterBeanFactoryPostProcessor();
    }
}
