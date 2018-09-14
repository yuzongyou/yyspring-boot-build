package com.duowan.common.redis.util;

import com.duowan.common.redis.model.RedisDefinition;
import com.duowan.common.redis.model.RiseRedisDefinition;
import com.duowan.common.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Arvin
 */
public abstract class RedisDefinitionUtil {

    /**
     * 提取启用的列表
     *
     * @param enabledIds   要启用的ID列表
     * @param redisDefinitionList redisDef 定义列表
     * @return 返回所有启用的 RedisDefinition 定义
     */
    public static List<RedisDefinition> extractEnabledRedisDefList(Set<String> enabledIds, List<RedisDefinition> redisDefinitionList) {

        if (enabledIds == null || enabledIds.isEmpty() || null == redisDefinitionList || redisDefinitionList.isEmpty()) {
            return redisDefinitionList;
        }

        List<RedisDefinition> resultList = new ArrayList<>();
        for (RedisDefinition redisDefinition : redisDefinitionList) {
            if (isRedisDefEnabled(enabledIds, redisDefinition)) {
                resultList.add(redisDefinition);
            }
        }
        return resultList;
    }

    /**
     * 判断 redis 定义是否启用
     *
     * @param enabledIds 启用的 RedisDefinition id ，可以使用通配符 *
     * @param redisDefinition   RedisDefinition 定义
     * @return 返回是否启用
     */
    private static boolean isRedisDefEnabled(Set<String> enabledIds, RedisDefinition redisDefinition) {
        if (enabledIds == null || enabledIds.isEmpty()) {
            return true;
        }
        if (enabledIds.contains(redisDefinition.getId())) {
            return true;
        }
        // 通配符检查
        for (String enabledId : enabledIds) {
            if (CommonUtil.isStartWildcardMatch(redisDefinition.getId(), enabledId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 将 RedisDefinition.id = primaryId 的定义对象设置为 primary
     *
     * @param primaryId    主ID
     * @param redisDefinitionList 定义列表
     * @return 返回原jdbc定义列表
     */
    public static List<RedisDefinition> applyPrimaryJdbcDefList(String primaryId, List<RedisDefinition> redisDefinitionList) {

        if (null == redisDefinitionList || redisDefinitionList.isEmpty()) {
            return redisDefinitionList;
        }
        for (RedisDefinition redisDefinition : redisDefinitionList) {
            redisDefinition.setPrimary(redisDefinition.getId().equals(primaryId));
        }

        return redisDefinitionList;
    }

    public static List<RedisDefinition> autoFillProperties(List<RedisDefinition> redisDefinitionList, Environment environment) {
        List<RedisDefinition> resultList = new ArrayList<>();

        for (RedisDefinition jdbcDef : redisDefinitionList) {

            if (jdbcDef instanceof RiseRedisDefinition) {

                List<RedisDefinition> subList = autoFillProperties((RiseRedisDefinition) jdbcDef, environment);

                if (null != subList && !subList.isEmpty()) {
                    resultList.addAll(subList);
                }
            } else {
                resultList.add(jdbcDef);
            }
        }

        return resultList;
    }

    public static List<RedisDefinition> autoFillProperties(RiseRedisDefinition redisDef, Environment environment) {
        List<RedisDefinition> resultList = new ArrayList<>();

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
