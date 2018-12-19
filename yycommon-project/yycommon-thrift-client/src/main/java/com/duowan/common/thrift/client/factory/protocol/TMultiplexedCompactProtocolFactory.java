package com.duowan.common.thrift.client.factory.protocol;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.thrift.client.config.TClientConfig;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 13:18
 */
public class TMultiplexedCompactProtocolFactory extends AbstractTProtocolFactory {

    public TMultiplexedCompactProtocolFactory(Class<?> serviceClass, String router) {
        super(serviceClass, null, router);
    }

    public TMultiplexedCompactProtocolFactory(Class<?> serviceClass, Set<ClientType> clientTypes, String router) {
        super(serviceClass, clientTypes, router);
    }

    public TMultiplexedCompactProtocolFactory(Class<?> serviceClass, ClientType clientType, String router) {
        super(serviceClass, clientType == null ? null : new HashSet<>(Collections.singletonList(clientType)), router);
    }

    @Override
    public TProtocol create(TClientConfig clientConfig, TTransport transport) {
        return new TMultiplexedProtocol(new TCompactProtocol(transport), router());
    }
}
