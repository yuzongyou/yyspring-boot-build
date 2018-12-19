package com.duowan.common.thrift.client.factory;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.exception.ThriftClientException;
import com.duowan.common.thrift.client.exception.ThriftInvalidClientTypeException;
import com.duowan.common.thrift.client.util.ThriftUtil;
import org.apache.thrift.TServiceClient;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import java.lang.reflect.Constructor;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 19:02
 */
public class TClientFactory {

    private TClientFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Object createClientByClientType(TTransport transport, ClientType clientType, TProtocolFactory protocolFactory, TClientConfig clientConfig) {
        try {
            Class<?> serviceClass = protocolFactory.getServiceClass();
            TProtocol protocol = protocolFactory.create(clientConfig, transport);

            // 创建 Client 对象
            if (ClientType.IFACE.equals(clientType)) {
                Class<? extends TServiceClient> clientClass = ThriftUtil.getTServiceClientClass(serviceClass);
                Constructor<? extends TServiceClient> constructor = clientClass.getConstructor(TProtocol.class);
                return constructor.newInstance(protocol);
            } else {
                throw new ThriftInvalidClientTypeException(String.valueOf(clientType));
            }
        } catch (Exception e) {
            throw new ThriftClientException(e);
        }
    }

}
