package com.duowan.common.redis.parser.standard;

import com.duowan.common.redis.model.RedisDef;
import com.duowan.common.redis.model.StdRedisDef;
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
 * 标准 RedisDef 定义解析器
 *
 * 前缀： redis.std
 * 配置项：
 * 注： Redis ID, 唯一标识这个 Redis 实例
 * required: redis.std.{redisId}.server=localhost:6379 HOST:POST
 * optional: redis.std.{redisId}.primary=true          是否 primary
 * optional: redis.std.{redisId}.timeout=3000          连接超时，默认是3000
 * optional: redis.std.{redisId}.password=123456       安全链接密码，默认是null
 * optional: redis.std.{redisId}.database=0            连接的数据库，从0开始，默认是 0
 * optional: redis.std.{redisId}.pool.xxx              连接池配置，参考标准的 redis.clients.jedis.JedisPoolConfig
 *
 * </pre>
 *
 * @author Arvin
 */
public class StandardRedisDefParser implements RedisDefParser {

    protected static final Logger logger = LoggerFactory.getLogger(StandardRedisDefParser.class);

    /**
     * 属性解析器
     */
    private static final List<StandardRedisDefFieldResolver> RESOLVER_LIST = lookupRedisDefFieldResolvers();

    /**
     * 搜索解析器
     *
     * @return 返回解析器列表
     */
    private static List<StandardRedisDefFieldResolver> lookupRedisDefFieldResolvers() {

        return ReflectUtil.scanAndInstanceByDefaultConstructor(
                StandardRedisDefFieldResolver.class, StandardRedisDefFieldResolver.class.getPackage().getName());

    }

    @Override
    public List<? extends RedisDef> parseRedisDefList(Map<String, String> configMap, Environment environment) {

        if (configMap == null || configMap.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, StdRedisDef> defMap = new HashMap<>();

        final String regex = "redis\\.std\\.([^\\.]+)\\.(.*)$";

        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            String key = CommonUtil.trim(entry.getKey());

            if (StringUtils.isBlank(key) || !key.startsWith("redis.std.")) {
                continue;
            }

            String value = CommonUtil.trim(entry.getValue());
            if (StringUtils.isBlank(value)) {
                value = "";
            }
            String id = CommonUtil.trim(key.replaceFirst(regex, "$1"));
            String subKey = CommonUtil.trim(key.replaceFirst(regex, "$2"));

            StdRedisDef redisDef = defMap.get(id);
            if (null == redisDef) {
                redisDef = new StdRedisDef();
                redisDef.setId(id);
                defMap.put(id, redisDef);
            }

            // 处理其他属性
            resolveDefinitionProperty(redisDef, key, subKey, value);

        }

        return new ArrayList<>(defMap.values());
    }

    protected static StdRedisDef resolveDefinitionProperty(StdRedisDef redisDef, String key, String subKey, String value) {
        boolean hadResolved = false;
        if (RESOLVER_LIST != null) {
            for (StandardRedisDefFieldResolver resolver : RESOLVER_LIST) {
                if (resolver.resolve(redisDef, key, subKey, value)) {
                    hadResolved = true;
                    break;
                }
            }
        }

        if (!hadResolved) {
            logger.warn("无效的STD Redis属性[" + key + "]=[" + value + "]");
        }
        return redisDef;
    }

}
