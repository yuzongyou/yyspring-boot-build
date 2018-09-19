package com.duowan.common.thrift.client.config;

import com.duowan.common.thrift.client.exception.InvalidPrototolFactoriesException;
import com.duowan.common.thrift.client.exception.TTransportFactoryNotFoundException;
import com.duowan.common.thrift.client.exception.ThriftClientConfigException;
import com.duowan.common.thrift.client.factory.TProtocolFactory;
import com.duowan.common.thrift.client.factory.TTransportFactory;
import com.duowan.common.thrift.client.interceptor.ThriftInterceptor;
import com.duowan.common.thrift.client.loadbalancer.DefaultLoadBalancer;
import com.duowan.common.thrift.client.loadbalancer.LoadBalancer;
import com.duowan.common.thrift.client.monitor.*;
import com.duowan.common.thrift.client.pool.TClientPoolConfig;
import com.duowan.common.thrift.client.pool.TransportPool;
import com.duowan.common.thrift.client.pool.TransportPooledObjectFactory;
import com.duowan.common.thrift.client.servernode.ServerNodeProvider;
import com.duowan.common.thrift.client.util.ThriftUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * TClientConfig
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 20:24
 */
public class TClientConfig {

    private static final MonitorReporter DEFAULT_MONITOR_REPORTER = new LogMonitorReporter();

    private static final NodeMonitor DEFAULT_NODE_MONITOR = new TelnetNodeMonitor();

    private static final TClientPoolConfig DEFAULT_POOL_CONFIG = new TClientPoolConfig();

    /**
     * REQUIRED, 服务发布方，一个端口只能发布一种
     **/
    private final TTransportFactory transportFactory;
    /**
     * REQUIRED, 客户端服务路由，如果服务端以TMultiplexedProcessor发布，那么可以有多个，每一个对应一个具体服务
     * 如果有多个Factory的情况, 不允许出现 router() 为空的情况，并且不允许 router() 有重复的
     **/
    private final List<TProtocolFactory> protocolFactories;

    /**
     * REQUIRED, 负载均衡器，如果没有自定义则使用默认的负载均衡器，如果没有自定义，那么 ServerNodeProvider 不允许为空
     **/
    private final LoadBalancer loadBalancer;

    /**
     * OPTIONAL, default=5000, 连接、读超时时间，单位毫秒
     **/
    private int timeoutMillis = 5000;

    /**
     * OPTIONAL, 连接池配置, 无则使用默认的
     **/
    private TClientPoolConfig poolConfig;
    /**
     * OPTIONAL, 调用拦截器
     **/
    private List<ThriftInterceptor> interceptors;
    /**
     * OPTIONAL, 节点监控对象，默认是使用 telnet host:port 监控
     **/
    private NodeMonitor nodeMonitor;
    /**
     * OPTIONAL, 监控报告接口，默认是直接打印日志
     **/
    private MonitorReporter monitorReporter;
    /**
     * OPTIONAL, 重试次数，默认是重试一次，如果 TProtocolFactory 没有设置则以这个为准
     **/
    private int defaultRetryTimes = 1;

    /**
     * 连接池对象
     **/
    private TransportPool pool;

    /**
     * 开启日志
     **/
    private boolean enabledLogging;

    /**
     * 客户端对象验证器
     **/
    private ClientValidator clientValidator;

    /**
     * ProtocolFactory.router to ProtocolFactory
     **/
    private Map<String, TProtocolFactory> protocolFactoryMap = new HashMap<>();

    public TClientConfig(TTransportFactory transportFactory, TProtocolFactory protocolFactory, LoadBalancer loadBalancer) {
        this(transportFactory, Collections.singletonList(protocolFactory), loadBalancer);
    }

    public TClientConfig(TTransportFactory transportFactory, TProtocolFactory protocolFactory, ServerNodeProvider serverNodeProvider) {
        this(transportFactory, Collections.singletonList(protocolFactory), serverNodeProvider);
    }

    public TClientConfig(TTransportFactory transportFactory, List<TProtocolFactory> protocolFactories, LoadBalancer loadBalancer) {
        this(transportFactory, protocolFactories, null, loadBalancer);
    }

    public TClientConfig(TTransportFactory transportFactory, List<TProtocolFactory> protocolFactories, ServerNodeProvider serverNodeProvider) {
        this(transportFactory, protocolFactories, serverNodeProvider, null);
    }

    private TClientConfig(TTransportFactory transportFactory, List<TProtocolFactory> protocolFactories, ServerNodeProvider nodeProvider, LoadBalancer loadBalancer) {
        this.transportFactory = transportFactory;
        this.protocolFactories = protocolFactories;
        this.protocolFactoryMap = checkAndResolveProtocolFactories(protocolFactories);

        if (null == transportFactory) {
            throw new TTransportFactoryNotFoundException();
        }

        if (loadBalancer == null && nodeProvider == null) {
            throw new ThriftClientConfigException("LoadBalancer and ServerNodeProvider should be provide one!");
        }
        if (loadBalancer != null) {
            this.loadBalancer = loadBalancer;
        } else {
            this.loadBalancer = createDefaultLoadBalancer(nodeProvider);
        }
    }

