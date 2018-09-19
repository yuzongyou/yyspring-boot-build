package com.duowan.common.thrift.client.factory.transport;

import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TTransport;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 13:05
 */
public class TFastFramedTransportFactory extends TSocketTransportFactory {

    @Override
    public TTransport create(TClientConfig clientConfig, ThriftServerNode serverNode) {
        return new TFastFramedTransport(super.create(clientConfig, serverNode));
    }
}
