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
import com.duowan.common.thrift.server.exporter.TSimpleServerExporter;
import com.duowan.common.utils.JsonUtil;
import com.duowan.thrift.service.HelloService;
import com.duowan.thrift.service.HiService;
import com.duowan.thrift.service.impl.HelloServiceImpl;
import com.duowan.thrift.service.impl.HiServiceImpl;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.server.*;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;
import org.springframework.util.ClassUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 9:41
 */
public class TSimpleServerExporterTest {

    @Test
    public void testInstance() {
        new TSimpleServerExporter();

        System.out.println(TSimpleServer.Args.class.getName());
        System.out.println(TThreadPoolServer.Args.class.getName());
        System.out.println(TNonblockingServer.Args.class.getName());
        System.out.println(THsHaServer.Args.class.getName());
        System.out.println(TThreadedSelectorServer.Args.class.getName());


        HelloService.Iface helloService = new HelloServiceImpl();

        Set<Class<?>> classes = ClassUtils.getAllInterfacesForClassAsSet(helloService.getClass());

        System.out.println(JsonUtil.toPrettyJson(classes));

    }

    public int randomPort() {
        return new Random(System.currentTimeMillis()).nextInt(28000) + 12000;
    }

    private final int DEFAULT_PORT = 25000;

    @Test
    public void testJustExport() {
        int port = DEFAULT_PORT;

        TSimpleServerExporter exporter = new TSimpleServerExporter(port, true);
        exporter.export(Arrays.asList(
                new HelloServiceImpl(),
                new HiServiceImpl()
        ), null, null);

    }

    @Test
    public void testClient2() throws Exception {

        int port = DEFAULT_PORT;

        TSocket socket1 = new TSocket("127.0.0.1", port);
        TTransport transport = new TFastFramedTransport(socket1);
        transport.open();
        TMultiplexedProtocol protocol = new TMultiplexedProtocol(new TCompactProtocol(transport), "HiService");
        HiService.Iface hiService = new HiService.Client(protocol);
        System.out.println(hiService.sayHi("Arvin"));
        System.out.println(hiService.sayHi("Arvin"));
        System.out.println(hiService.sayHi("Arvin"));
//        transport.close();

//        transport = new TFastFramedTransport(new TSocket("127.0.0.1", port));
//        transport.open();
        protocol = new TMultiplexedProtocol(new TCompactProtocol(transport), "HiService");
        hiService = new HiService.Client(protocol);
        System.out.println(hiService.sayHi("Arvin"));
//        transport.close();


        TSocket socket2 = new TSocket("127.0.0.1", port);
        TTransport transport2 = new TFastFramedTransport(socket2);
        transport2.open();

        System.out.println(socket1.getSocket());
        System.out.println(socket2.getSocket());

        TMultiplexedProtocol protocol2 = new TMultiplexedProtocol(new TCompactProtocol(transport2), "HelloService");
        HelloService.Iface helloService = new HelloService.Client(protocol2);

        System.out.println(helloService.sayHello("Arvin"));


//
//
//        final CountDownLatch latch = new CountDownLatch(1000);
//        ExecutorService executorService = Executors.newFixedThreadPool(20);
//        int count = 0;
//        while (count < 1000) {
//            executorService.execute(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        assertEquals(hiService.sayHi("Arvin"), "Hi, Arvin");
//                        assertEquals(helloService.sayHello("Arvin"), "Hello, Arvin");
//                    } catch (TException e) {
//                    } finally {
//                        latch.countDown();
//                    }
//                }
//            });
//            count++;
//        }
//
//        latch.await();
//
//        System.out.println("Finished");


    }

