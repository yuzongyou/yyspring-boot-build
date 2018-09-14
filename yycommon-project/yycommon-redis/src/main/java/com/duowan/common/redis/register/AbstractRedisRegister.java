package com.duowan.common.redis.register;

import com.duowan.common.redis.model.RedisDefinition;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @param <T> Redis 定义注册器
 * @author Arvin
 */
public abstract class AbstractRedisRegister<T extends RedisDefinition> implements RedisRegister {

    protected Class<T> definitionClass;

    @SuppressWarnings({"unchecked"})
    public AbstractRedisRegister() {
        this.definitionClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public boolean canHandle(RedisDefinition redisDefinition) {
        return definitionClass.isInstance(redisDefinition);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public final void registerRedis(RedisDefinition redisDefinition, Environment environment, BeanDefinitionRegistry registry) {
        AssertUtil.assertTrue(canHandle(redisDefinition), "[" + getClass().getName() + "] 无法注册[" + redisDefinition + "]");
        // 注册
        register((T) redisDefinition, environment, registry);
    }

    /**
     * 删除不识别的连接池属性
     *
     * @param poolConfig 连接池配置MAP
     * @param poolClass  连接池类
     * @return 返回MAP
     */
    protected Map<String, Object> removeDefinitionUnSupportPoolConfig(Map<String, Object> poolConfig, Class<?> poolClass) {

        Set<String> fieldNames = poolConfig.keySet();

        Method[] allMethods = poolClass.getMethods();

        Map<String, Method> getMethodMap = new HashMap<>();
        Map<String, Method> setMethodMap = new HashMap<>();

        for (Method method : allMethods) {
            String methodName = method.getName();
            String fieldName = CommonUtil.firstLetterToLowerCase(methodName.replaceFirst("is|get|set", ""));
            if (methodName.startsWith("is") || methodName.startsWith("get")) {
                getMethodMap.put(fieldName, method);
            } else if (methodName.startsWith("set")) {
                setMethodMap.put(fieldName, method);
            }
        }

        Set<String> needRemoveFieldNames = new HashSet<>();

        for (String fieldName : fieldNames) {
            if (!getMethodMap.containsKey(fieldName) || !setMethodMap.containsKey(fieldName)) {
                needRemoveFieldNames.add(fieldName);
            }
        }

        if (!needRemoveFieldNames.isEmpty()) {
            for (String needRemoveFieldName : needRemoveFieldNames) {
                poolConfig.remove(needRemoveFieldName);
            }
        }

        return poolConfig;
    }

    /**
     * 注册 bean
     *
     * @param redisDef    redis定义
     * @param environment 环境
     * @param registry    注册器
     */
    protected abstract void register(T redisDef, Environment environment, BeanDefinitionRegistry registry);
}
