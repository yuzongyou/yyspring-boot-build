package com.duowan.yyspringboot.autoconfigure.jdbc;

import com.duowan.common.jdbc.Jdbc;
import com.duowan.common.jdbc.model.JdbcDefinition;
import com.duowan.common.jdbc.model.RiseJdbcDefinition;
import com.duowan.common.jdbc.provider.dbtype.DBProvider;
import com.duowan.common.jdbc.provider.pooltype.PoolProvider;
import com.duowan.common.jdbc.util.JdbcRegisterUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import com.duowan.yyspring.boot.AppContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/13 12:50
 */
@Configuration
@ConditionalOnClass({Jdbc.class})
@EnableConfigurationProperties(JdbcProperties.class)
public class JdbcAutoConfiguration {

    private Environment environment;

    private JdbcProperties jdbcProperties;

    private BeanDefinitionRegistry registry;

    public JdbcAutoConfiguration(Environment environment, JdbcProperties jdbcProperties) {
        this.environment = environment;
        this.jdbcProperties = jdbcProperties;
        this.registry = AppContext.getBeanDefinitionRegistry();

        this.doJdbcAutoRegister();
    }

    private void doJdbcAutoRegister() {

        List<DBProvider> dbProviderList = newInstances(DBProvider.class, jdbcProperties.getDbProviderClasses());
        List<PoolProvider> poolProviderList = newInstances(PoolProvider.class, jdbcProperties.getPoolProviderClasses());

        List<JdbcDefinition> jdbcDefinitionList = lookupJdbcDefList();

        // 注册Bean
        JdbcRegisterUtil.registerJdbcBeanDefinitions(
                dbProviderList,
                poolProviderList,
                jdbcProperties.getPrimaryId(),
                jdbcProperties.getEnabledIds(),
                jdbcDefinitionList,
                registry,
                environment);

    }

    private List<JdbcDefinition> lookupJdbcDefList() {
        List<JdbcDefinition> resultList = new ArrayList<>();

        CommonUtil.appendList(resultList, lookupStandardJdbcDefList());
        CommonUtil.appendList(resultList, lookupRiseJdbcDefList());

        return resultList;

    }

    private List<JdbcDefinition> lookupRiseJdbcDefList() {
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

    private List<JdbcDefinition> lookupStandardJdbcDefList() {

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

    private <T> List<T> newInstances(Class<T> requireType, Set<String> classes) {

        if (null == classes || classes.isEmpty()) {
            return new ArrayList<>(0);
        }

        List<T> instanceList = new ArrayList<>();
        for (String className : classes) {
            if (StringUtils.isNotBlank(className)) {
                T instance = ReflectUtil.newInstanceByDefaultConstructor(requireType, className.trim());
                if (null != instance) {
                    instanceList.add(instance);
                }
            }
        }
        return instanceList;
    }
}
