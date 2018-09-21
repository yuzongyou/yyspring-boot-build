package com.duowan.yyspringboot.autoconfigure.thriftserver;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 19:14
 */
@ConfigurationProperties(prefix = "yyspring.thrift.exporter.hsha")
public class THsHaProperties {

    /**
     * 最少工作线程数量
     */
    private int minWorkerThreads = 5;

    /**
     * 最多工作线程数量
     */
    private int maxWorkerThreads = Runtime.getRuntime().availableProcessors() * 5;

    /**
     * 服务的工作线程队列容量
     */
    private int workerQueueCapacity = 1000;

    /**
     * 线程的存活时间
     */
    private int keepAliveMinute = 1;

    public int getMinWorkerThreads() {
        return minWorkerThreads;
    }

    public void setMinWorkerThreads(int minWorkerThreads) {
        this.minWorkerThreads = minWorkerThreads;
    }

    public int getMaxWorkerThreads() {
        return maxWorkerThreads;
    }

    public void setMaxWorkerThreads(int maxWorkerThreads) {
        this.maxWorkerThreads = maxWorkerThreads;
    }

    public int getWorkerQueueCapacity() {
        return workerQueueCapacity;
    }

    public void setWorkerQueueCapacity(int workerQueueCapacity) {
        this.workerQueueCapacity = workerQueueCapacity;
    }

    public int getKeepAliveMinute() {
        return keepAliveMinute;
    }

    public void setKeepAliveMinute(int keepAliveMinute) {
        this.keepAliveMinute = keepAliveMinute;
    }
}
