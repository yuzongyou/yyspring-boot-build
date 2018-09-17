package com.duowan.common.thrift.client.factory;

import com.duowan.common.thrift.client.config.ThriftClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.pool.PooledTransport;
import com.duowan.common.thrift.client.pool.ThriftClientKeyedObjectPoolConfig;
import com.duowan.common.thrift.client.pool.TransportGenericKeyedObjectPool;
import com.duowan.common.thrift.client.pool.TransportKeyedPooledObjectFactory;
import com.duowan.common.thrift.client.util.ThriftUtil;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 19:02
 */
public class TClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(TClientFactory.class);

    private final TransportGenericKeyedObjectPool pool;

    public TClientFactory(TransportKeyedPooledObjectFactory objectFactory) {
        this.pool = new TransportGenericKeyedObjectPool(objectFactory);
    }

    public TClientFactory(TransportKeyedPooledObjectFactory objectFactory, ThriftClientKeyedObjectPoolConfig poolConfig) {
        this.pool = new TransportGenericKeyedObjectPool(objectFactory, poolConfig);
    }

    @SuppressWarnings({"unchecked"})
    public <T extends TServiceClient> T createServiceClient(ThriftServerNode serverNode, int connectTimeout, ThriftClientConfig config) throws Exception {

        PooledTransport pooledTransport = pool.borrowObject(serverNode);
        TTransport transport = pooledTransport.getTransport();
        TProtocol protocol = config.getProtocolFactory().create(config, transport);
        Class<?> serviceClass = config.getServiceClass();
        Class<? extends TServiceClient> clientClass = ThriftUtil.getTServiceClass(serviceClass);

        try {
            Constructor<? extends TServiceClient> constructor = clientClass.getConstructor(TProtocol.class);
            T instance = (T) constructor.newInstance(protocol);
            if (logger.isDebugEnabled()) {
                logger.debug("创建[" + clientClass + "] 成功, node=" + serverNode + ", connectTimeout=" + connectTimeout);
            }
            return instance;
        } catch (Exception e) {
            throw new IllegalArgumentException(serviceClass.getName() + " 找不到合法的 TServiceClient 类！");
        }
    }

    public void destroy() {
        this.pool.close();
    }
}
