package com.duowan.common.thrift.server.exporter;

import org.apache.thrift.server.THsHaServer;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 14:54
 */
public class THsHaServerExporter extends AbstractThriftServiceExporter<THsHaServer.Args> {

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

    public THsHaServerExporter() {
        super();
    }

    public THsHaServerExporter(int port) {
        super(port);
    }

    public THsHaServerExporter(boolean joinToParentThread) {
        super(joinToParentThread);
    }

    public THsHaServerExporter(int port, boolean joinToParentThread) {
        super(port, joinToParentThread);
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

    @Override
    protected boolean applyCustomArgs(THsHaServer.Args args, List<ThriftServiceWrapper> thriftServiceWrapperList) {
        super.applyCustomArgs(args, thriftServiceWrapperList);

        if (minWorkerThreads > 0) {
            args.minWorkerThreads(minWorkerThreads);
        }

        if (maxWorkerThreads > 0) {
            args.maxWorkerThreads(maxWorkerThreads);
        }

        args.executorService(new ThreadPoolExecutor(
                args.getMinWorkerThreads(),
                maxWorkerThreads,
                keepAliveMinute,
                TimeUnit.MINUTES,
                new LinkedBlockingDeque<>(workerQueueCapacity)));

        return true;
    }
}
