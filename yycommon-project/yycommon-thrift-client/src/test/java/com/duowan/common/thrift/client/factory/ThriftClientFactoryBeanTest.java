package com.duowan.common.thrift.client.factory;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
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

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 16:28
 */
public class ThriftClientFactoryBeanTest {
    @Test
    public void getObject() throws Exception {

        final String router = "test";
        //final String router = "test-thrift$com.icekredit.rpc.thrift.server.TestService$1.0";

        TTransportFactory transportFactory = new TTransportFactory() {
            @Override
            public TTransport create(TClientConfig clientConfig, ThriftServerNode serverNode) {
                return new TFastFramedTransport(new TSocket(serverNode.getHost(), serverNode.getPort()));
                //return new TSocket(serverNode.getHost(), serverNode.getPort());
            }
        };

        TProtocolFactory protocolFactory = new TProtocolFactory() {

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
                return router;
            }
        };

        ThriftServerNode serverNode = new ThriftServerNode("127.0.0.1", 25000);

        ServerNodeDiscovery serverNodeDiscovery = new FixedServerNodeDiscovery(Collections.singletonList(serverNode));

        TClientConfig clientConfig = new TClientConfig(transportFactory, protocolFactory, serverNodeDiscovery);
        clientConfig.setEnabledLogging(true);

        ThriftClientFactoryBean factoryBean = new ThriftClientFactoryBean(clientConfig, protocolFactory.router(), ClientType.IFACE);

        TestService.Iface client = (TestService.Iface) factoryBean.getObject();

        String info = client.test(1000);

        System.out.println("GetInfo: " + info);

    }

}