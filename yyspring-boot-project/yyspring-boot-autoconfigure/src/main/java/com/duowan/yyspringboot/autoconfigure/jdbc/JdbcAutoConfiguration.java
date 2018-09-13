package com.duowan.yyspringboot.autoconfigure.jdbc;

import com.duowan.common.jdbc.Jdbc;
import com.duowan.common.jdbc.auto.JdbcRegisterUtil;
import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.jdbc.model.RiseJdbcDef;
import com.duowan.common.jdbc.provider.dbtype.DBProvider;
import com.duowan.common.jdbc.provider.pooltype.PoolProvider;
import com.duowan.common.jdbc.util.JdbcDefHelper;
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

        this.doJdbcRegister();
    }

    private void doJdbcRegister() {

        List<DBProvider> dbProviderList = newInstances(DBProvider.class, jdbcProperties.getDbProviderClasses());
        List<PoolProvider> poolProviderList = newInstances(PoolProvider.class, jdbcProperties.getPoolProviderClasses());

        List<JdbcDef> jdbcDefList = lookupJdbcDefList();

        // 注册Bean
        JdbcRegisterUtil.doJdbcBeanDefinitionsRegister(
                dbProviderList,
                poolProviderList,
                jdbcProperties.getPrimaryId(),
                jdbcProperties.getEnabledIds(),
                jdbcDefList,
                registry,
                environment);

    }

    private List<JdbcDef> lookupJdbcDefList() {
        List<JdbcDef> resultList = new ArrayList<>();

        CommonUtil.appendList(resultList, lookupStandardJdbcDefList());
        CommonUtil.appendList(resultList, lookupRiseJdbcDefList());

        return resultList;

    }

    private List<JdbcDef> lookupRiseJdbcDefList() {
        Map<String, RiseJdbcDef> riseMap = jdbcProperties.getRises();
        Map<String, String> aliasMap = jdbcProperties.getRiseAlias();
        List<JdbcDef> resultList = new ArrayList<>();

        if (null == riseMap || riseMap.isEmpty()) {
            return resultList;
        }

        for (Map.Entry<String, RiseJdbcDef> entry : riseMap.entrySet()) {
            RiseJdbcDef jdbcDef = entry.getValue();
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

    private List<JdbcDef> lookupStandardJdbcDefList() {

        Map<String, JdbcDef> standardMap = jdbcProperties.getStandards();
        List<JdbcDef> resultList = new ArrayList<>();

        if (null == standardMap || standardMap.isEmpty()) {
            return resultList;
        }

        for (Map.Entry<String, JdbcDef> entry : standardMap.entrySet()) {
            JdbcDef jdbcDef = entry.getValue();
            if (StringUtils.isBlank(jdbcDef.getId())) {
                jdbcDef.setId(entry.getKey());
            }
            resultList.add(jdbcDef);
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
