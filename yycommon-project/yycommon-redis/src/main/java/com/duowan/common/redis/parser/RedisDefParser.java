package com.duowan.common.redis.parser;

import com.duowan.common.redis.model.RedisDef;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;

/**
 * RedisDef 解析器接口定义
 *
 * @author Arvin
 */
public interface RedisDefParser {

    /**
     * 从环境中读取 redis 定义
     *
     * @param configMap   配置Map
     * @param environment SpringBoot 运行环境
     * @return 如果没有的话返回一个空的列表集合
     */
    List<? extends RedisDef> parseRedisDefList(Map<String, String> configMap, Environment environment);

}
