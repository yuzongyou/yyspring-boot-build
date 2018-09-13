package com.duowan.common.redis.parser.rise;

import com.duowan.common.redis.model.RedisDef;
import com.duowan.common.redis.model.RiseRedisDef;
import com.duowan.common.redis.model.StdRedisDef;
import com.duowan.common.redis.parser.RedisDefParser;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ConvertUtil;
import com.duowan.common.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * <pre>
 *
 * 支持升龙Redis数据源， 升龙数据源在部署的时候，升龙会把相关的配置写到环境变量中去:
 *     cloudapp_cloudredis1000_port:8096
 *     cloudapp_cloudredis1000_host:e009be.redis.yyclouds.com
 *
 * 前缀： redis.rise
 * 配置项：
 * 注： Redis ID, 唯一标识这个 Redis 实例
 * required: redis.rise.{redisId}.name=cloudapp_cloudredis1000 升龙Redis数据源
 * optional: redis.rise.{redisId}.timeout=3000                 连接超时，默认是3000
 * optional: redis.rise.{redisId}.password=123456              安全链接密码，默认是null
 * optional: redis.rise.{redisId}.database                     连接的数据库，从0开始，默认是 0
 * optional: redis.rise.{redisId}.pool.xxx                     连接池配置，参考标准的：
 * redis.clients.jedis.JedisPoolConfig
 *
 * 以上配置，最终会注册以下 Bean（参见 StdRedisDefRegister）：
 * ${redisId}Redis                         DefaultRedisImpl
 * ${redisId}JedisProvider                 DefaultJedisProvider
 * ${redisId}JedisPool                     redis.clients.jedis.JedisPool
 * ${redisId}JedisPoolConfig               redis.clients.jedis.JedisPoolConfig
 *
 * 示例：
 * redis.rise.common.bane=cloudapp_cloudredis1000
 *
 * 然后代码中可以通过以下方式进行注入：
 *
 * @author Arvin
 * Resource(name = "commonRedis")
 * Redis commonRedis;
 * 会注册Bean：
 * commonRedis
 * commonJedisProvider
 * commonJedisPool
 * commonJedisPoolConfig redis.clients.jedis.JedisPoolConfig
 * 注意，如果在开发环境下，是没有写入到环境变量的，因此需要在开发环境下设置属性：
 * cloudapp_cloudredis1000_host=xxx
 * cloudapp_cloudredis1000_port=xxx
 * </pre>
 */
public class RiseRedisDefParser implements RedisDefParser {

    protected static final Logger logger = LoggerFactory.getLogger(RiseRedisDefParser.class);

    /**
     * 属性解析器
     */
    private static final List<RiseRedisDefFieldResolver> RESOLVER_LIST = lookupRedisDefFieldResolvers();

    /**
     * 搜索解析器
     *
     * @return 返回解析器列表
     */
    private static List<RiseRedisDefFieldResolver> lookupRedisDefFieldResolvers() {

        return ReflectUtil.scanAndInstanceByDefaultConstructor(
                RiseRedisDefFieldResolver.class, RiseRedisDefFieldResolver.class.getPackage().getName());

    }

    @Override
    public List<? extends RedisDef> parseRedisDefList(Map<String, String> configMap, Environment environment) {

        // 解析升龙数据源别名
        Map<String, String> aliasMap = parseRiseDsAliasMap(configMap);

        List<RiseRedisDef> riseRedisDefList = parseRedisDefList(environment, configMap, aliasMap);

        // 处理环境变量值
        riseRedisDefList = resolveRiseEnvVar(configMap, riseRedisDefList);

        return riseRedisDefList;
    }

