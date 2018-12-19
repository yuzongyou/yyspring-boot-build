package com.duowan.common.thrift.client.factory;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.thrift.client.config.TClientConfig;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TTransport;

import java.util.Set;

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
    TProtocol create(TClientConfig clientConfig, TTransport transport);

    /**
     * 获取 Thrift Service Class 类对象
     *
     * @return 返回 ServiceClass
     */
    Class<?> getServiceClass();

    /**
     * 如果服务端以 TMultiplexedProcessor 方式发布的服务，那么这个不能为空
     *
     * @return 路由ID
     */
    String router();

    /**
     * 支持的客户端类型
     *
     * @return 默认是支持 Iface 接口
     */
    Set<ClientType> clientTypes();
}
