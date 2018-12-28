package com.duowan.yyspringboot.autoconfigure.redis;

import com.duowan.yyspring.boot.SpringApplicationRunListenerAdapter;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/26 15:17
 */
public class RedisConfigApplicationRunListener extends SpringApplicationRunListenerAdapter {

    private boolean needAutoConfigurer = false;

    public RedisConfigApplicationRunListener(SpringApplication application, String[] args) {
        super(application, args);
        this.needAutoConfigurer = isClassesImported("com.duowan.common.redis.Redis");
    }

    @Override
    protected boolean needAutoConfigurer() {
        return needAutoConfigurer;
    }

    @Override
    protected void doContextPrepared(ConfigurableApplicationContext context, BeanDefinitionRegistry registry, ConfigurableEnvironment environment) {
        RedisProperties redisProperties = bindProperties(RedisProperties.PROPERTIES_PREFIX, RedisProperties.class);

        if (null != redisProperties) {
            RedisSpringRegister.registerRedisBeans(redisProperties, context, registry, environment);
        }
    }

}
