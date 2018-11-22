package com.duowan.common.redis;

import com.duowan.common.redis.model.RedisDefinition;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/22 20:47
 */
public class RedisDefinitionContext {

    private Map<String, RedisDefinition> redisDefinitionMap;

    public RedisDefinitionContext(List<RedisDefinition> redisDefinitionList) {
        if (null != redisDefinitionList) {
            redisDefinitionMap = redisDefinitionList.stream().collect(Collectors.toMap(RedisDefinition::getId, item -> item));
        }
    }

    public RedisDefinition getRedisDefinition(String redisId) {
        return null == redisDefinitionMap ? null : redisDefinitionMap.get(redisId);
    }

    public Map<String, RedisDefinition> getRedisDefinitionMap() {
        return redisDefinitionMap;
    }
}
