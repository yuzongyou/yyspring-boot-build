package com.duowan.common.thrift.server.exporter;

import com.duowan.common.thrift.server.annotation.ThriftService;
import com.duowan.common.thrift.server.exception.DuplicateIfaceFoundException;
import com.duowan.common.thrift.server.exception.ThriftIfaceNotFoundException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.ClassUtils;

import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 22:09
 */
public class ThriftServiceWrapper {

    /**
     * 服务类本身
     **/
    @NonNull
    private final Class<?> serviceClass;

    /**
     * Thrift 服务类 Iface、AsyncIface 接口所在类
     **/
    @NonNull
    private final Class<?> thriftServiceClass;

    /**
     * IFace 接口
     **/
    @NonNull
    private final Class<?> ifaceClass;

    /**
     * 服务发布路由
     **/
    @NonNull
    private final String router;

    /**
     * 服务实例对象
     */
    @NonNull
    private Object thriftService;

    public ThriftServiceWrapper(Object thriftService) {
        this(thriftService, null);
    }

    public ThriftServiceWrapper(Object thriftService, String router) {
        this.thriftService = thriftService;
        this.serviceClass = thriftService.getClass();
        this.ifaceClass = findThriftIfaceInterface(this.serviceClass);
        this.thriftServiceClass = this.ifaceClass.getEnclosingClass();
        this.router = deduceServiceRouter(router, this.thriftServiceClass);
    }

    /**
     * 找到Thrift服务实现类实现的 Thrift Iface 接口
     *
     * @param serviceClass 业务服务类
     * @return 返回Iface 接口
     */
    private Class<?> findThriftIfaceInterface(Class<?> serviceClass) {

        Set<Class<?>> interfaceSet = ClassUtils.getAllInterfacesForClassAsSet(serviceClass);

        if (null == interfaceSet || interfaceSet.isEmpty()) {
            throw new ThriftIfaceNotFoundException("类[" + this.serviceClass.getName() + "] 没有实现 Thrift 相关的Iface接口！");
        }

        Class<?> ifaceClass = null;
        for (Class<?> clazz : interfaceSet) {
            if (clazz.getName().endsWith("$Iface") && clazz.getEnclosingClass() != null) {
                if (ifaceClass != null) {
                    throw new DuplicateIfaceFoundException("类[" + this.serviceClass.getName() + "] 不允许实现 多个 Thrift Iface接口！");
                }
                ifaceClass = clazz;
            }
        }

        return ifaceClass;
    }

    private String deduceServiceRouter(String router, Class<?> thriftServiceClass) {
        if (null == router || "".equals(router.trim())) {
            ThriftService thriftService = AnnotationUtils.findAnnotation(serviceClass, ThriftService.class);
            if (null != thriftService) {
                router = thriftService.router();
            }
            if (null == router || "".equals(router.trim())) {
                router = thriftServiceClass.getSimpleName();
            }
            return router.trim();
        } else {
            return router.trim();
        }
    }

    public Class<?> getServiceClass() {
        return serviceClass;
    }

    public Class<?> getThriftServiceClass() {
        return thriftServiceClass;
    }

    public Class<?> getIfaceClass() {
        return ifaceClass;
    }

    public String getRouter() {
        return router;
    }

    public Object getThriftService() {
        return thriftService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ThriftServiceWrapper that = (ThriftServiceWrapper) o;

        if (!router.equals(that.router)) {
            return false;
        }
        return thriftService.equals(that.thriftService);
    }

    @Override
    public int hashCode() {
        int result = router.hashCode();
        result = 31 * result + thriftService.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ThriftServiceWrapper{" +
                "router='" + router + '\'' +
                ", thriftServiceClass=" + thriftServiceClass +
                ", serviceClass=" + serviceClass +
                ", thriftService=" + thriftService +
                '}';
    }
}
