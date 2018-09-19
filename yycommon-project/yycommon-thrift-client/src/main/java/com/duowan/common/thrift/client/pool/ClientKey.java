package com.duowan.common.thrift.client.pool;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.thrift.client.util.ThriftUtil;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 15:16
 */
public class ClientKey {

    private String router;

    private ClientType clientType;

    public ClientKey(String router, ClientType clientType) {
        this.router = ThriftUtil.fixRouter(router);
        this.clientType = clientType;
    }

    public String getRouter() {
        return router;
    }

    public ClientType getClientType() {
        return clientType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClientKey clientKey = (ClientKey) o;

        if (router != null ? !router.equals(clientKey.router) : clientKey.router != null) {
            return false;
        }
        return clientType == clientKey.clientType;
    }

    @Override
    public int hashCode() {
        int result = router != null ? router.hashCode() : 0;
        result = 31 * result + clientType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ClientKey{" +
                "router='" + router + '\'' +
                ", clientType=" + clientType +
                '}';
    }
}
