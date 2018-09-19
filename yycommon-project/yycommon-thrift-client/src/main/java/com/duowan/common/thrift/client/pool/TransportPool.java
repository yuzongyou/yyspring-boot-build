package com.duowan.common.thrift.client.pool;

import com.duowan.common.thrift.client.config.ThriftServerNode;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 20:53
 */
public class TransportPool extends GenericKeyedObjectPool<ThriftServerNode, PooledTransport> {

    public TransportPool(KeyedPooledObjectFactory<ThriftServerNode, PooledTransport> factory) {
        super(factory);
    }

    public TransportPool(KeyedPooledObjectFactory<ThriftServerNode, PooledTransport> factory, GenericKeyedObjectPoolConfig config) {
        super(factory, config == null ? new TClientPoolConfig() : config);
    }
}
