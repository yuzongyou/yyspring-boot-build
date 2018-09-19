package com.duowan.common.thrift.client.monitor;

import com.duowan.common.thrift.client.pool.ClientKey;
import com.duowan.common.thrift.client.pool.PooledTransport;

/**
 * Thrift 客户端对象验证
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 15:13
 */
public interface ClientValidator {

    /**
     * 验证指定的对象
     *
     * @param pooledTransport 连接池对象
     * @param clientKey       Thrift客户端对象Key
     * @param client          Thrift客户端对象
     * @return 返回是否通过验证
     */
    boolean validateObject(PooledTransport pooledTransport, ClientKey clientKey, Object client);
}
