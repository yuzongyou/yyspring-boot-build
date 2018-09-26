package com.duowan.yyspringboot.autoconfigure;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.NoSuchElementException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/26 15:34
 */
public class SpringApplicationRunListenerAdapter implements SpringApplicationRunListener, Ordered {

    private final SpringApplication application;

    private final String[] args;

    private ConfigurableEnvironment environment;

    public SpringApplicationRunListenerAdapter(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    public SpringApplication getApplication() {
        return application;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public void starting() {

    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    private BeanDefinitionRegistry registry;

    @Override
    public final void contextPrepared(ConfigurableApplicationContext context) {
        if (context instanceof BeanDefinitionRegistry) {
            this.registry = (BeanDefinitionRegistry) context;
        } else {
            ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
            if (beanFactory instanceof BeanDefinitionRegistry) {
                this.registry = (BeanDefinitionRegistry) beanFactory;
            }
        }

        this.doContextPrepared(context, registry, environment);
    }

    protected void doContextPrepared(ConfigurableApplicationContext context, BeanDefinitionRegistry registry, ConfigurableEnvironment environment) {

    }

    protected <T> T bindProperties(Environment environment, String prefix, Class<T> target, T defaultProperties) {
        try {
            if (environment == null) {
                environment = this.environment;
            }
            return Binder.get(environment).bind(prefix, target).get();
        } catch (NoSuchElementException e) {
            return defaultProperties;
        }
    }

    protected <T> T bindProperties(String prefix, Class<T> target, T defaultProperties) {
        return bindProperties(this.environment, prefix, target, defaultProperties);
    }

    protected <T> T bindProperties(String prefix, Class<T> target) {
        return bindProperties(this.environment, prefix, target, null);
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }

    @Override
    public final int getOrder() {
        // 必须在 com.duowan.yyspring.boot.YySpringApplicationRunListener 之后执行
        return 2 + order();
    }

    protected int order() {
        return 0;
    }
}
