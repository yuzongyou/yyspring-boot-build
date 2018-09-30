package com.duowan.yyspringboot.autoconfigure;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/26 15:34
 */
public abstract class SpringApplicationRunListenerAdapter implements SpringApplicationRunListener, Ordered {

    protected static final int DEFAULT_ORDER = 100;
    protected static final int YYCORE_DEFAULT_ORDER = 1;

    private final SpringApplication application;

    private final String[] args;

    private ConfigurableEnvironment environment;

    public SpringApplicationRunListenerAdapter(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
    }

    protected abstract boolean needAutoConfigurer();

    public SpringApplication getApplication() {
        return application;
    }

    public String[] getArgs() {
        return args;
    }

    @Override
    public final void starting() {
        if (needAutoConfigurer()) {
            doStarting();
        }
    }

    protected void doStarting() {
    }

    @Override
    public final void environmentPrepared(ConfigurableEnvironment environment) {
        this.environment = environment;
        if (needAutoConfigurer()) {
            doEnvironmentPrepared(environment);
        }
    }

    protected void doEnvironmentPrepared(ConfigurableEnvironment environment) {
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

        if (needAutoConfigurer()) {
            this.doContextPrepared(context, registry, environment);
        }
    }

    protected void doContextPrepared(ConfigurableApplicationContext context, BeanDefinitionRegistry registry, ConfigurableEnvironment environment) {

    }

    protected <T> T bindProperties(Environment environment, String prefix, Class<T> targetType, T defaultProperties) {
        try {
            if (environment == null) {
                environment = this.environment;
            }
            RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment);
            Map<String, Object> properties = resolver.getSubProperties("");
            T target = targetType.newInstance();
            RelaxedDataBinder binder = new RelaxedDataBinder(target, prefix);
            binder.bind(new MutablePropertyValues(properties));
            return target;
        } catch (Exception e) {
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
    public final void contextLoaded(ConfigurableApplicationContext context) {
        if (needAutoConfigurer()) {
            doContextLoaded(context);
        }
    }

    protected void doContextLoaded(ConfigurableApplicationContext context) {
    }

    @Override
    public final void finished(ConfigurableApplicationContext context, Throwable throwable) {
        if (needAutoConfigurer()) {
            doFinished(context, throwable);
        }
    }

    protected void doFinished(ConfigurableApplicationContext context, Throwable throwable) {
    }

    @Override
    public final int getOrder() {
        // 必须在 com.duowan.yyspring.boot.YySpringApplicationRunListener 之后执行
        return DEFAULT_ORDER + order();
    }

    protected int order() {
        return 0;
    }

    protected boolean isClassesImported(String... classes) {
        if (classes == null || classes.length < 1) {
            return true;
        }
        for (String clazz : classes) {
            if (!ClassUtils.isPresent(clazz, Thread.currentThread().getContextClassLoader())) {
                return false;
            }
        }
        return true;
    }
}
