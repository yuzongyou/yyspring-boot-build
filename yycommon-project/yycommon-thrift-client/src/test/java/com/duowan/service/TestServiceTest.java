package com.duowan.service;

import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.factory.TProtocolFactory;
import com.duowan.common.thrift.client.factory.TTransportFactory;
import com.duowan.common.thrift.client.pool.PooledTransport;
import com.duowan.common.thrift.client.pool.TransportPool;
import com.duowan.common.thrift.client.servernode.FixedServerNodeProvider;
import com.duowan.common.thrift.client.servernode.ServerNodeProvider;
import com.duowan.common.thrift.client.util.ThriftUtil;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFastFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

import java.lang.reflect.Constructor;
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

        ServerNodeProvider serverNodeProvider = new FixedServerNodeProvider(Collections.singletonList(serverNode));

        TClientConfig clientConfig = new TClientConfig(transportFactory, protocolFactory, serverNodeProvider);
        clientConfig.setEnabledLogging(true);
        TransportPool pool = clientConfig.getPool();

        TestService.Iface testService = createServiceClient(pool, serverNode,
                clientConfig.getTimeoutMillis(), clientConfig, protocolFactory);

        String info = testService.test(1000);

        System.out.println("GetInfo: " + info);

    }

    public <T extends TServiceClient> T createServiceClient(TransportPool pool, ThriftServerNode serverNode, int connectTimeout, TClientConfig config, TProtocolFactory protocolFactory) throws Exception {

        PooledTransport pooledTransport = pool.borrowObject(serverNode);
        TTransport transport = pooledTransport.getTransport();
        TProtocol protocol = protocolFactory.create(config, transport);
        Class<?> serviceClass = protocolFactory.getServiceClass();
        Class<? extends TServiceClient> clientClass = ThriftUtil.getTServiceClientClass(serviceClass);

        try {
            Constructor<? extends TServiceClient> constructor = clientClass.getConstructor(TProtocol.class);
            T instance = (T) constructor.newInstance(protocol);
            System.err.println("创建[" + clientClass + "] 成功, node=" + serverNode + ", connectTimeout=" + connectTimeout);
            return instance;
        } catch (Exception e) {
            throw new IllegalArgumentException(serviceClass.getName() + " 找不到合法的 TServiceClient 类！");
        }
    }
}
