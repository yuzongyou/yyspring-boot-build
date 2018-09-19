package com.duowan.common.thrift.client;

import com.duowan.common.thrift.client.annotation.ThriftResource;
import com.duowan.common.thrift.client.exception.ThriftClientInjectException;
import com.duowan.common.thrift.client.util.ThriftUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 15:49
 */
public class ThriftResourceBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private static final Logger log = LoggerFactory.getLogger(ThriftResourceBeanPostProcessor.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        Object target = bean;

        target = getRealTargetObject(target);

        Class<?> targetClass = target.getClass();
        final Object targetBean = target;

        injectBeanByField(targetClass, targetBean);

        injectBeanByMethod(targetClass, targetBean);

        return bean;
    }

    private void injectBeanByMethod(Class<?> targetClass, Object targetBean) {
        ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                ThriftResource thriftResource = AnnotationUtils.findAnnotation(method, ThriftResource.class);

                if (thriftResource == null) {
                    return;
                }

                Class<?>[] paramTypes = method.getParameterTypes();
                if (paramTypes == null || paramTypes.length != 1) {
                    return;
                }

                Class<?> fieldType = paramTypes[0];
                Object refBean = lookupThriftBean(thriftResource, fieldType, method.getParameters()[0].getName());
                if (refBean == null) {
                    return;
                }

                ReflectionUtils.makeAccessible(method);
                try {
                    method.invoke(targetBean, refBean);
                } catch (InvocationTargetException e) {
                    throw new ThriftClientInjectException("通过方法注入[" + refBean + "] 失败： " + e.getMessage(), e);
                }
            }
        });

    }

    private void injectBeanByField(Class<?> targetClass, Object targetBean) {

        ReflectionUtils.doWithFields(targetClass, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                ThriftResource thriftResource = AnnotationUtils.findAnnotation(field, ThriftResource.class);
                if (thriftResource == null) {
                    return;
                }

                Class<?> fieldType = field.getType();

                Object refBean = lookupThriftBean(thriftResource, fieldType, field.getName());
                if (refBean == null) {
                    return;
                }

                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, targetBean, refBean);

            }
        });
    }

    private Object lookupThriftBean(ThriftResource thriftResource, Class<?> fieldType, String fieldName) {
        if (!fieldType.isInterface()) {
            throw new ThriftClientInjectException("@ThriftResource 必须作用在接口上面，不允许使用非接口，请使用Iface或者AsyncIface接口！");
        }

        Object refBean = null;

        ClientType clientType = parseClientType(fieldType);
        if (clientType == null) {
            log.warn(fieldName + " 不支持 ThriftResource 注解！");
            return null;
        }

        Class<?> serviceClass = parseServiceClass(fieldType);
        if (null == serviceClass) {
            log.warn(fieldName + " 不支持 ThriftResource 注解, 找不到Thrift对应 ServiceClass！");
            return null;
        }
        String router = thriftResource.router();
        String refBeanName = thriftResource.name();
        if (StringUtils.isEmpty(refBeanName)) {
            refBeanName = ThriftUtil.buildDefaultThriftClientBeanName(serviceClass, router, clientType);
        }
        refBean = tryLookupThriftBean(refBeanName, fieldType, fieldName);

        if (refBean == null) {
            throw new ThriftClientInjectException(fieldName + " 注入Bean失败，无法找到指定的Bean！");
        }
        return refBean;
    }

    private Class<?> parseServiceClass(Class<?> fieldType) {
        return fieldType.getEnclosingClass();
    }

    private Object tryLookupThriftBean(String beanName, Class<?> beanType, String secondBeanName) {

        Object bean = getBeanByName(beanName);
        if (null != bean) {
            return bean;
        }
        bean = getBeanByName(secondBeanName);
        if (null != bean) {
            return bean;
        }
        if (!beanType.isInterface()) {
            beanType = beanType.getInterfaces()[0];
        }

        bean = getBeanByType(beanType);

        if (null != bean) {
            return bean;
        }
        throw new RuntimeException("Bean[" + beanName + "] not found!");
    }

    private Object getBeanByName(String beanName) {
        try {
            return applicationContext.getBean(beanName);
        } catch (BeansException e) {
            return null;
        }
    }

    private Object getBeanByType(Class<?> beanType) {
        try {
            return applicationContext.getBean(beanType);
        } catch (BeansException e) {
            return null;
        }
    }

    private ClientType parseClientType(Class<?> thriftType) {

        if (null == thriftType) {
            return null;
        }

        String className = thriftType.getName();
        if (className.endsWith("$Iface") || className.endsWith("$Client")) {
            return ClientType.IFACE;
        }
        if (className.endsWith("$AsyncIface") || className.endsWith("$AsyncClient")) {
            return ClientType.ASYNC_IFACE;
        }

        return null;
    }

    private Object getRealTargetObject(Object target) {
        if (AopUtils.isJdkDynamicProxy(target)) {
            TargetSource targetSource = ((Advised) target).getTargetSource();
            if (log.isDebugEnabled()) {
                log.debug("Target object {} uses jdk dynamic proxy", target);
            }

            try {
                target = targetSource.getTarget();
            } catch (Exception e) {
                throw new IllegalStateException("Failed to get target bean from " + target, e);
            }
        }

        if (AopUtils.isCglibProxy(target)) {
            TargetSource targetSource = ((Advised) target).getTargetSource();
            if (log.isDebugEnabled()) {
                log.debug("Target object {} uses cglib proxy");
            }

            try {
                target = targetSource.getTarget();
            } catch (Exception e) {
                throw new IllegalStateException("Failed to get target bean from " + target, e);
            }
        }
        return target;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
