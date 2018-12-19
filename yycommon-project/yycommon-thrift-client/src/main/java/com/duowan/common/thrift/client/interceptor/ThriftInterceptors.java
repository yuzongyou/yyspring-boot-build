package com.duowan.common.thrift.client.interceptor;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 17:44
 */
public class ThriftInterceptors implements ThriftInterceptor {

    private List<ThriftInterceptor> interceptors;

    private boolean needInterceptor = true;

    public ThriftInterceptors(List<ThriftInterceptor> interceptors) {

        if (interceptors == null || interceptors.isEmpty()) {
            this.needInterceptor = false;
        }

        Collections.sort(interceptors, (o1, o2) -> {
            int ret = o1.getOrder() - o2.getOrder();
            return Integer.compare(ret, 0);
        });
        this.interceptors = new CopyOnWriteArrayList<>(interceptors);
    }

    @Override
    public Object before(ThriftInvokeContext invokeContext) {
        if (!needInterceptor) {
            return null;
        }
        for (ThriftInterceptor interceptor : interceptors) {
            Object returnValue = interceptor.before(invokeContext);
            if (null != returnValue) {
                return returnValue;
            }
        }
        return null;
    }

    @Override
    public void afterReturning(Object returnValue, ThriftInvokeContext invokeContext) {
        if (!needInterceptor) {
            return;
        }
        for (ThriftInterceptor interceptor : interceptors) {
            interceptor.afterReturning(returnValue, invokeContext);
        }
    }

    @Override
    public void afterThrowing(Exception exception, ThriftInvokeContext invokeContext) {
        if (!needInterceptor) {
            return;
        }
        for (ThriftInterceptor interceptor : interceptors) {
            interceptor.afterThrowing(exception, invokeContext);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
