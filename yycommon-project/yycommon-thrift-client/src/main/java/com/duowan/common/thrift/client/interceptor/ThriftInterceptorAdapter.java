package com.duowan.common.thrift.client.interceptor;

import com.duowan.common.thrift.client.config.ThriftClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;

import java.lang.reflect.Method;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 17:36
 */
public abstract class ThriftInterceptorAdapter implements ThriftInterceptor {

    @Override
    public void before(Method method, Object[] args, Object target, ThriftClientConfig clientConfig, ThriftServerNode serverNode) throws Throwable {

    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target, ThriftClientConfig clientConfig, ThriftServerNode serverNode) throws Throwable {

    }

    @Override
    public void afterThrowing(Method method, Object[] args, Object target, ThriftClientConfig clientConfig, ThriftServerNode serverNode, Exception e) throws Throwable {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
