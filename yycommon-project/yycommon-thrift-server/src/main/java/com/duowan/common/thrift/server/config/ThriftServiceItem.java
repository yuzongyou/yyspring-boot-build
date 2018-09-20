package com.duowan.common.thrift.server.config;

import com.duowan.common.thrift.server.annotation.ThriftService;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 22:09
 */
public class ThriftServiceItem {

    private final Class<?> serviceClass;

    private final String router;

    /** 服务实例对象 */
    private Object serviceInstance;

    public ThriftServiceItem(Class<?> serviceClass) {
        this(serviceClass, null);
    }

    public ThriftServiceItem(Class<?> serviceClass, String router) {
        this.serviceClass = serviceClass;
        this.router = deduceServiceRouter(router);
    }

    private String deduceServiceRouter(String router) {
        if (null == router || "".equals(router.trim())) {
            ThriftService thriftService = AnnotationUtils.findAnnotation(serviceClass, ThriftService.class);
            if (null != thriftService) {
                router = thriftService.router();
            }
            if (null == router || "".equals(router.trim())) {
                router = serviceClass.getSimpleName();
            }
            return router.trim();
        } else {
            return router.trim();
        }
    }

    public Class<?> getServiceClass() {
        return serviceClass;
    }

    public String getRouter() {
        return router;
    }

    public void setServiceInstance(Object serviceInstance) {
        if (null == serviceInstance) {

        }
        this.serviceInstance = serviceInstance;
    }

    public Object getServiceInstance() {
        return serviceInstance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ThriftServiceItem that = (ThriftServiceItem) o;

        if (serviceClass != null ? !serviceClass.equals(that.serviceClass) : that.serviceClass != null) {
            return false;
        }
        return router != null ? router.equals(that.router) : that.router == null;
    }

    @Override
    public int hashCode() {
        return serviceClass != null ? serviceClass.hashCode() : 0;
    }
}
