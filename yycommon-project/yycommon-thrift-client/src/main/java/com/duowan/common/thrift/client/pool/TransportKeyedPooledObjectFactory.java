package com.duowan.common.thrift.client.pool;

import com.duowan.common.thrift.client.config.ThriftClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.exception.ThriftClientConfigException;
import com.duowan.common.thrift.client.exception.ThriftClientOpenException;
import com.duowan.common.thrift.client.monitor.MonitorResult;
import com.duowan.common.thrift.client.monitor.NodeMonitor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 20:55
 */
public class TransportKeyedPooledObjectFactory extends BaseKeyedPooledObjectFactory<ThriftServerNode, PooledTransport> {

    private static final int MAX_SERVER_PORT = 65535;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final ThriftClientConfig clientConfig;

    public TransportKeyedPooledObjectFactory(ThriftClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    @Override
    public PooledTransport create(ThriftServerNode serverNode) throws Exception {
        if (StringUtils.isBlank(serverNode.getHost())) {
            throw new ThriftClientConfigException("Invalid Thrift server, node IP address: " + serverNode.getHost());
        }

        if (serverNode.getPort() <= 0 || serverNode.getPort() > MAX_SERVER_PORT) {
            throw new ThriftClientConfigException("Invalid Thrift server, node port: " + serverNode.getPort());
        }

        TTransport transport = null;
        try {
            transport = clientConfig.getTransportFactory().create(clientConfig, serverNode, clientConfig.getConnectTimeoutMills());
            transport.open();
            if (logger.isDebugEnabled()) {
                logger.debug("[{}] Open a new transport {}, {}:{}", clientConfig.getServiceId(), transport, serverNode.getHost(), serverNode.getPort());
            }
        } catch (Exception e) {
            throw new ThriftClientOpenException("[" + clientConfig.getServiceId() + "]Connect to " + serverNode.getHost() + ":" + serverNode.getPort() + " failed", e);
        }

        return new PooledTransport(serverNode, clientConfig.getServiceClass(), transport);
    }

    @Override
    public PooledObject<PooledTransport> wrap(PooledTransport pooledTransport) {
        return new DefaultPooledObject<>(pooledTransport);
    }

    @Override
    public boolean validateObject(ThriftServerNode serverNode, PooledObject<PooledTransport> p) {

        if (null == p || null == serverNode) {
            logger.warn("PooledObject is already null");
            return false;
        }

        PooledTransport pooledTransport = p.getObject();
        if (null == pooledTransport) {
            logger.warn("PooledTransport is already null");
            return false;
        }

        TTransport transport = pooledTransport.getTransport();
        if (null == transport) {
            logger.warn("TTransport is already null");
            return false;
        }

        try {
            boolean isOpen = transport.isOpen();
            if (isOpen) {
                NodeMonitor monitor = clientConfig.getNodeMonitor();
                if (null != monitor) {
                    MonitorResult monitorResult = monitor.monitor(serverNode, transport);
                    if (MonitorResult.Status.UP.equals(monitorResult.getStatus())) {
                        return true;
                    }
                    throw new IllegalMonitorStateException("对象监控失败：" + transport + ", server=" + serverNode);
                }
            }
            return false;
        } catch (Exception e) {
            logger.error(e.getCause().getMessage());
            return false;
        }
    }

    @Override
    public void destroyObject(ThriftServerNode serverNode, PooledObject<PooledTransport> p) throws Exception {
        if (p != null) {
            PooledTransport pooledTransport = p.getObject();
            if (null != pooledTransport) {
                TTransport transport = pooledTransport.getTransport();
                if (null != transport) {
                    transport.close();
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Destroy Thrift Client : {}, node:{}", pooledTransport, serverNode);
                }
                pooledTransport.discard();
            }
            p.markAbandoned();
        }
    }
}
