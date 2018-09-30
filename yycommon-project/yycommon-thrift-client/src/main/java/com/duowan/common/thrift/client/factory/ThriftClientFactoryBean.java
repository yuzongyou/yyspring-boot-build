package com.duowan.common.thrift.client.factory;

import com.duowan.common.thrift.client.ClientType;
import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.config.ThriftServerNode;
import com.duowan.common.thrift.client.exception.TProtocolFacotryNotFoundException;
import com.duowan.common.thrift.client.exception.ThriftInvalidClientTypeException;
import com.duowan.common.thrift.client.exception.ThriftInvokeException;
import com.duowan.common.thrift.client.interceptor.ThriftInterceptor;
import com.duowan.common.thrift.client.interceptor.ThriftInterceptors;
import com.duowan.common.thrift.client.interceptor.ThriftInvokeContext;
import com.duowan.common.thrift.client.interceptor.ThriftLoggingInterceptor;
import com.duowan.common.thrift.client.loadbalancer.LoadBalancer;
import com.duowan.common.thrift.client.pool.PooledTransport;
import com.duowan.common.thrift.client.pool.TransportPool;
import com.duowan.common.thrift.client.util.RetryUtil;
import com.duowan.common.thrift.client.util.ThriftUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 生成指定的ThriftClient对象，逻辑如下：
 * 1. 客户端节点负载均衡器
 * 2. 从负载均衡器拿到服务节点，从池中获取指定的TTransport，然后生成指定类型的Bean代理对象
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 15:39
 */
public class ThriftClientFactoryBean implements FactoryBean, InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private Class<?> objectType;

    private ClientType clientType;

    private TClientConfig clientConfig;

    private LoadBalancer lb;

    private TransportPool pool;

    private Class<?> serviceClass;

    private TProtocolFactory protocolFactory;

    private ThriftInterceptor interceptor;

    public ThriftClientFactoryBean(TClientConfig config) {
        this(config, null, ClientType.IFACE);
    }

    public ThriftClientFactoryBean(TClientConfig config, String router, ClientType clientType) {
        this.clientType = clientType;
        if (this.clientType == null) {
            throw new ThriftInvalidClientTypeException();
        }

        this.protocolFactory = config.getProtocolFactoryByRouter(router);
        if (null == protocolFactory) {
            throw new TProtocolFacotryNotFoundException("无法找到router[" + router + "]的 TProtocolFactory 实例对象");
        }

        this.clientConfig = config;
        this.lb = config.getLoadBalancer();
        this.pool = config.getPool();
        this.serviceClass = protocolFactory.getServiceClass();

        List<ThriftInterceptor> interceptors = config.getInterceptors();
        if (config.isEnabledLogging()) {
            interceptors = addLoggingInterceptorToFirst(interceptors);
        }
        if (interceptors != null && !interceptors.isEmpty()) {
            this.interceptor = new ThriftInterceptors(interceptors);
        }

        if (ClientType.IFACE.equals(clientType)) {
            objectType = ThriftUtil.getIfaceClass(this.serviceClass);
        }

    }

    /**
     * 将日志拦截器添加到第一个
     *
     * @param interceptors 拦截器列表
     * @return 返回添加好的列表
     */
    private List<ThriftInterceptor> addLoggingInterceptorToFirst(List<ThriftInterceptor> interceptors) {
        if (interceptors == null || interceptors.isEmpty()) {
            List<ThriftInterceptor> list = new ArrayList<>(1);
            list.add(new ThriftLoggingInterceptor());
            return list;
        }
        List<ThriftInterceptor> interceptorList = new ArrayList<>(interceptors.size() + 1);
        interceptorList.add(new ThriftLoggingInterceptor());
        for (ThriftInterceptor interceptor : interceptors) {
            if (!ThriftLoggingInterceptor.class.equals(interceptor.getClass())) {
                interceptorList.add(interceptor);
            }
        }
        return interceptorList;
    }

    @Override
    public Object getObject() throws Exception {
        return createJdkProxyObject();
    }

    private Object createJdkProxyObject() {

        return Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{getObjectType()}, new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, final Method method, final Object[] args) throws Throwable {

                int tryTimes = clientConfig.getDefaultRetryTimes();
                if (tryTimes < 0) {
                    tryTimes = 0;
                }

                return RetryUtil.execute(tryTimes, 0, true, new RetryUtil.Executor<Object>() {
                    @Override
                    public Object execute(int tryTime) throws Exception {

                        PooledTransport pooledTransport = null;
                        ThriftServerNode serverNode = null;

                        Exception exception = null;
                        Object returnValue = null;
                        ThriftInvokeContext context = null;
                        String tryLabel = tryTime == 1 ? "首次执行 " : "第[" + tryTime + "]次尝试执行 ";

                        try {
                            // 选择一个Key
                            serverNode = lb.chooseServerNode(null);
                            if (serverNode == null) {
                                throw new ThriftInvokeException(tryLabel + "无法获取Thrift服务[" + serviceClass.getName() + "]节点！");
                            }

                            // 从连接池中获取client
                            pooledTransport = pool.borrowObject(serverNode);
                            // 设置最后一次访问时间
                            pooledTransport.setLastAccessTime(System.currentTimeMillis());

                            Object client = pooledTransport.getClient(protocolFactory.router(), clientType);

                            if (interceptor != null) {
                                context = new ThriftInvokeContext(method, args, clientConfig, serverNode, pooledTransport, protocolFactory, client);
                                returnValue = interceptor.before(context);
                                if (null != returnValue) {
                                    return returnValue;
                                }
                            }

                            // 执行方法
                            returnValue = method.invoke(client, args);
                            return returnValue;
                        } catch (Exception e) {
                            // 调用出错的话，直接将该连接丢弃
                            if (null != pooledTransport) {
                                pooledTransport.discard(); // 遗弃
                                String remark = tryLabel + "Thrift 调用异常: method=" + method.getName() + ", args=" + ThriftUtil.argsToString(args);
                                pooledTransport.setRemark(remark);
                                logger.warn(remark + ", client =[" + pooledTransport + "], error = " + e.getMessage(), e);
                            }

                            exception = e;
                            if (interceptor != null && context != null) {
                                interceptor.afterThrowing(exception, context);
                            }

                            throw e;
                        } finally {

                            if (exception == null && interceptor != null && context != null) {
                                if (interceptor != null) {
                                    interceptor.afterReturning(returnValue, context);
                                }
                            }

                            // 释放到连接池中
                            if (null != pooledTransport && serverNode != null) {
                                pool.returnObject(serverNode, pooledTransport);
                            }
                        }
                    }
                });

            }
        });
    }

    @Override
    public Class<?> getObjectType() {
        return objectType;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
