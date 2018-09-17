package com.duowan.common.thrift.client.interceptor;

import com.duowan.common.thrift.client.Ordered;
import com.duowan.common.thrift.client.config.ThriftClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;

import java.lang.reflect.Method;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 17:28
 */
public interface ThriftInterceptor extends Ordered {

    /**
     * 在执行 Thrift 方法之前进行拦截
     *
     * @param method       要调用的方法
     * @param args         调用参数
     * @param target       调用方法的目标对象
     * @param clientConfig 客户端配置
     * @param serverNode   当前使用的服务器节点
     * @throws Throwable 任何异常
     */
    void before(Method method,
                Object[] args,
                Object target,
                ThriftClientConfig clientConfig,
                ThriftServerNode serverNode) throws Throwable;

    /**
     * 在执行 Thrift 方法之后进行拦截， 注意是没有任何异常的情况下才会被调用
     *
     * @param returnValue  返回值对象
     * @param method       要调用的方法
     * @param args         调用参数
     * @param target       调用方法的目标对象
     * @param clientConfig 客户端配置
     * @param serverNode   当前使用的服务器节点
     * @throws Throwable 任何异常
     */
    void afterReturning(Object returnValue,
                        Method method,
                        Object[] args,
                        Object target,
                        ThriftClientConfig clientConfig,
                        ThriftServerNode serverNode) throws Throwable;

    /**
     * 在执行 Thrift 方法之后进行拦截， 注意是没有任何异常的情况下才会被调用
     *
     * @param method       要调用的方法
     * @param args         调用参数
     * @param target       调用方法的目标对象
     * @param clientConfig 客户端配置
     * @param serverNode   当前使用的服务器节点
     * @param e            调用的异常信息
     * @throws Throwable 任何异常
     */
    void afterThrowing(
            Method method,
            Object[] args,
            Object target,
            ThriftClientConfig clientConfig,
            ThriftServerNode serverNode,
            Exception e) throws Throwable;
}