    @Test
    public void testClient4() throws Exception {
        int port = DEFAULT_PORT;

        ThriftServerNode serverNode = new ThriftServerNode("127.0.0.1", port);
        ServerNodeDiscovery serverNodeDiscovery = new FixedServerNodeDiscovery(Collections.singletonList(serverNode));
        TTransportFactory transportFactory = new TFastFramedTransportFactory();
        TProtocolFactory hiProtocolFactory = new TMultiplexedCompactProtocolFactory(HiService.class, "HiService");
        TProtocolFactory helloProtocolFactory = new TMultiplexedCompactProtocolFactory(HelloService.class, "HelloService");
        TClientConfig clientConfig = new TClientConfig(transportFactory, Arrays.asList(
                hiProtocolFactory,
                helloProtocolFactory
        ), serverNodeDiscovery);


        clientConfig.setEnabledLogging(true);
        ThriftClientFactoryBean hiFactoryBean = new ThriftClientFactoryBean(clientConfig, hiProtocolFactory.router(), ClientType.IFACE);
        HiService.Iface hiService = (HiService.Iface) hiFactoryBean.getObject();

        ThriftClientFactoryBean helloFactoryBean = new ThriftClientFactoryBean(clientConfig, helloProtocolFactory.router(), ClientType.IFACE);
        HelloService.Iface helloService = (HelloService.Iface) helloFactoryBean.getObject();

        final CountDownLatch latch = new CountDownLatch(1000);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        int count = 0;
        while (count < 1000) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        assertEquals(hiService.sayHi("Arvin"), "Hi, Arvin");
                        assertEquals(helloService.sayHello("Arvin"), "Hello, Arvin");
                    } catch (TException e) {
                    } finally {
                        latch.countDown();
                    }
                }
            });
            count++;
        }

        latch.await();

        System.out.println("Finished");


    }

    @Test
    public void testClient3() throws Exception {
        int port = DEFAULT_PORT;


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

    @Test
    public void testClient() throws Exception {
        int port = DEFAULT_PORT;
        testHiServiceClient(port, "HiService");
        testHelloServiceClient(port, "HelloService");
    }

    @Test
    public void testPublish() throws Exception {

        int port = randomPort();

        TSimpleServerExporter exporter = new TSimpleServerExporter(port, false);
        exporter.export(Arrays.asList(
                new HelloServiceImpl(),
                new HiServiceImpl()
        ), null, null);

        Thread.sleep(1000);

        testHiServiceClient(port, "HiService");
        testHelloServiceClient(port, "HelloService");

        exporter.stop();
    }

    private void testHiServiceClient(int port, String router) throws Exception {

        ThriftServerNode serverNode = new ThriftServerNode("127.0.0.1", port);

        ServerNodeDiscovery serverNodeDiscovery = new FixedServerNodeDiscovery(Collections.singletonList(serverNode));

        TTransportFactory transportFactory = new TFastFramedTransportFactory();

        TProtocolFactory protocolFactory = new TMultiplexedCompactProtocolFactory(HiService.class, router);

        TClientConfig clientConfig = new TClientConfig(transportFactory, protocolFactory, serverNodeDiscovery);
        clientConfig.setEnabledLogging(true);

        ThriftClientFactoryBean factoryBean = new ThriftClientFactoryBean(clientConfig, protocolFactory.router(), ClientType.IFACE);

        HiService.Iface hiService = (HiService.Iface) factoryBean.getObject();

        assertEquals(hiService.sayHi("Arvin"), "Hi, Arvin");

    }

    private void testHelloServiceClient(int port, String router) throws Exception {

        ThriftServerNode serverNode = new ThriftServerNode("127.0.0.1", port);

        ServerNodeDiscovery serverNodeDiscovery = new FixedServerNodeDiscovery(Collections.singletonList(serverNode));

        TTransportFactory transportFactory = new TFastFramedTransportFactory();

        TProtocolFactory protocolFactory = new TMultiplexedCompactProtocolFactory(HelloService.class, router);

        TClientConfig clientConfig = new TClientConfig(transportFactory, protocolFactory, serverNodeDiscovery);
        clientConfig.setEnabledLogging(true);

        ThriftClientFactoryBean factoryBean = new ThriftClientFactoryBean(clientConfig, protocolFactory.router(), ClientType.IFACE);

        HelloService.Iface helloService = (HelloService.Iface) factoryBean.getObject();

        assertEquals(helloService.sayHello("Arvin"), "Hello, Arvin");

    }
}