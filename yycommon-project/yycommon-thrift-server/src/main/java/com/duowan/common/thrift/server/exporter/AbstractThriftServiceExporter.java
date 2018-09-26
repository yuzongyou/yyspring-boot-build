package com.duowan.common.thrift.server.exporter;

import com.duowan.common.thrift.server.exception.CanNotDeduceTServerException;
import com.duowan.common.thrift.server.exception.ThriftServiceExportException;
import com.duowan.common.thrift.server.util.ThriftUtil;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.*;
import org.apache.thrift.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/21 8:52
 */
public abstract class AbstractThriftServiceExporter<T extends TServer.AbstractServerArgs<T>> implements ThriftServiceExporter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 默认的 THRIFT 服务监听端口
     */
    public static final int DEFAULT_THRIFT_SERVER_PORT = 12000;

    public static final boolean DEFAULT_JOIN_TO_PARENT_THREAD = true;

    private int port;

    /**
     * 是否加入到主线程中去
     **/
    private boolean joinToParentThread;

    /**
     * Thrift Server class
     **/
    private Class<? extends TServer> serverClass;

    /**
     * 参数类
     **/
    private final Class<T> argsClass;

    private final Class<? extends TServerTransport> serverTransportClass;

    private List<ThriftServiceWrapper> thriftServiceWrapperList;

    private final Constructor<?> argsConstructor;

    private final Constructor<?> serverConstructor;

    private ThriftExportThread listenThread;

    private volatile boolean exported;

    private TServer thriftServer;

    private final Map<Class<?>, Class<? extends TServer>> argsClassToServerClassMap = Collections.unmodifiableMap(new HashMap<Class<?>, Class<? extends TServer>>() {
        {
            put(TServer.Args.class, TSimpleServer.class);
            put(TSimpleServer.Args.class, TSimpleServer.class);
            put(TThreadPoolServer.Args.class, TThreadPoolServer.class);
            put(TNonblockingServer.Args.class, TNonblockingServer.class);
            put(THsHaServer.Args.class, THsHaServer.class);
            put(TThreadedSelectorServer.Args.class, TThreadedSelectorServer.class);
        }
    });

    public AbstractThriftServiceExporter() {
        this(DEFAULT_THRIFT_SERVER_PORT, DEFAULT_JOIN_TO_PARENT_THREAD);
    }

    public AbstractThriftServiceExporter(int port) {
        this(port, DEFAULT_JOIN_TO_PARENT_THREAD);
    }

    public AbstractThriftServiceExporter(boolean joinToParentThread) {
        this(DEFAULT_THRIFT_SERVER_PORT, joinToParentThread);
    }

    @SuppressWarnings("unchecked")
    public AbstractThriftServiceExporter(int port, boolean joinToParentThread) {
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.argsClass = (Class<T>) types[0];
        this.serverClass = deduceServerClass(this.argsClass);
        this.serverTransportClass = deduceServerTransportClass(this.serverClass);
        this.argsConstructor = deduceArgsConstructor(this.serverClass);
        this.serverConstructor = deduceServerConstructor(this.serverClass);
        this.port = port;
        this.joinToParentThread = joinToParentThread;
    }

    protected Constructor<?> deduceServerConstructor(Class<? extends TServer> serverClass) {
        try {
            if (TSimpleServer.class.equals(serverClass)) {
                return TSimpleServer.class.getConstructor(TServer.AbstractServerArgs.class);
            }
            if (TThreadPoolServer.class.equals(serverClass)) {
                return TThreadPoolServer.class.getConstructor(TThreadPoolServer.Args.class);
            }
            if (TNonblockingServer.class.equals(serverClass)) {
                return TNonblockingServer.class.getConstructor(AbstractNonblockingServer.AbstractNonblockingServerArgs.class);
            }
            if (THsHaServer.class.equals(serverClass)) {
                return THsHaServer.class.getConstructor(THsHaServer.Args.class);
            }
            if (TThreadedSelectorServer.class.equals(serverClass)) {
                return TThreadedSelectorServer.class.getConstructor(TThreadedSelectorServer.Args.class);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        throw new CanNotDeduceTServerException("不合法的Thrift Server class : " + serverClass.getName());
    }

    protected Constructor<?> deduceArgsConstructor(Class<? extends TServer> serverClass) {
        try {
            if (TSimpleServer.class.equals(serverClass)) {
                return TSimpleServer.Args.class.getConstructor(TServerTransport.class);
            }
            if (TThreadPoolServer.class.equals(serverClass)) {
                return TThreadPoolServer.Args.class.getConstructor(TServerTransport.class);
            }
            if (TNonblockingServer.class.equals(serverClass)) {
                return TNonblockingServer.Args.class.getConstructor(TNonblockingServerTransport.class);
            }
            if (THsHaServer.class.equals(serverClass)) {
                return THsHaServer.Args.class.getConstructor(TNonblockingServerTransport.class);
            }
            if (TThreadedSelectorServer.class.equals(serverClass)) {
                return TThreadedSelectorServer.Args.class.getConstructor(TNonblockingServerTransport.class);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        throw new CanNotDeduceTServerException("不合法的Thrift Server class : " + serverClass.getName());
    }

    protected Class<? extends TServerTransport> deduceServerTransportClass(Class<? extends TServer> serverClass) {

        if (TSimpleServer.class.equals(serverClass)) {
            return TServerSocket.class;
        }
        if (TThreadPoolServer.class.equals(serverClass)) {
            return TServerSocket.class;
        }
        if (TNonblockingServer.class.equals(serverClass)) {
            return TNonblockingServerSocket.class;
        }
        if (THsHaServer.class.equals(serverClass)) {
            return TNonblockingServerSocket.class;
        }
        if (TThreadedSelectorServer.class.equals(serverClass)) {
            return TNonblockingServerSocket.class;
        }

        throw new CanNotDeduceTServerException("不合法的Thrift Server class : " + serverClass.getName());

    }

    /**
     * <pre>
     * 推断 TServer class 类型, 对应关系如下：
     * TSimpleServer             org.apache.thrift.server.TServer$Args
     * TThreadPoolServer         org.apache.thrift.server.TThreadPoolServer$Args
     * TNonblockingServer        org.apache.thrift.server.TNonblockingServer$Args
     * THsHaServer               org.apache.thrift.server.THsHaServer$Args
     * TThreadedSelectorServer   org.apache.thrift.server.TThreadedSelectorServer$Args
     * </pre>
     *
     * @param argsClass 参数类型
     * @return 返回TServer类
     */
    protected Class<? extends TServer> deduceServerClass(Class<T> argsClass) {
        Class<? extends TServer> clazz = argsClassToServerClassMap.get(argsClass);
        if (null == clazz) {
            throw new CanNotDeduceTServerException("无法推断参数类[" + this.argsClass.getName() + "]对应的 TServer 类！");
        }
        return clazz;
    }

    public Class<? extends TServer> getServerClass() {
        return serverClass;
    }

    public Class<T> getArgsClass() {
        return argsClass;
    }

    public Class<? extends TServerTransport> getServerTransportClass() {
        return serverTransportClass;
    }

    public List<ThriftServiceWrapper> getThriftServiceWrapperList() {
        return thriftServiceWrapperList;
    }

    public Constructor<?> getArgsConstructor() {
        return argsConstructor;
    }

    public Constructor<?> getServerConstructor() {
        return serverConstructor;
    }

    public Map<Class<?>, Class<? extends TServer>> getArgsClassToServerClassMap() {
        return argsClassToServerClassMap;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isJoinToParentThread() {
        return joinToParentThread;
    }

    public void setJoinToParentThread(boolean joinToParentThread) {
        this.joinToParentThread = joinToParentThread;
    }

    public ThriftExportThread getListenThread() {
        return listenThread;
    }

    public boolean isExported() {
        return exported;
    }

    public TServer getThriftServer() {
        return thriftServer;
    }

    @Override
    public synchronized void export(List<ThriftServiceWrapper> thriftServiceWrappers, ApplicationContext applicationContext, Environment environment) throws ThriftServiceExportException {

        String mode = joinToParentThread ? "JOIN_TO_PARENT_THREAD" : "NEW_THREAD";

        if (deduceIsCurrentRunAsWebApp(environment)) {
            joinToParentThread = false;
            logger.info("检测到当前是 Web 运行环境，不允许和服务端是同一个线程,将使用单独的线程启动！");
            checkThriftAndWebServerPort(environment);
        }

        if (exported) {
            logger.info("已经发布过Thrift服务, mode: " + mode +
                    ", port:" + getPort() +
                    ", exporter:" + this +
                    ", services:" + getThriftServiceInfo());
            return;
        }

        try {
            if (thriftServiceWrappers == null || thriftServiceWrappers.isEmpty()) {
                logger.warn("没有提供 Thrift 服务实例对象，不需要发布Thrift服务！");
                return;
            }

            this.thriftServiceWrapperList = thriftServiceWrappers;

            // 检查是否具有重复的 Router
            checkDuplicateRouter(this.thriftServiceWrapperList);

            T args = createArgs();

            // 应用默认的参数
            applyDefaultArgs(args, this.thriftServiceWrapperList);

            // 应用自定义的配置项
            applyCustomArgs(args, this.thriftServiceWrapperList);

            thriftServer = (TServer) serverConstructor.newInstance(args);

            logger.info("成功发布Thrift服务, mode: " + mode +
                    ", port:" + getPort() +
                    ", exporter:" + this +
                    ", services:" + getThriftServiceInfo());

            if (joinToParentThread) {
                thriftServer.serve();
            } else {
                listenThread = new ThriftExportThread(thriftServer);
                listenThread.start();
                exported = true;
            }
        } catch (Exception e) {
            throw new ThriftServiceExportException(e);
        }
    }

    private void checkThriftAndWebServerPort(Environment environment) {
        int thriftPort = getPort();

        int webPort = deduceWebPort(environment);

        if (webPort == thriftPort) {
            throw new ThriftServiceExportException("检测到 Thrift 服务发布的端口和当前 web 运行环境监听端口冲突，请重新规划启动端口！");
        }
    }

    private int deduceWebPort(Environment environment) {
        try {
            String serverPort = environment.getProperty("server.port", "8080");
            return Integer.parseInt(serverPort);
        } catch (NumberFormatException e) {
            return 8080;
        }
    }

    @Override
    public void stop() throws ThriftServiceExportException {
        String mode = joinToParentThread ? "JOIN_TO_PARENT_THREAD" : "NEW_THREAD";
        if (exported) {
            if (joinToParentThread && thriftServer != null) {
                thriftServer.stop();
            } else {
                listenThread.shutdownNow();
            }

            logger.info("成功停止了Thrift服务, mode: " + mode +
                    ", port:" + getPort() +
                    ", exporter:" + this +
                    ", services:" + getThriftServiceInfo());
        } else {
            logger.info("Thrift服务尚未启动，不需要停止, mode: " + mode +
                    ", port:" + getPort() +
                    ", exporter:" + this +
                    ", services:" + getThriftServiceInfo());
        }
    }

    /**
     * 推断当前是否是 web 环境运行
     *
     * @return 返回是否是web环境运行
     */
    private boolean deduceIsCurrentRunAsWebApp(Environment environment) {
        if (environment == null) {
            return false;
        }
        return isServletWebApplication(environment) || isReactiveWebApplication(environment);
    }

    private boolean isReactiveWebApplication(Environment environment) {
        Class<?> reactiveWebEnvClass = lookupClass("org.springframework.boot.web.reactive.context.ConfigurableReactiveWebEnvironment");
        if (null != reactiveWebEnvClass && reactiveWebEnvClass.isAssignableFrom(environment.getClass())) {
            // Servlet
            return true;
        }
        return false;
    }

    private boolean isServletWebApplication(Environment environment) {
        Class<?> webEnvClass = lookupClass("org.springframework.web.context.ConfigurableWebEnvironment");
        if (null != webEnvClass && webEnvClass.isAssignableFrom(environment.getClass())) {
            // Servlet
            return true;
        }
        return false;
    }

    private Class<?> lookupClass(String className) {
        try {
            return ClassUtils.forName(className, getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private String getThriftServiceInfo() {
        List<ThriftServiceWrapper> wrappers = getThriftServiceWrapperList();
        StringBuilder builder = new StringBuilder("[");
        for (ThriftServiceWrapper wrapper : wrappers) {
            builder.append(wrapper.toString()).append(",");
        }
        builder.setLength(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }

    protected void applyDefaultArgs(T args, List<ThriftServiceWrapper> thriftServiceWrapperList) {
        args.transportFactory(new TFramedTransport.Factory());
        args.protocolFactory(new TCompactProtocol.Factory());

        TMultiplexedProcessor multiplexedProcessor = new TMultiplexedProcessor();

        // 添加服务
        for (ThriftServiceWrapper wrapper : thriftServiceWrapperList) {
            Object service = wrapper.getThriftService();
            Class<?> thriftServiceClass = wrapper.getThriftServiceClass();
            TProcessor processor = ThriftUtil.createThriftServiceProcessor(thriftServiceClass, wrapper.getIfaceClass(), service);
            multiplexedProcessor.registerProcessor(wrapper.getRouter(), processor);
            logger.info("注册Thrift服务到 [LOCALHOST:" + getPort() + "/" + wrapper.getRouter() + "] by service [" + service + "] for thrift class [" + thriftServiceClass.getName() + "]");
        }

        // 同时发布多个
        args.processor(multiplexedProcessor);
    }

    /**
     * 创建参数对象
     *
     * @return 返回参数对象
     */
    @SuppressWarnings("unchecked")
    protected T createArgs() throws TTransportException {

        TServerTransport serverTransport = createServerTransport(getPort());

        try {
            return (T) argsConstructor.newInstance(serverTransport);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected TServerTransport createServerTransport(int port) throws TTransportException {
        if (this.serverTransportClass == TServerSocket.class) {
            return new TServerSocket(port);
        }

        return new TNonblockingServerSocket(port);
    }

    private void checkDuplicateRouter(List<ThriftServiceWrapper> itemList) {
        Set<String> existsRouters = new HashSet<>();
        for (ThriftServiceWrapper wrapper : itemList) {
            if (existsRouters.contains(wrapper.getRouter())) {
                throw new ThriftServiceExportException("具有相同的 Router[" + wrapper.getRouter() + "]，请检查是否编码重复！");
            }
            existsRouters.add(wrapper.getRouter());
        }
    }

    protected boolean applyCustomArgs(T args, List<ThriftServiceWrapper> thriftServiceWrapperList) {
        return false;
    }
}
