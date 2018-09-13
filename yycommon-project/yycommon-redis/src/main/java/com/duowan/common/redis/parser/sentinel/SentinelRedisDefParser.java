package com.duowan.common.redis.parser.sentinel;

import com.duowan.common.redis.model.RedisDef;
import com.duowan.common.redis.model.SentinelRedisDef;
import com.duowan.common.redis.parser.RedisDefParser;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *     哨兵模式 Redis 定义解析器， 解析格式：
 *     前缀： sentinel.redis
 *     配置项：
 *     注： RedisID, 唯一标识这个 Redis 实例
 *
 *     redis.sentinel.{redisId}.sentinels=host1:port1,host2:port2... 哨兵主机、端口列表英文逗号分割
 *     redis.sentinel.{redisId}.masterName=xx  连接超时，默认是3000
 *     redis.sentinel.{redisId}.timeout=3000   连接超时，默认是3000
 *     redis.sentinel.{redisId}.password=      安全链接密码，默认是null
 *     redis.sentinel.{redisId}.database=0     连接的数据库，从0开始，默认是 0
 *     redis.sentinel.{redisId}.pool.xxx=      连接池配置，参考标准的 redis.clients.jedis.JedisPoolConfig
 *
 * </pre>
 *
 * @author Arvin
 */
public class SentinelRedisDefParser implements RedisDefParser {

    protected static final Logger logger = LoggerFactory.getLogger(SentinelRedisDefParser.class);

    /**
     * 属性解析器
     */
    private static final List<SentinelRedisDefFieldResolver> RESOLVER_LIST = lookupRedisDefFieldResolvers();

    /**
     * 搜索解析器
     *
     * @return 返回解析器列表
     */
    private static List<SentinelRedisDefFieldResolver> lookupRedisDefFieldResolvers() {

        return ReflectUtil.scanAndInstanceByDefaultConstructor(
                SentinelRedisDefFieldResolver.class, SentinelRedisDefFieldResolver.class.getPackage().getName());

    }

    @Override
    public List<? extends RedisDef> parseRedisDefList(Map<String, String> configMap, Environment environment) {

        if (configMap == null || configMap.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, SentinelRedisDef> defMap = new HashMap<>();

        final String regex = "redis\\.sentinel\\.([^\\.]+)\\.(.*)$";

        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            String key = CommonUtil.trim(entry.getKey());

            if (StringUtils.isBlank(key) || !key.startsWith("redis.sentinel.")) {
                continue;
            }

            String value = CommonUtil.trim(entry.getValue());
            if (StringUtils.isBlank(value)) {
                value = "";
            }
            String id = CommonUtil.trim(key.replaceFirst(regex, "$1"));
            String subKey = CommonUtil.trim(key.replaceFirst(regex, "$2"));

            SentinelRedisDef redisDef = defMap.get(id);
            if (null == redisDef) {
                redisDef = new SentinelRedisDef();
                redisDef.setId(id);
                defMap.put(id, redisDef);
            }

            // 处理其他属性
            resolveDefinitionProperty(redisDef, key, subKey, value);

        }

        return new ArrayList<>(defMap.values());

    }

    private static SentinelRedisDef resolveDefinitionProperty(SentinelRedisDef redisDef, String key, String subKey, String value) {
        boolean hadResolved = false;
        if (RESOLVER_LIST != null) {
            for (SentinelRedisDefFieldResolver resolver : RESOLVER_LIST) {
                if (resolver.resolve(redisDef, key, subKey, value)) {
                    hadResolved = true;
                    break;
                }
            }
        }

        if (!hadResolved) {
            logger.warn("无效的Sentinel Redis属性[" + key + "]=[" + value + "]");
        }
        return redisDef;
    }

}
