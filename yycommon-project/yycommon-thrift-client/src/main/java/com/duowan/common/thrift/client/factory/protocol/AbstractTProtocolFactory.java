package com.duowan.common.thrift.client.factory.protocol;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.thrift.client.factory.TProtocolFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 13:10
 */
public abstract class AbstractTProtocolFactory implements TProtocolFactory {

    private Set<ClientType> clientTypes = null;

    private String router;

    private Class<?> serviceClass;

    public AbstractTProtocolFactory(Class<?> serviceClass, Set<ClientType> clientTypes, String router) {
        this.serviceClass = serviceClass;
        this.clientTypes = clientTypes;
        this.router = router;
    }

    @Override
    public Class<?> getServiceClass() {
        return serviceClass;
    }

    @Override
    public Set<ClientType> clientTypes() {
        return clientTypes == null || clientTypes.isEmpty()
                ? new HashSet<>(Collections.singletonList(ClientType.IFACE))
                : clientTypes;
    }

    @Override
    public String router() {
        return router;
    }
}
