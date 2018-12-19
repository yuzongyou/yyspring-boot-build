package com.duowan.common.thrift.client.interceptor;

import com.duowan.common.thrift.client.Ordered;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 17:28
 */
public interface ThriftInterceptor extends Ordered {

    /**
     * 在执行 Thrift 方法之前进行拦截
     *
     * @param invokeContext 执行上下文
     * @return 结果对象，如果不为null则直接返回结果
     */
    Object before(ThriftInvokeContext invokeContext);

    /**
     * 在执行 Thrift 方法之后进行拦截， 注意是没有任何异常的情况下才会被调用
     *
     * @param returnValue   返回值
     * @param invokeContext 执行上下文
     */
    void afterReturning(Object returnValue, ThriftInvokeContext invokeContext);

    /**
     * 在执行 Thrift 方法之后进行拦截， 注意是没有任何异常的情况下才会被调用
     *
     * @param exception     运行异常
     * @param invokeContext 执行上下文
     */
    void afterThrowing(Exception exception, ThriftInvokeContext invokeContext);
}
