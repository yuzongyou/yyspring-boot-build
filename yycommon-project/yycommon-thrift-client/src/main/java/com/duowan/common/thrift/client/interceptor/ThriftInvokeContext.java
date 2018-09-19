package com.duowan.common.thrift.client.interceptor;

import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.factory.TProtocolFactory;
import com.duowan.common.thrift.client.pool.PooledTransport;
import com.duowan.common.thrift.client.util.TraceUtil;

import java.lang.reflect.Method;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 10:18
 */
public class ThriftInvokeContext {

    /**
     * 跟踪ID
     **/
    private final String traceId;

    /**
     * 执行的方法
     **/
    private final Method method;

    /**
     * 参数数组
     **/
    private final Object[] args;

    /**
     * 客户端配置
     **/
    private final TClientConfig clientConfig;

    /**
     * 当前选择节点
     **/
    private final ThriftServerNode serverNode;

    /**
     * 当前连接池中的对象
     **/
    private final PooledTransport pooledTransport;

    /**
     * 当前使用的 ProtocolFactory 对象
     **/
    private final TProtocolFactory protocolFactory;

    /**
     * 客户端实例对象
     **/
    private final Object client;

    public ThriftInvokeContext(Method method, Object[] args, TClientConfig clientConfig, ThriftServerNode serverNode, PooledTransport pooledTransport, TProtocolFactory protocolFactory, Object client) {
        this.client = client;
        this.traceId = TraceUtil.generateTraceId();
        this.method = method;
        this.args = args;
        this.clientConfig = clientConfig;
        this.serverNode = serverNode;
        this.pooledTransport = pooledTransport;
        this.protocolFactory = protocolFactory;
    }

    public String getTraceId() {
        return traceId;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public TClientConfig getClientConfig() {
        return clientConfig;
    }

    public ThriftServerNode getServerNode() {
        return serverNode;
    }

    public PooledTransport getPooledTransport() {
        return pooledTransport;
    }

    public TProtocolFactory getProtocolFactory() {
        return protocolFactory;
    }

    public Object getClient() {
        return client;
    }

}
