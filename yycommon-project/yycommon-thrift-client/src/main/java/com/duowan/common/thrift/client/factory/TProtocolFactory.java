package com.duowan.common.thrift.client.factory;

import com.duowan.common.thrift.client.config.ThriftClientConfig;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 18:06
 */
public interface TProtocolFactory {

    /**
     * TProtocol 工厂接口
     *
     * @param clientConfig Thrift 客户端配置
     * @param transport    thrift transport instance
     * @return 返回TProtocol对象
     */
    TProtocol create(ThriftClientConfig clientConfig, TTransport transport);
}
