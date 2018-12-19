package com.duowan.common.thrift.server.exporter;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.factory.TProtocolFactory;
import com.duowan.common.thrift.client.factory.TTransportFactory;
import com.duowan.common.thrift.client.factory.ThriftClientFactoryBean;
import com.duowan.common.thrift.client.factory.protocol.TMultiplexedCompactProtocolFactory;
import com.duowan.common.thrift.client.factory.transport.TFastFramedTransportFactory;
import com.duowan.common.thrift.client.servernode.FixedServerNodeDiscovery;
import com.duowan.common.thrift.client.servernode.ServerNodeDiscovery;
import com.duowan.thrift.service.HelloService;
import com.duowan.thrift.service.HiService;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 18:10
 */
public class TThreadPoolServerExporterTest {

    @Test
    public void testClient() throws Exception {
        int port = AbstractThriftServiceExporter.DEFAULT_THRIFT_SERVER_PORT;

        ThriftServerNode serverNode = new ThriftServerNode("127.0.0.1", port);
        ServerNodeDiscovery serverNodeDiscovery = new FixedServerNodeDiscovery(Collections.singletonList(serverNode));
        TTransportFactory transportFactory = new TFastFramedTransportFactory();
        TProtocolFactory protocolFactory = new TMultiplexedCompactProtocolFactory(HiService.class, "HiService");
        TClientConfig clientConfig = new TClientConfig(transportFactory, protocolFactory, serverNodeDiscovery);
        clientConfig.setEnabledLogging(true);
        ThriftClientFactoryBean factoryBean = new ThriftClientFactoryBean(clientConfig, protocolFactory.router(), ClientType.IFACE);
        HiService.Iface hiService = (HiService.Iface) factoryBean.getObject();
        assertEquals(hiService.sayHi("Arvin"), "Hi, Arvin");


        ThriftServerNode serverNode2 = new ThriftServerNode("127.0.0.1", port);
        ServerNodeDiscovery serverNodeDiscovery2 = new FixedServerNodeDiscovery(Collections.singletonList(serverNode2));
        TTransportFactory transportFactory2 = new TFastFramedTransportFactory();
        TProtocolFactory protocolFactory2 = new TMultiplexedCompactProtocolFactory(HelloService.class, "HelloService");
        TClientConfig clientConfig2 = new TClientConfig(transportFactory2, protocolFactory2, serverNodeDiscovery2);
        clientConfig2.setEnabledLogging(true);
        ThriftClientFactoryBean factoryBean2 = new ThriftClientFactoryBean(clientConfig2, protocolFactory2.router(), ClientType.IFACE);
        HelloService.Iface helloService = (HelloService.Iface) factoryBean2.getObject();
        assertEquals(helloService.sayHello("Arvin"), "Hello, Arvin");
    }
}