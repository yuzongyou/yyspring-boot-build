package com.duowan.esb.core;

import com.duowan.yyspring.boot.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Arvin
 */
public abstract class AbstractSpringBootRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected static final Map<Class<?>, Boolean> HAD_IN_INIT_MAP = new HashMap<>();

    protected Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;

        // 初始化
        init(environment);
    }

    protected Environment getEnvironment() {
        return environment;
    }

    /**
     * 初始化
     *
     * @param environment 系统环境
     */
    protected void init(Environment environment) {

    }

    @Override
    public final void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 保证只初始化一次
        Class<?> clazz = getClass();
        synchronized (HAD_IN_INIT_MAP) {
            Boolean hadInInit = HAD_IN_INIT_MAP.get(clazz);
            if (null != hadInInit) {
                return;
            }
            HAD_IN_INIT_MAP.put(clazz, true);
        }

        registerBeanDefinitions(importingClassMetadata, registry, environment);
    }

    protected String getAppProperty(String key, String defaultValue) {
        return AppContext.getAppProperty(key, defaultValue);
    }

    /**
     * 注册 BeanDefinition
     */
    public abstract void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, Environment environment);
}
