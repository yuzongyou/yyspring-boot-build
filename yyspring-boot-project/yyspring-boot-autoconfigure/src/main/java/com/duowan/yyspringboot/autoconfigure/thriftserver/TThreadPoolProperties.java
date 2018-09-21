package com.duowan.yyspringboot.autoconfigure.thriftserver;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 19:18
 */
@ConfigurationProperties(prefix = "yyspring.thrift.exporter.threadpool")
public class TThreadPoolProperties {

    /**
     * 最少工作线程的数量
     */
    private int minWorkerThreads = 5;

    /**
     * 最大工作线程的数量
     */
    private int maxWorkerThreads = Runtime.getRuntime().availableProcessors() * 5;

    /**
     * 线程请求超时时间
     */
    private int requestTimeoutMillis = 5;

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

    public int getRequestTimeoutMillis() {
        return requestTimeoutMillis;
    }

    public void setRequestTimeoutMillis(int requestTimeoutMillis) {
        this.requestTimeoutMillis = requestTimeoutMillis;
    }
}
