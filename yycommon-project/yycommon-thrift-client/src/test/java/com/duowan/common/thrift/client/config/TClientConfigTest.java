package com.duowan.common.thrift.client.config;

import com.duowan.common.thrift.client.factory.TProtocolFactory;
import com.duowan.common.thrift.client.factory.TTransportFactory;
import com.duowan.common.thrift.client.servernode.FixedServerNodeDiscovery;
import com.duowan.common.thrift.client.servernode.ServerNodeDiscovery;
import com.duowan.service.TestService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 9:38
 */
public class TClientConfigTest {

    private static TTransportFactory TRANSPORT_FACTORY = new TTransportFactory() {
        @Override
        public TTransport create(TClientConfig clientConfig, ThriftServerNode serverNode) {
            return new TFastFramedTransport(new TSocket(serverNode.getHost(), serverNode.getPort(), clientConfig.getTimeoutMillis()));
            //return new TSocket(serverNode.getHost(), serverNode.getPort());
        }
    };


    private static TProtocolFactory PROTOCOL_FACTORY = new TProtocolFactory() {
        @Override
        public TProtocol create(TClientConfig clientConfig, TTransport transport) {
            return new TMultiplexedProtocol(new TCompactProtocol(transport), router());
            //return new TBinaryProtocol(transport);
            //return new TCompactProtocol(transport);
        }

        @Override
        public Class<?> getServiceClass() {
            return TestService.class;
        }

        @Override
        public String router() {
            return "test";
        }
    };

    @Test
    public void testTClientConfig() {

        ThriftServerNode serverNode = new ThriftServerNode("127.0.0.1", 25000);

        ServerNodeDiscovery serverNodeDiscovery = new FixedServerNodeDiscovery(Collections.singletonList(serverNode));

        TClientConfig clientConfig = new TClientConfig(TRANSPORT_FACTORY, PROTOCOL_FACTORY, serverNodeDiscovery);
        clientConfig.setEnabledLogging(true);

        TProtocolFactory protocolFactory = clientConfig.getProtocolFactoryByRouter("test");

        assertEquals(protocolFactory, PROTOCOL_FACTORY);
    }
}