package com.duowan.common.redis.util;

import com.duowan.common.redis.model.RedisDef;
import com.duowan.common.redis.model.RiseRedisDef;
import com.duowan.common.redis.parser.RedisDefParser;
import com.duowan.common.redis.provider.def.RedisDefProvider;
import com.duowan.common.redis.register.RedisRegister;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Arvin
 */
public abstract class RedisDefUtil {

    /**
     * JDBC 扩展配置前缀
     */
    public static final String REDIS_EXTEND_PREFIX = "extend.redis.";

    /**
     * 返回 Redis 自定义组件实例列表,使用：
     * extend.redis.[appId].[extendType].[extendKey]=[extendValue]
     * 如，扩展自定义的 RedisDefParser
     * # 下面的配置将实例化指定包下面实现了 JdbcDefParser 接口且包含默认构造函数的类
     * extend.redis.test.parser.packages=com.duowan.test.parser,net.oschina.test.parser
     * # 下面的配置直接指定某个parser
     * extend.redis.test.parser.classes=com.duowan.test.parser.CustomRedisDefParser,net.oschina.test.parser.CustomRedisDefParser
     *
     * @param configMap     配置MAP
     * @param environment   环境
     * @param componentType 组件类型
     * @param packageSuffix 包后缀
     * @param classesSuffix 类后缀
     * @param <T>           结果类型
     * @return 始终返回非 null
     */
    public static <T> List<T> lookupRedisDefExtendComponents(Map<String, String> configMap, Environment environment, Class<T> componentType, String packageSuffix, String classesSuffix) {
        List<T> componentList = new ArrayList<>();

        // 获取自定义的 JdbcDefParser
        if (null == configMap || configMap.isEmpty()) {
            return componentList;
        }

        for (Map.Entry<String, String> entry : configMap.entrySet()) {

            String key = entry.getKey();

            String value = entry.getValue();
            if (StringUtils.isBlank(value)) {
                value = "";
            }

            // 不是可识别的参数
            if (StringUtils.isAnyBlank(key, value) || !key.startsWith(REDIS_EXTEND_PREFIX)) {
                continue;
            }

            // 指定包下面的所有实现了 JdbcDefParser 且包含默认构造函数的都创建实例
            if (StringUtils.isNotBlank(packageSuffix) && key.endsWith(packageSuffix)) {
                List<T> subComponentList = ReflectUtil.scanAndInstanceByDefaultConstructor(componentType, value);
                if (null != subComponentList && !subComponentList.isEmpty()) {
                    componentList.addAll(subComponentList);
                }
                continue;
            }

            // 指定实现了 JdbcDefParser 且含默认构造函数的都创建实例
            if (StringUtils.isNotBlank(classesSuffix) && key.endsWith(classesSuffix)) {
                List<T> subComponentList = ReflectUtil.newInstancesByDefaultConstructor(componentType, value);
                if (null != subComponentList && !subComponentList.isEmpty()) {
                    componentList.addAll(subComponentList);
                }
            }
        }

        // 过滤重复的
        return CommonUtil.filterDuplicateInstance(componentList);
    }

    /**
     * 返回 Redis 定义解析器列表, 可以自定义解析器，使用：
     * extend.redis.[appId].[extendType].[extendKey]=[extendValue]
     * 如，扩展自定义的 RedisDefParser
     * # 下面的配置将实例化指定包下面实现了 RedisDefParser 接口且包含默认构造函数的类
     * extend.redis.test.parser.packages=com.duowan.test.parser,net.oschina.test.parser
     * # 下面的配置直接指定某个parser
     * extend.redis.test.parser.classes=com.duowan.test.parser.CustomRedisDefParser,net.oschina.test.parser.CustomRedisDefParser
     *
     * @param configMap   配置MAP
     * @param environment 环境
     * @return 始终返回非 null
     */
    public static List<RedisDefParser> lookupRedisDefParsers(Map<String, String> configMap, Environment environment) {
        List<RedisDefParser> parserList = new ArrayList<>();

        CommonUtil.appendList(parserList, ReflectUtil.scanAndInstanceByDefaultConstructor(RedisDefParser.class, RedisDefParser.class.getPackage().getName()));

        CommonUtil.appendList(parserList, lookupRedisDefExtendComponents(configMap, environment,
                RedisDefParser.class, "parser.packages", "parser.classes"));

        // 过滤重复的
        return CommonUtil.filterDuplicateInstance(parserList);
    }

    /**
     * 返回 Redis 定义Bean注册列表, 可以自定义，使用：
     * extend.redis.[appId].[extendType].[extendKey]=[extendValue]
     * 如，扩展自定义的 RedisRegister
     * # 下面的配置将实例化指定包下面实现了 RedisDefParser 接口且包含默认构造函数的类
     * extend.redis.test.register.packages=com.duowan.test.register,net.oschina.test.register
     * # 下面的配置直接指定某个parser
     * extend.redis.test.register.classes=com.duowan.test.register.CustomRedisRegister,net.oschina.test.parser.CustomRedisRegister
     *
     * @param configMap   配置MAP
     * @param environment 环境
     * @return 始终返回非 null
     */
    public static List<RedisRegister> lookupRedisRegisters(Map<String, String> configMap, Environment environment) {
        List<RedisRegister> instanceList = new ArrayList<>();

        CommonUtil.appendList(instanceList, ReflectUtil.scanAndInstanceByDefaultConstructor(RedisRegister.class, RedisRegister.class.getPackage().getName()));

        CommonUtil.appendList(instanceList, lookupRedisDefExtendComponents(configMap, environment,
                RedisRegister.class, "register.packages", "register.classes"));

        // 过滤重复的
        return CommonUtil.filterDuplicateInstance(instanceList);
    }