    /**
     * 创建默认的 LoadBalancer
     *
     * @param serverNodeProvider 返回服务节点提供者
     * @return 返回LoadBalancer
     */
    private LoadBalancer createDefaultLoadBalancer(ServerNodeProvider serverNodeProvider) {
        return new DefaultLoadBalancer(serverNodeProvider);
    }

    /**
     * 检查 TProtocolFactory 列表，如果长度大于 1 则不允许出现重复的 router，并且不能有空的 router
     *
     * @return 返回 router() to TProtocolFactory 对象Map
     */
    private Map<String, TProtocolFactory> checkAndResolveProtocolFactories(List<TProtocolFactory> protocolFactories) {
        if (protocolFactories == null || protocolFactories.isEmpty()) {
            throw new InvalidPrototolFactoriesException("TProtocolFactory 列表不能为空！");
        }

        Map<String, TProtocolFactory> protocolFactoryMap = new HashMap<>(protocolFactories.size());
        // 只有一个的时候，不管 router() 是否为空
        if (protocolFactories.size() == 1) {
            TProtocolFactory factory = protocolFactories.get(0);
            protocolFactoryMap.put(ThriftUtil.fixRouter(factory.router()), factory);
            return protocolFactoryMap;
        }

        // 多个的时候，不允许重复 router，并且不能有空的
        Set<String> existsRouters = new HashSet<>();
        for (TProtocolFactory factory : protocolFactories) {
            String router = ThriftUtil.fixRouter(factory.router());
            if (StringUtils.isBlank(router)) {
                throw new InvalidPrototolFactoriesException("TProtocolFactory列表不允许有空 router 对象！");
            }
            if (existsRouters.contains(router)) {
                throw new InvalidPrototolFactoriesException("TProtocolFactory列表不允许有多个 router=" + router + " 的实例！");
            }
            existsRouters.add(router);
            protocolFactoryMap.put(router.toUpperCase(), factory);
        }
        return protocolFactoryMap;
    }

    public TTransportFactory getTransportFactory() {
        return transportFactory;
    }

    public List<TProtocolFactory> getProtocolFactories() {
        return protocolFactories;
    }

    public TProtocolFactory getProtocolFactoryByRouter(String router) {
        return this.protocolFactoryMap.get(ThriftUtil.fixRouter(router));
    }

    public LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public TClientPoolConfig getPoolConfig() {
        return poolConfig == null ? DEFAULT_POOL_CONFIG : poolConfig;
    }

    public void setPoolConfig(TClientPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public List<ThriftInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<ThriftInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public NodeMonitor getNodeMonitor() {
        return nodeMonitor == null ? DEFAULT_NODE_MONITOR : nodeMonitor;
    }

    public void setNodeMonitor(NodeMonitor nodeMonitor) {
        this.nodeMonitor = nodeMonitor;
    }

    public MonitorReporter getMonitorReporter() {
        return monitorReporter == null ? DEFAULT_MONITOR_REPORTER : monitorReporter;
    }

    public void setMonitorReporter(MonitorReporter monitorReporter) {
        this.monitorReporter = monitorReporter;
    }

    public int getDefaultRetryTimes() {
        return defaultRetryTimes;
    }

    public void setDefaultRetryTimes(int defaultRetryTimes) {
        this.defaultRetryTimes = defaultRetryTimes;
    }

    public boolean isEnabledLogging() {
        return enabledLogging;
    }

    public void setEnabledLogging(boolean enabledLogging) {
        this.enabledLogging = enabledLogging;
    }

    public ClientValidator getClientValidator() {
        return clientValidator;
    }

    public void setClientValidator(ClientValidator clientValidator) {
        this.clientValidator = clientValidator;
    }

    public synchronized TransportPool getPool() {
        if (this.pool == null) {
            if (this.poolConfig == null) {
                this.poolConfig = new TClientPoolConfig();
            }
            this.pool = new TransportPool(new TransportPooledObjectFactory(this), this.poolConfig);
        }
        return this.pool;
    }

    @Override
    public String toString() {
        return "TClientConfig{" +
                "protocolFactories=[" + protocolFactoriesToString(protocolFactories) + "]" +
                ", timeoutMillis=" + timeoutMillis +
                ", enabledLogging=" + enabledLogging +
                '}';
    }

    private String protocolFactoriesToString(List<TProtocolFactory> protocolFactories) {
        StringBuilder builder = new StringBuilder();
        if (null != protocolFactories && !protocolFactories.isEmpty()) {
            for (TProtocolFactory factory : protocolFactories) {
                builder.append(factory.router()).append(",");
            }
            builder.setLength(builder.length() - 1);
        }
        return builder.toString();

    }
}
