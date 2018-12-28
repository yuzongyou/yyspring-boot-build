package com.duowan.yyspringboot.autoconfigure.jdbc;

import com.duowan.common.jdbc.model.JdbcDefinition;
import com.duowan.common.jdbc.model.RiseJdbcDefinition;
import com.duowan.common.jdbc.provider.dbtype.DBProvider;
import com.duowan.common.jdbc.provider.pooltype.PoolProvider;
import com.duowan.common.jdbc.util.JdbcRegisterContext;
import com.duowan.common.jdbc.util.JdbcRegisterUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import com.duowan.common.utils.StringUtil;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/28 14:04
 */
public class JdbcSpringRegister {

    private JdbcSpringRegister() {
        throw new IllegalStateException("Utility class");
    }

    public static void registerJdbcBeans(JdbcProperties jdbcProperties, BeanDefinitionRegistry registry, Environment environment) {
        List<DBProvider> dbProviderList = ReflectUtil.newInstancesByDefaultConstructor(DBProvider.class, jdbcProperties.getDbProviderClasses());
        List<PoolProvider> poolProviderList = ReflectUtil.newInstancesByDefaultConstructor(PoolProvider.class, jdbcProperties.getPoolProviderClasses());

        List<JdbcDefinition> jdbcDefinitionList = lookupJdbcDefList(jdbcProperties);

        // 注册Bean
        JdbcRegisterUtil.registerJdbcBeanDefinitions(
                new JdbcRegisterContext()
                        .setDbProviderList(dbProviderList)
                        .setPoolProviderList(poolProviderList)
                        .setPrimaryId(jdbcProperties.getPrimaryId())
                        .setEnabledIds(jdbcProperties.getEnabledIds())
                        .setExcludeIds(jdbcProperties.getExcludeIds())
                        .setJdbcDefinitionList(jdbcDefinitionList)
                        .setRegistry(registry)
                        .setEnvironment(environment));
    }

    private static List<JdbcDefinition> lookupJdbcDefList(JdbcProperties jdbcProperties) {
        List<JdbcDefinition> resultList = new ArrayList<>();

        CommonUtil.appendList(resultList, lookupStandardJdbcDefList(jdbcProperties));
        CommonUtil.appendList(resultList, lookupRiseJdbcDefList(jdbcProperties));

        return resultList;

    }

    private static List<JdbcDefinition> lookupRiseJdbcDefList(JdbcProperties jdbcProperties) {
        Map<String, RiseJdbcDefinition> riseMap = jdbcProperties.getRises();
        List<JdbcDefinition> resultList = new ArrayList<>();

        if (null == riseMap || riseMap.isEmpty()) {
            return resultList;
        }

        Map<String, String> aliasMap = jdbcProperties.getRiseAlias();

        for (Map.Entry<String, RiseJdbcDefinition> entry : riseMap.entrySet()) {
            RiseJdbcDefinition jdbcDef = entry.getValue();
            if (StringUtil.isBlank(jdbcDef.getId())) {
                jdbcDef.setId(entry.getKey());
            }

            String dsName = jdbcDef.getName();
            if (aliasMap != null) {
                String aliasDsName = aliasMap.get(dsName);
                if (StringUtil.isNotBlank(aliasDsName)) {
                    jdbcDef.setName(aliasDsName);
                }
            }

            resultList.add(jdbcDef);
        }

        return resultList;
    }

    private static List<JdbcDefinition> lookupStandardJdbcDefList(JdbcProperties jdbcProperties) {

        Map<String, JdbcDefinition> standardMap = jdbcProperties.getStandards();
        List<JdbcDefinition> resultList = new ArrayList<>();

        if (null == standardMap || standardMap.isEmpty()) {
            return resultList;
        }

        for (Map.Entry<String, JdbcDefinition> entry : standardMap.entrySet()) {
            JdbcDefinition jdbcDefinition = entry.getValue();
            if (StringUtil.isBlank(jdbcDefinition.getId())) {
                jdbcDefinition.setId(entry.getKey());
            }
            resultList.add(jdbcDefinition);
        }

        return resultList;

    }
}
