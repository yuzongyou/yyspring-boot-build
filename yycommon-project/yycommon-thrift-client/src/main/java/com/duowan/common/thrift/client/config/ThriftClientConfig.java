package com.duowan.common.thrift.client.config;

import com.duowan.common.thrift.client.factory.TProtocolFactory;
import com.duowan.common.thrift.client.factory.TTransportFactory;
import com.duowan.common.thrift.client.interceptor.ThriftInterceptors;
import com.duowan.common.thrift.client.monitor.NodeMonitor;

import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 17:19
 */
public class ThriftClientConfig {

    /** 服务唯一标识 */
    private String serviceId;

    /** 服务类 */
    private Class<?> serviceClass;

    /** Thrift 服务提供者节点列表 */
    private List<ThriftServerNode> serverNodes;

    /** 拦截器 */
    private ThriftInterceptors interceptors;

    /** Thrift Transport Factory */
    private TTransportFactory transportFactory;

    /** Thrift TProtocol Factory */
    private TProtocolFactory protocolFactory;

    /** 节点监控示例对象 */
    private NodeMonitor nodeMonitor;
    /** 连接超时时间，默认是 5 秒 */
    private int connectTimeoutMills = 5000;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Class<?> getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(Class<?> serviceClass) {
        this.serviceClass = serviceClass;
    }

    public List<ThriftServerNode> getServerNodes() {
        return serverNodes;
    }

    public void setServerNodes(List<ThriftServerNode> serverNodes) {
        this.serverNodes = serverNodes;
    }

    public ThriftInterceptors getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(ThriftInterceptors interceptors) {
        this.interceptors = interceptors;
    }

    public TTransportFactory getTransportFactory() {
        return transportFactory;
    }

    public void setTransportFactory(TTransportFactory transportFactory) {
        this.transportFactory = transportFactory;
    }

    public TProtocolFactory getProtocolFactory() {
        return protocolFactory;
    }

    public void setProtocolFactory(TProtocolFactory protocolFactory) {
        this.protocolFactory = protocolFactory;
    }

    public NodeMonitor getNodeMonitor() {
        return nodeMonitor;
    }

    public void setNodeMonitor(NodeMonitor nodeMonitor) {
        this.nodeMonitor = nodeMonitor;
    }

    public int getConnectTimeoutMills() {
        return connectTimeoutMills;
    }

    public void setConnectTimeoutMills(int connectTimeoutMills) {
        this.connectTimeoutMills = connectTimeoutMills;
    }
}
