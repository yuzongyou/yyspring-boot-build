package com.duowan.common.redis.util;

import com.duowan.common.redis.model.RedisDefinition;
import com.duowan.common.redis.provider.def.RedisDefinitionProvider;
import com.duowan.common.redis.register.RedisRegister;
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
     * 注册RedisBean
     *
     * @param registerList 注册器列表
     * @param providerList 提供者列表
     * @param redisDefinitionList Redis定义列表
     * @param enabledIds   要启用的ID列表，支持通配符 *
     * @param primaryId    主Redis 实例的ID
     * @param registry     注册器
     * @param environment  环境
     */
    public static void registerRedisBeanDefinitions(
            List<RedisRegister> registerList,
            List<RedisDefinitionProvider> providerList,
            List<RedisDefinition> redisDefinitionList,
            Set<String> enabledIds,
            Set<String> excludeIds,
            String primaryId,
            BeanDefinitionRegistry registry,
            Environment environment) {

        if (null == redisDefinitionList || redisDefinitionList.isEmpty()) {
            return;
        }

        // 自动填充属性
        redisDefinitionList = RedisDefinitionUtil.autoFillProperties(redisDefinitionList, environment);

        // 删除所有禁用的Jdbc定义列表
        redisDefinitionList = RedisDefinitionUtil.filterExcludeRedisDefList(excludeIds, redisDefinitionList);

        // 提取所有启用的Jdbc定义列表
        redisDefinitionList = RedisDefinitionUtil.extractEnabledRedisDefList(enabledIds, redisDefinitionList);

        // 应用 主 定义
        redisDefinitionList = RedisDefinitionUtil.applyPrimaryJdbcDefList(primaryId, redisDefinitionList);

        // 检查定义列表，看看是不是有重复、是否有多个 primary 定义的之类
        redisDefinitionList = checkRedisDefList(redisDefinitionList);

        registerList = appendDefaultRegisterAndFilterDuplicateInstance(registerList);
        AssertUtil.assertNotEmpty(registerList, "Redis注册列表不能为空，当前解析Redis列表数： " + redisDefinitionList.size());
        providerList = appendDefaultProviderAndFilterDuplicateInstance(providerList);

        RedisDefinition primary = null;

        for (RedisDefinition redisDefinition : redisDefinitionList) {

            if (primary != null) {
                AssertUtil.assertFalse(redisDefinition.isPrimary(), "当前 Redis 定义 primary 冲突，只能设置一个primary,冲突项：[" + primary.getId() + "," + redisDefinition.getId() + "]");
            }

            if (redisDefinition.isPrimary()) {
                primary = redisDefinition;
            }

            // 应用默认配置
            applyRedisDefinitionDefaultConfig(redisDefinition, providerList);

            RedisRegister register = lookupRegister(registerList, redisDefinition);

            AssertUtil.assertNotNull(register, redisDefinition.getClass().getName() + " 找不到对应的Redis 注册器！");

            register.registerRedis(redisDefinition, environment, registry);

            logger.info("自动注册Redis数据源：" + redisDefinition);
        }

    }

    private static List<RedisDefinitionProvider> appendDefaultProviderAndFilterDuplicateInstance(List<RedisDefinitionProvider> providerList) {

        if (null == providerList) {
            providerList = new ArrayList<>();
        }

        CommonUtil.appendList(providerList, ReflectUtil.scanAndInstanceByDefaultConstructor(RedisDefinitionProvider.class, RedisDefinitionProvider.class.getPackage().getName()));

        return providerList;

    }

    private static List<RedisRegister> appendDefaultRegisterAndFilterDuplicateInstance(List<RedisRegister> registerList) {

        if (registerList == null) {
            registerList = new ArrayList<>();
        }

        CommonUtil.appendList(registerList, ReflectUtil.scanAndInstanceByDefaultConstructor(RedisRegister.class, RedisRegister.class.getPackage().getName()));

        return CommonUtil.filterDuplicateElement(registerList);
    }

    private static void applyRedisDefinitionDefaultConfig(RedisDefinition redisDefinition, List<RedisDefinitionProvider> providerList) {

        if (null == redisDefinition) {
            return;
        }

        if (null == providerList || providerList.isEmpty()) {
            return;
        }

        for (RedisDefinitionProvider provider : providerList) {
            if (provider.checkAndApplyDefaultConfig(redisDefinition)) {
                break;
            }
        }

    }

    private static RedisRegister lookupRegister(List<RedisRegister> registerList, RedisDefinition definition) {
        for (RedisRegister register : registerList) {
            if (register.canHandle(definition)) {
                return register;
            }
        }

        return null;
    }

    /**
     * RedisDefinition.id --> RedisDefinition
     */
    private static Map<String, RedisDefinition> REDIS_DEF_MAP = new HashMap<>();

    /**
     * 主 定义
     */
    private static volatile RedisDefinition PRIMARY_REDIS_DEF = null;

    /**
     * 检查是否有重复的
     */
    private static List<RedisDefinition> checkRedisDefList(List<RedisDefinition> redisDefinitionList) {

        if (null == redisDefinitionList || redisDefinitionList.isEmpty()) {
            return redisDefinitionList;
        }

        for (RedisDefinition redisDefinition : redisDefinitionList) {
            AssertUtil.assertFalse(REDIS_DEF_MAP.containsKey(redisDefinition.getId()), "RedisDefinition[id=" + redisDefinition.getId() + "]重复定义！");

            if (redisDefinition.isPrimary()) {
                if (null == PRIMARY_REDIS_DEF) {
                    PRIMARY_REDIS_DEF = redisDefinition;
                } else {
                    AssertUtil.assertTrue(PRIMARY_REDIS_DEF.getId().equals(redisDefinition.getId()),
                            "不能定义多个 primary RedisDefinition 目前定义了[" + PRIMARY_REDIS_DEF.getId() + "," + redisDefinition.getId() + "]");
                }

            }

            REDIS_DEF_MAP.put(redisDefinition.getId(), redisDefinition);
        }

        return redisDefinitionList;
    }

}
