package com.duowan.service;

import com.duowan.common.thrift.client.config.ThriftClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.factory.TClientFactory;
import com.duowan.common.thrift.client.factory.TProtocolFactory;
import com.duowan.common.thrift.client.factory.TTransportFactory;
import com.duowan.common.thrift.client.pool.TransportKeyedPooledObjectFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

import java.util.Collections;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 21:31
 */
public class TestServiceTest {

    @Test
    public void testSimple() throws Exception {

        ThriftServerNode serverNode = new ThriftServerNode("127.0.0.1", 25000);

        TTransportFactory transportFactory = new TTransportFactory() {
            @Override
            public TTransport create(ThriftClientConfig clientConfig, ThriftServerNode serverNode, int connectTimeout) {
                return new TFastFramedTransport(new TSocket(serverNode.getHost(), serverNode.getPort()));
                //return new TSocket(serverNode.getHost(), serverNode.getPort());
            }
        };

        TProtocolFactory protocolFactory = new TProtocolFactory() {
            @Override
            public TProtocol create(ThriftClientConfig clientConfig, TTransport transport) {
                //return new TMultiplexedProtocol(new TCompactProtocol(transport), "test-thrift$com.icekredit.rpc.thrift.server.TestService$1.0");
                return new TMultiplexedProtocol(new TCompactProtocol(transport), "test");
                //return new TBinaryProtocol(transport);
            }
        };

        ThriftClientConfig clientConfig = new ThriftClientConfig();
        clientConfig.setServiceId("test");
        clientConfig.setTransportFactory(transportFactory);
        clientConfig.setProtocolFactory(protocolFactory);
        clientConfig.setServerNodes(Collections.singletonList(serverNode));
        clientConfig.setServiceClass(TestService.class);

        TransportKeyedPooledObjectFactory pooledObjectFactory = new TransportKeyedPooledObjectFactory(clientConfig);
        TClientFactory factory = new TClientFactory(pooledObjectFactory);

        TestService.Iface testService = factory.createServiceClient(serverNode, clientConfig.getConnectTimeoutMills(), clientConfig);

        String info = testService.test(1000);

        System.out.println("GetInfo: " + info);

        factory.destroy();

    }
}
