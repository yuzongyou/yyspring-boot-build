package com.duowan.yyspringboot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 12:41
 */
public abstract class AbstractAutoConfiguration implements ApplicationContextAware, EnvironmentAware, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    private Environment environment;

    private BeanDefinitionRegistry registry;

    @Override
    public final void afterPropertiesSet() throws Exception {
        doAutoConfiguration(applicationContext, registry, environment);
    }

    protected void doAutoConfiguration(ApplicationContext applicationContext, BeanDefinitionRegistry registry, Environment environment) {}

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.registry = getBeanDefinitionRegistry();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    /**
     * Get the bean definition registry.
     *
     * @return the BeanDefinitionRegistry if it can be determined
     */
    private BeanDefinitionRegistry getBeanDefinitionRegistry() {
        if (applicationContext == null) {
            throw new IllegalStateException("AppContext not init yet, Cloud not locate BeanDefinitionRegistry");
        }
        if (applicationContext instanceof BeanDefinitionRegistry) {
            return (BeanDefinitionRegistry) applicationContext;
        }
        if (applicationContext instanceof AbstractApplicationContext) {
            return (BeanDefinitionRegistry) ((AbstractApplicationContext) applicationContext)
                    .getBeanFactory();
        }
        throw new IllegalStateException("Could not locate BeanDefinitionRegistry");
    }
}