    /**
     * <pre>
     * 处理环境变量的值
     * 1. server -- host + port
     * </pre>
     *
     * @param defList 定义列表
     * @return 返回定义列表
     */
    private List<RiseRedisDef> resolveRiseEnvVar(Map<String, String> configMap, List<RiseRedisDef> defList) {

        List<RiseRedisDef> resultList = new ArrayList<>();

        if (defList == null || defList.isEmpty()) {
            return resultList;
        }
        for (RiseRedisDef redisDef : defList) {

            String dsName = redisDef.getName();

            String host = lookupRiseDsEnvVar(configMap, dsName, "host", "");
            String port = lookupRiseDsEnvVar(configMap, dsName, "port", "");

            if (StringUtils.isAnyBlank(host, port)) {
                continue;
            }

            String server = host + ":" + port;

            redisDef.setServer(server);

            resultList.add(redisDef);

        }

        return resultList;
    }

    private String lookupRiseDsEnvVar(Map<String, String> configMap, String dsName, String field, String defVal) {

        String key = dsName + "_" + field;

        String value = System.getenv(key);

        if (StringUtils.isBlank(value)) {
            value = System.getProperty(key);
        }
        if (StringUtils.isBlank(value)) {
            return configMap.get(key);
        }

        return value;
    }

    private Map<String, String> parseRiseDsAliasMap(final Map<String, String> aliasConfigMap) {

        Map<String, String> aliasMap = new HashMap<>();

        if (null == aliasConfigMap || aliasConfigMap.isEmpty()) {
            return aliasMap;
        }
        final String regex = "rise\\.ds\\.(.*)$";
        for (Map.Entry<String, String> entry : aliasConfigMap.entrySet()) {

            String key = entry.getKey();
            String value = ConvertUtil.toString(entry.getValue(), "").trim();

            if (!key.startsWith("rise.ds.")) {
                continue;
            }

            String dsName = key.replaceFirst(regex, "$1");
            if (StringUtils.isAnyBlank(value, dsName)) {
                continue;
            }

            aliasMap.put(dsName, value);
        }

        return aliasMap;
    }

    /**
     * 解析标准的Redis定义
     *
     * @param environment 环境
     * @param configMap   所有标准Redis 配置 Map
     * @param aliasMap    升龙数据源名称
     * @return 返回Redis定义列表
     */
    protected static List<RiseRedisDef> parseRedisDefList(Environment environment, Map<String, String> configMap, Map<String, String> aliasMap) {

        if (configMap == null || configMap.isEmpty()) {
            return new ArrayList<>();
        }

        Map<String, RiseRedisDef> defMap = new HashMap<>();

        final String regex = "redis\\.rise\\.([^\\.]+)\\.(.*)$";

        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            String key = CommonUtil.trim(entry.getKey());

            if (StringUtils.isBlank(key) || !key.startsWith("redis.rise.")) {
                continue;
            }

            String value = CommonUtil.trim(entry.getValue());
            if (StringUtils.isBlank(value)) {
                value = "";
            }
            String id = CommonUtil.trim(key.replaceFirst(regex, "$1"));
            String subKey = CommonUtil.trim(key.replaceFirst(regex, "$2"));

            RiseRedisDef redisDef = defMap.get(id);
            if (null == redisDef) {
                redisDef = new RiseRedisDef();
                redisDef.setId(id);
                defMap.put(id, redisDef);
            }

            // 处理其他属性
            resolveDefinitionProperty(aliasMap, redisDef, key, subKey, value);

        }

        return new ArrayList<>(defMap.values());
    }

    protected static StdRedisDef resolveDefinitionProperty(Map<String, String> aliasMap, RiseRedisDef redisDef, String key, String subKey, String value) {
        boolean hadResolved = false;
        if (RESOLVER_LIST != null) {
            for (RiseRedisDefFieldResolver resolver : RESOLVER_LIST) {
                if (resolver.resolve(aliasMap, redisDef, key, subKey, value)) {
                    hadResolved = true;
                    break;
                }
            }
        }

        if (!hadResolved) {
            logger.warn("无效的Rise Redis属性[" + key + "]=[" + value + "]");
        }
        return redisDef;
    }
}
