package com.duowan.common.thrift.client.pool;

import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.exception.ThriftClientConfigException;
import com.duowan.common.thrift.client.exception.ThriftClientOpenException;
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
public class TransportPooledObjectFactory extends BaseKeyedPooledObjectFactory<ThriftServerNode, PooledTransport> {

    private static final int MAX_SERVER_PORT = 65535;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final TClientConfig clientConfig;

    public TransportPooledObjectFactory(TClientConfig clientConfig) {
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
            transport = clientConfig.getTransportFactory().create(clientConfig, serverNode);
            transport.open();
            if (logger.isInfoEnabled()) {
                logger.info("Open a new transport {}, {}:{}", transport, serverNode.getHost(), serverNode.getPort());
            }
        } catch (Exception e) {
            throw new ThriftClientOpenException("Connect to " + serverNode.getHost() + ":" + serverNode.getPort() + " failed", e);
        }

        return new PooledTransport(serverNode, clientConfig, transport);
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
                isOpen = pooledTransport.validateObject();
            }
            if (!isOpen) {
                pooledTransport.discard();
            }
            return isOpen;
        } catch (Exception e) {
            pooledTransport.discard();
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
                if (logger.isInfoEnabled()) {
                    logger.info("Destroy Thrift Client : {}, node:{}", pooledTransport, serverNode);
                }
                pooledTransport.discard();
            }
            p.markAbandoned();
        }
    }
}
