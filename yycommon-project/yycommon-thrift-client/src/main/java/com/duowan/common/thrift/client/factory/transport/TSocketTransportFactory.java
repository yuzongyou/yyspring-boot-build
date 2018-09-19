package com.duowan.common.thrift.client.factory.transport;

import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.factory.TTransportFactory;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 13:01
 */
public class TSocketTransportFactory implements TTransportFactory {

    protected final int DEFAULT_TIMEOUT_MILLIS = 5000;

    @Override
    public TTransport create(TClientConfig clientConfig, ThriftServerNode serverNode) {
        int timeout = clientConfig.getTimeoutMillis();
        if (timeout < 1) {
            timeout = DEFAULT_TIMEOUT_MILLIS;
        }
        return new TSocket(serverNode.getHost(), serverNode.getPort(), timeout);
    }
}
