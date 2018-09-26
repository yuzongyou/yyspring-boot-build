package com.duowan.yyspringboot.autoconfigure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 12:41
 */
public abstract class AbstractAutoConfiguration {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final ApplicationContext applicationContext;

    private final Environment environment;

    private final BeanDefinitionRegistry registry;

    protected AbstractAutoConfiguration(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
        this.registry = getBeanDefinitionRegistry();
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