    /**
     * 解析 Redis 定义列表
     *
     * @param configMap   配置MAP
     * @param parserList  解析器列表
     * @param environment 环境
     * @return 返回非 null 列表
     */
    public static List<RedisDef> parseRedisDefList(Map<String, String> configMap, List<RedisDefParser> parserList, Environment environment) {

        List<RedisDef> resultList = new ArrayList<>();

        for (RedisDefParser parser : parserList) {
            List<? extends RedisDef> subList = parser.parseRedisDefList(configMap, environment);
            if (null != subList && !subList.isEmpty()) {
                resultList.addAll(subList);
            }
        }

        return resultList;
    }

    /**
     * 提取启用的列表：
     * <p>
     * extend.redis.[appId].[extendType].[extendKey]=[extendValue]
     * <p>
     * # 下面的配置直接指定哪些 redis 定义的 ID 是启用的
     * extend.redis.test.enabled.ids=user,user1
     *
     * @param enabledIds   要启用的ID列表
     * @param redisDefList redisDef 定义列表
     * @return 返回所有启用的 RedisDef 定义
     */
    public static List<RedisDef> extractEnabledRedisDefList(Set<String> enabledIds, List<RedisDef> redisDefList) {

        if (enabledIds == null || enabledIds.isEmpty() || null == redisDefList || redisDefList.isEmpty()) {
            return redisDefList;
        }

        List<RedisDef> resultList = new ArrayList<>();
        for (RedisDef redisDef : redisDefList) {
            if (isRedisDefEnabled(enabledIds, redisDef)) {
                resultList.add(redisDef);
            }
        }
        return resultList;
    }

    /**
     * 判断 redis 定义是否启用
     *
     * @param enabledIds 启用的 RedisDef id ，可以使用通配符 *
     * @param redisDef   RedisDef 定义
     * @return 返回是否启用
     */
    private static boolean isRedisDefEnabled(Set<String> enabledIds, RedisDef redisDef) {
        if (enabledIds == null || enabledIds.isEmpty()) {
            return true;
        }
        if (enabledIds.contains(redisDef.getId())) {
            return true;
        }
        // 通配符检查
        for (String enabledId : enabledIds) {
            if (CommonUtil.isStartWildcardMatch(redisDef.getId(), enabledId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 将 RedisDef.id = primaryId 的定义对象设置为 primary
     *
     * @param primaryId    主ID
     * @param redisDefList 定义列表
     * @return 返回原jdbc定义列表
     */
    public static List<RedisDef> applyPrimaryJdbcDefList(String primaryId, List<RedisDef> redisDefList) {

        if (null == redisDefList || redisDefList.isEmpty()) {
            return redisDefList;
        }
        for (RedisDef redisDef : redisDefList) {
            redisDef.setPrimary(redisDef.getId().equals(primaryId));
        }

        return redisDefList;
    }

    /**
     * 获取默认的 RedisDef  provider 实例对象
     *
     * @param configMap   配置map
     * @param environment 环境
     * @return 返回非 null list
     */
    public static List<RedisDefProvider> lookupRedisDefDbProviders(Map<String, String> configMap, Environment environment) {

        List<RedisDefProvider> providerList = new ArrayList<>();

        CommonUtil.appendList(providerList, ReflectUtil.scanAndInstanceByDefaultConstructor(RedisDefProvider.class, RedisDefProvider.class.getPackage().getName()));

        CommonUtil.appendList(providerList, lookupRedisDefExtendComponents(configMap, environment,
                RedisDefProvider.class, "provider.packages", "provider.classes"));

        return CommonUtil.filterDuplicateInstance(providerList);
    }

    public static List<RedisDef> autoFillProperties(List<RedisDef> redisDefList, Environment environment) {
        List<RedisDef> resultList = new ArrayList<>();

        for (RedisDef jdbcDef : redisDefList) {

            if (jdbcDef instanceof RiseRedisDef) {

                List<RedisDef> subList = autoFillProperties((RiseRedisDef) jdbcDef, environment);

                if (null != subList && !subList.isEmpty()) {
                    resultList.addAll(subList);
                }
            } else {
                resultList.add(jdbcDef);
            }
        }

        return resultList;
    }

    public static List<RedisDef> autoFillProperties(RiseRedisDef redisDef, Environment environment) {
        List<RedisDef> resultList = new ArrayList<>();

        String dsName = redisDef.getName();

        String host = lookupRiseDsEnvVar(environment, dsName, "host", "");
        String port = lookupRiseDsEnvVar(environment, dsName, "port", "");

        if (StringUtils.isAnyBlank(host, port)) {
            return resultList;
        }

        String server = host + ":" + port;

        redisDef.setServer(server);

        resultList.add(redisDef);

        return resultList;
    }

    private static String lookupRiseDsEnvVar(Environment environment, String dsName, String field, String defVal) {

        String key = dsName + "_" + field;

        String value = environment.getProperty(key);
        if (StringUtils.isBlank(value)) {
            value = System.getProperty(key);
        }

        if (StringUtils.isBlank(value)) {
            value = System.getenv(key);
        }

        value = StringUtils.isBlank(value) ? defVal : value;
        value = environment.resolvePlaceholders(value);
        return value;
    }
}
