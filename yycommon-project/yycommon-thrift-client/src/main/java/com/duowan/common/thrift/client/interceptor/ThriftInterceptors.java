package com.duowan.common.thrift.client.interceptor;

import com.duowan.common.thrift.client.config.ThriftClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
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

        Collections.sort(interceptors, new Comparator<ThriftInterceptor>() {
            @Override
            public int compare(ThriftInterceptor o1, ThriftInterceptor o2) {
                int ret = o1.getOrder() - o2.getOrder();
                return ret > 0 ? 1 : ret == 0 ? 0 : -1;
            }
        });
        this.interceptors = new CopyOnWriteArrayList<>(interceptors);
    }

    @Override
    public void before(Method method, Object[] args, Object target, ThriftClientConfig clientConfig, ThriftServerNode serverNode) throws Throwable {
        if (!needInterceptor) {
            return;
        }
        for (ThriftInterceptor interceptor : interceptors) {
            interceptor.before(method, args, target, clientConfig, serverNode);
        }
    }

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target, ThriftClientConfig clientConfig, ThriftServerNode serverNode) throws Throwable {
        if (!needInterceptor) {
            return;
        }
        for (ThriftInterceptor interceptor : interceptors) {
            interceptor.afterReturning(returnValue, method, args, target, clientConfig, serverNode);
        }
    }

    @Override
    public void afterThrowing(Method method, Object[] args, Object target, ThriftClientConfig clientConfig, ThriftServerNode serverNode, Exception e) throws Throwable {
        if (!needInterceptor) {
            return;
        }
        for (ThriftInterceptor interceptor : interceptors) {
            interceptor.afterThrowing(method, args, target, clientConfig, serverNode, e);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
