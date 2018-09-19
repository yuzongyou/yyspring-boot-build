package com.duowan.common.thrift.client.factory.protocol;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.thrift.client.config.TClientConfig;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 13:14
 */
public class TCompactPtotocolFactory extends AbstractTProtocolFactory {

    public TCompactPtotocolFactory(Class<?> serviceClass) {
        super(serviceClass, null, null);
    }

    public TCompactPtotocolFactory(Class<?> serviceClass, Set<ClientType> clientTypes) {
        super(serviceClass, clientTypes, null);
    }

    public TCompactPtotocolFactory(Class<?> serviceClass, ClientType clientType) {
        super(serviceClass, clientType == null ? null : new HashSet<>(Collections.singletonList(clientType)), null);
    }

    @Override
    public TProtocol create(TClientConfig clientConfig, TTransport transport) {
        return new TCompactProtocol(transport);
    }
}
