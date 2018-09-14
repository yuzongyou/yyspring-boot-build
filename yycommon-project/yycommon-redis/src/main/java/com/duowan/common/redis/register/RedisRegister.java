package com.duowan.common.redis.register;

import com.duowan.common.redis.model.RedisDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;

/**
 * Redis 注册器
 *
 * @author Arvin
 */
public interface RedisRegister {

    /**
     * 是否能够处理这个redis定义
     *
     * @param redisDefinition 要判断的对象
     * @return 如果可以注册就返回true，否则返回false， 返回false将不会调用 registerRedis 方法
     */
    boolean canHandle(RedisDefinition redisDefinition);

    /**
     * 注册 Redis 数据源
     *
     * @param redisDefinition    redis 定义
     * @param environment 环境
     * @param registry    注册入口
     */
    void registerRedis(RedisDefinition redisDefinition, Environment environment, BeanDefinitionRegistry registry);
}
