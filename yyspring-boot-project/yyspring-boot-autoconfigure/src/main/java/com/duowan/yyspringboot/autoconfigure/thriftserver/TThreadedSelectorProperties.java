package com.duowan.yyspringboot.autoconfigure.thriftserver;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 19:17
 */
@ConfigurationProperties(prefix = "yyspring.thrift.exporter.threadedselector")
public class TThreadedSelectorProperties {

    /**
     * 每个Selector线程可接收连接的阻塞队列大小
     */
    private int acceptQueueSizePerThread = 4;

    /**
     * 接收连接的Selector线程数量
     */
    private int selectorThreads = 2;

    /**
     * 处理任务的最小工作线程池大小
     * 如果为0，默认由Selector线程进行处理
     */
    private int minWorkerThreads = 5;

    /**
     * 最大工作线程的数量
     */
    private int maxWorkerThreads = Runtime.getRuntime().availableProcessors() * 5;

    /**
     * 服务的工作线程队列容量
     */
    private int workerQueueCapacity = 1000;

    /**
     * 工作线程池中的线程的存活时间
     */
    private int keepAliveMinute = 5;

    public int getAcceptQueueSizePerThread() {
        return acceptQueueSizePerThread;
    }

    public void setAcceptQueueSizePerThread(int acceptQueueSizePerThread) {
        this.acceptQueueSizePerThread = acceptQueueSizePerThread;
    }

    public int getSelectorThreads() {
        return selectorThreads;
    }

    public void setSelectorThreads(int selectorThreads) {
        this.selectorThreads = selectorThreads;
    }

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
