package com.duowan.common.thrift.server.exporter;

import org.apache.thrift.server.TThreadPoolServer;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 14:55
 */
public class TThreadPoolServerExporter extends AbstractThriftServiceExporter<TThreadPoolServer.Args> {

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

    public TThreadPoolServerExporter() {
        super();
    }

    public TThreadPoolServerExporter(int port) {
        super(port);
    }

    public TThreadPoolServerExporter(boolean joinToParentThread) {
        super(joinToParentThread);
    }

    public TThreadPoolServerExporter(int port, boolean joinToParentThread) {
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

    public int getRequestTimeoutMillis() {
        return requestTimeoutMillis;
    }

    public void setRequestTimeoutMillis(int requestTimeoutMillis) {
        this.requestTimeoutMillis = requestTimeoutMillis;
    }

    @Override
    protected boolean applyCustomArgs(TThreadPoolServer.Args args, List<ThriftServiceWrapper> thriftServiceWrapperList) {
        super.applyCustomArgs(args, thriftServiceWrapperList);

        if (minWorkerThreads > 0) {
            args.minWorkerThreads(minWorkerThreads);
        }
        if (maxWorkerThreads > 0) {
            args.maxWorkerThreads(maxWorkerThreads);
        }

        if (this.requestTimeoutMillis > 0) {
            args.requestTimeout(this.requestTimeoutMillis);
            args.requestTimeoutUnit(TimeUnit.MILLISECONDS);
        }

        return true;
    }
}
