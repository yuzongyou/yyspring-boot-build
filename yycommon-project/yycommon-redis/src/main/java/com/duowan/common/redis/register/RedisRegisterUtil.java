package com.duowan.common.redis.register;

import com.duowan.common.redis.model.RedisDef;
import com.duowan.common.redis.parser.RedisDefParser;
import com.duowan.common.redis.provider.def.RedisDefProvider;
import com.duowan.common.redis.util.RedisDefUtil;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * @author Arvin
 */
public class RedisRegisterUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisRegisterUtil.class);

    /**
     * 解析并注册 Redis
     *
     * @param configMap   数据源配置MAP
     * @param enabledIds  启用的ID列表
     * @param primaryId   设置为主的JdbcDef.id 定义
     * @param environment Spring 环境
     * @param registry    注册入口
     */
    public synchronized static void registerRedisBeanDefinitions(Map<String, String> configMap, Set<String> enabledIds, String primaryId, Environment environment, BeanDefinitionRegistry registry) {

        // 获取 Redis Def 解析器
        List<RedisDefParser> parserList = RedisDefUtil.lookupRedisDefParsers(configMap, environment);

        // 解析标准所有的 RedisDef
        List<RedisDef> redisDefList = RedisDefUtil.parseRedisDefList(configMap, parserList, environment);

        registerRedisBeanDefinitions(null, null, redisDefList, enabledIds, primaryId, registry, environment);
    }

    /**
     * 注册RedisBean
     *
     * @param registerList 注册器列表
     * @param providerList 提供者列表
     * @param redisDefList Redis定义列表
     * @param enabledIds   要启用的ID列表，支持通配符 *
     * @param primaryId    主Redis 实例的ID
     * @param registry     注册器
     * @param environment  环境
     */
    public static void registerRedisBeanDefinitions(
            List<RedisRegister> registerList,
            List<RedisDefProvider> providerList,
            List<RedisDef> redisDefList,
            Set<String> enabledIds,
            String primaryId,
            BeanDefinitionRegistry registry,
            Environment environment) {

        if (null == redisDefList || redisDefList.isEmpty()) {
            return;
        }

        // 自动填充属性
        redisDefList = RedisDefUtil.autoFillProperties(redisDefList, environment);

        // 提取所有启用的Jdbc定义列表
        redisDefList = RedisDefUtil.extractEnabledRedisDefList(enabledIds, redisDefList);

        // 应用 主 定义
        redisDefList = RedisDefUtil.applyPrimaryJdbcDefList(primaryId, redisDefList);

        // 检查定义列表，看看是不是有重复、是否有多个 primary 定义的之类
        redisDefList = checkRedisDefList(redisDefList);

        registerList = appendDefaultRegisterAndFilterDuplicateInstance(registerList);
        AssertUtil.assertNotEmpty(registerList, "Redis注册列表不能为空，当前解析Redis列表数： " + redisDefList.size());
        providerList = appendDefaultProviderAndFilterDuplicateInstance(providerList);

        RedisDef primary = null;

        for (RedisDef redisDef : redisDefList) {

            if (primary != null) {
                AssertUtil.assertFalse(redisDef.isPrimary(), "当前 Redis 定义 primary 冲突，只能设置一个primary,冲突项：[" + primary.getId() + "," + redisDef.getId() + "]");
            }

            if (redisDef.isPrimary()) {
                primary = redisDef;
            }

            // 应用默认配置
            applyRedisDefinitionDefaultConfig(redisDef, providerList);

            RedisRegister register = lookupRegister(registerList, redisDef);

            AssertUtil.assertNotNull(register, redisDef.getClass().getName() + " 找不到对应的Redis 注册器！");

            register.registerRedis(redisDef, environment, registry);

            logger.info("自动注册Redis数据源：" + redisDef);
        }

    }

    private static List<RedisDefProvider> appendDefaultProviderAndFilterDuplicateInstance(List<RedisDefProvider> providerList) {

        if (null == providerList) {
            providerList = new ArrayList<>();
        }

        CommonUtil.appendList(providerList, ReflectUtil.scanAndInstanceByDefaultConstructor(RedisDefProvider.class, RedisDefProvider.class.getPackage().getName()));

        return providerList;

    }

    private static List<RedisRegister> appendDefaultRegisterAndFilterDuplicateInstance(List<RedisRegister> registerList) {

        if (registerList == null) {
            registerList = new ArrayList<>();
        }

        CommonUtil.appendList(registerList, ReflectUtil.scanAndInstanceByDefaultConstructor(RedisRegister.class, RedisRegister.class.getPackage().getName()));

        return CommonUtil.filterDuplicateElement(registerList);
    }

    private static void applyRedisDefinitionDefaultConfig(RedisDef redisDef, List<RedisDefProvider> providerList) {

        if (null == redisDef) {
            return;
        }

        if (null == providerList || providerList.isEmpty()) {
            return;
        }

        for (RedisDefProvider provider : providerList) {
            if (provider.checkAndApplyDefaultConfig(redisDef)) {
                break;
            }
        }

    }

    private static RedisRegister lookupRegister(List<RedisRegister> registerList, RedisDef definition) {
        for (RedisRegister register : registerList) {
            if (register.canHandle(definition)) {
                return register;
            }
        }

        return null;
    }

    /**
     * RedisDef.id --> RedisDef
     */
    private static Map<String, RedisDef> REDIS_DEF_MAP = new HashMap<>();

    /**
     * 主 定义
     */
    private static volatile RedisDef PRIMARY_REDIS_DEF = null;

    /**
     * 检查是否有重复的
     */
    private static List<RedisDef> checkRedisDefList(List<RedisDef> redisDefList) {

        if (null == redisDefList || redisDefList.isEmpty()) {
            return redisDefList;
        }

        for (RedisDef redisDef : redisDefList) {
            AssertUtil.assertFalse(REDIS_DEF_MAP.containsKey(redisDef.getId()), "RedisDef[id=" + redisDef.getId() + "]重复定义！");

            if (redisDef.isPrimary()) {
                if (null == PRIMARY_REDIS_DEF) {
                    PRIMARY_REDIS_DEF = redisDef;
                } else {
                    AssertUtil.assertTrue(PRIMARY_REDIS_DEF.getId().equals(redisDef.getId()),
                            "不能定义多个 primary RedisDef 目前定义了[" + PRIMARY_REDIS_DEF.getId() + "," + redisDef.getId() + "]");
                }

            }

            REDIS_DEF_MAP.put(redisDef.getId(), redisDef);
        }

        return redisDefList;
    }

}
