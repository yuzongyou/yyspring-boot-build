package com.duowan.common.thrift.client.interceptor;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 17:36
 */
public abstract class ThriftInterceptorAdapter implements ThriftInterceptor {

    @Override
    public Object before(ThriftInvokeContext invokeContext) {
        return null;
    }

    @Override
    public void afterReturning(Object returnValue, ThriftInvokeContext invokeContext) {

    }

    @Override
    public void afterThrowing(Exception exception, ThriftInvokeContext invokeContext) {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
