package com.duowan.common.redis.register;

import com.duowan.common.redis.DefaultRedisImpl;
import com.duowan.common.redis.model.StdRedisDef;
import com.duowan.common.redis.provider.jedis.DefaultJedisProvider;
import com.duowan.common.utils.CommonUtil;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.env.Environment;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

/**
 * <pre>
 *
 *     支持简单类型的 StdRedisDef 注册，注册结果包含如下Bean
 *     {redisId}Redis             DefaultRedisImpl
 *     {redisId}JedisPoolConfig   JedisPoolConfig
 *     {redisId}JedisProvider     DefaultJedisProvider
 *     {redisId}JedisPool         JedisPool
 *
 * </pre>
 *
 * @author Arvin
 */
public class StdRedisDefinitionRegister extends AbstractRedisRegister<StdRedisDef> {

    @Override
    protected void register(StdRedisDef redisDef, Environment environment, BeanDefinitionRegistry registry) {

        if (null == redisDef) {
            return;
        }

        String poolConfigBeanName = registerPoolConfigBeanDefinition(redisDef, registry);

        String jedisPoolBeanName = registerJedisPoolBeanDefinition(redisDef, registry, poolConfigBeanName);

        String providerBeanName = registerJedisProviderBeanDefinition(redisDef, registry, jedisPoolBeanName);

        registerRedisBeanDefinition(redisDef, registry, providerBeanName);

    }

    private void registerRedisBeanDefinition(StdRedisDef definition, BeanDefinitionRegistry registry, String providerBeanName) {
        String redisBeanName = definition.getId() + "Redis";
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DefaultRedisImpl.class);
        beanDefinition.getPropertyValues().addPropertyValue("provider", new RuntimeBeanReference(providerBeanName));

        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        if (definition.isPrimary()) {
            beanDefinition.setPrimary(true);
        }

        registry.registerBeanDefinition(redisBeanName, beanDefinition);
    }

    private String registerJedisProviderBeanDefinition(StdRedisDef definition, BeanDefinitionRegistry registry, String jedisPoolBeanName) {
        String providerBeanName = definition.getId() + "JedisProvider";
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DefaultJedisProvider.class);
        beanDefinition.getPropertyValues().addPropertyValue("jedisPool", new RuntimeBeanReference(jedisPoolBeanName));

        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        if (definition.isPrimary()) {
            beanDefinition.setPrimary(true);
        }
        registry.registerBeanDefinition(providerBeanName, beanDefinition);
        return providerBeanName;
    }

    private String registerJedisPoolBeanDefinition(StdRedisDef definition, BeanDefinitionRegistry registry, String poolConfigBeanName) {
        String jedisPoolBeanName = definition.getId() + "JedisPool";
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(JedisPool.class);
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, new RuntimeBeanReference(poolConfigBeanName));
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, definition.getHost());
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(2, definition.getPort());
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(3, definition.getTimeout());
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(4, definition.getPassword());
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(5, definition.getDatabase());

        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        if (definition.isPrimary()) {
            beanDefinition.setPrimary(true);
        }

        registry.registerBeanDefinition(jedisPoolBeanName, beanDefinition);

        return jedisPoolBeanName;
    }

    private String registerPoolConfigBeanDefinition(StdRedisDef definition, BeanDefinitionRegistry registry) {
        String poolConfigBeanName = definition.getId() + "JedisPoolConfig";
        Class<?> poolClass = JedisPoolConfig.class;
        // 移除不识别的连接池配置
        Map<String, String> poolConfig = CommonUtil.filterUnRecordedField(definition.getPoolConfig(), poolClass);
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(poolClass);
        beanDefinition.setLazyInit(true);
        MutablePropertyValues properties = new MutablePropertyValues(poolConfig);
        beanDefinition.getPropertyValues().addPropertyValues(properties);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        beanDefinition.setPrimary(definition.isPrimary());

        registry.registerBeanDefinition(poolConfigBeanName, beanDefinition);
        return poolConfigBeanName;
    }

}
