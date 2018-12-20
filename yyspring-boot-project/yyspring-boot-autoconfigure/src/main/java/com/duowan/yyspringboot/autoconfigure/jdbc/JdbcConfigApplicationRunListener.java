package com.duowan.yyspringboot.autoconfigure.jdbc;

import com.duowan.common.jdbc.model.JdbcDefinition;
import com.duowan.common.jdbc.model.RiseJdbcDefinition;
import com.duowan.common.jdbc.provider.dbtype.DBProvider;
import com.duowan.common.jdbc.provider.pooltype.PoolProvider;
import com.duowan.common.jdbc.util.JdbcRegisterContext;
import com.duowan.common.jdbc.util.JdbcRegisterUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import com.duowan.yyspring.boot.SpringApplicationRunListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/26 15:17
 */
public class JdbcConfigApplicationRunListener extends SpringApplicationRunListenerAdapter {

    private boolean needAutoConfigurer = false;

    public JdbcConfigApplicationRunListener(SpringApplication application, String[] args) {
        super(application, args);
        this.needAutoConfigurer = isClassesImported(
                "com.duowan.common.jdbc.Jdbc"
        );
    }

    @Override
    protected boolean needAutoConfigurer() {
        return needAutoConfigurer;
    }

    @Override
    protected void doContextPrepared(ConfigurableApplicationContext context, BeanDefinitionRegistry registry, ConfigurableEnvironment environment) {
        JdbcProperties jdbcProperties = bindProperties(JdbcProperties.PROPERTIES_PREFIX, JdbcProperties.class);

        if (null != jdbcProperties) {
            doAutoConfiguration(jdbcProperties, registry, environment);
        }
    }

    static void doAutoConfiguration(JdbcProperties jdbcProperties, BeanDefinitionRegistry registry, Environment environment) {

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
            if (StringUtils.isBlank(jdbcDef.getId())) {
                jdbcDef.setId(entry.getKey());
            }

            String dsName = jdbcDef.getName();
            if (aliasMap != null) {
                String aliasDsName = aliasMap.get(dsName);
                if (StringUtils.isNotBlank(aliasDsName)) {
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
            if (StringUtils.isBlank(jdbcDefinition.getId())) {
                jdbcDefinition.setId(entry.getKey());
            }
            resultList.add(jdbcDefinition);
        }

        return resultList;

    }
}
