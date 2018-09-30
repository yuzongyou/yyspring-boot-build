package com.duowan.yyspring.boot;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;

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

    private static Map<String, AtomicInteger> instanceCountMap = new HashMap<>();

    private static synchronized int addInstanceCount(Class<?> clazz) {
        String className = clazz.getName();
        AtomicInteger counter = instanceCountMap.get(className);
        if (counter == null) {
            counter = new AtomicInteger(0);
            instanceCountMap.put(className, counter);
        }
        return counter.incrementAndGet();
    }

    private int instanceIndex = 0;

    public SpringApplicationRunListenerAdapter(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
        instanceIndex = addInstanceCount(getClass());
    }

    private boolean isFirstInit() {
        return instanceIndex == 1;
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
        if (isFirstInit() && needAutoConfigurer()) {
            doStarting();
        }
    }

    protected void doStarting() {
    }

    @Override
    public final void environmentPrepared(ConfigurableEnvironment environment) {
        if (isFirstInit() && needAutoConfigurer()) {
            this.environment = environment;
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

        if (isFirstInit() && needAutoConfigurer()) {
            this.doContextPrepared(context, registry, environment);
        }
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
    public final void contextLoaded(ConfigurableApplicationContext context) {
        if (isFirstInit() && needAutoConfigurer()) {
            doContextLoaded(context);
        }
    }

    protected void doContextLoaded(ConfigurableApplicationContext context) {
    }

    @Override
    public final void started(ConfigurableApplicationContext context) {
        if (isFirstInit() && needAutoConfigurer()) {
            doStarted(context);
        }
    }

    protected void doStarted(ConfigurableApplicationContext context) {
    }

    @Override
    public final void running(ConfigurableApplicationContext context) {
        if (isFirstInit() && needAutoConfigurer()) {
            doRunning(context);
        }
    }

    protected void doRunning(ConfigurableApplicationContext context) {
    }

    @Override
    public final void failed(ConfigurableApplicationContext context, Throwable exception) {
        if (isFirstInit() && needAutoConfigurer()) {
            doFailed(context, exception);
        }
    }

    protected void doFailed(ConfigurableApplicationContext context, Throwable exception) {
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
