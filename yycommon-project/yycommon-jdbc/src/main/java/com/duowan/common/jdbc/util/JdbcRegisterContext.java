package com.duowan.common.jdbc.util;

import com.duowan.common.jdbc.model.JdbcDefinition;
import com.duowan.common.jdbc.provider.dbtype.DBProvider;
import com.duowan.common.jdbc.provider.pooltype.PoolProvider;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/20 14:42
 */
public class JdbcRegisterContext {

    /** DB 提供者列表 **/
    private List<DBProvider> dbProviderList;
    /** 连接池提供者实例列表 **/
    private List<PoolProvider> poolProviderList;
    /** 主JdbcID **/
    private String primaryId;
    /** 要启用的ID列表，支持通配符 * **/
    private Set<String> enabledIds;
    /** 要禁用的ID列表，支持通配符 * **/
    private Set<String> excludeIds;
    /** JDBC 定义列表 **/
    private List<JdbcDefinition> jdbcDefinitionList;
    /** Bean 注册入口 **/
    private BeanDefinitionRegistry registry;
    /** 环境 **/
    private Environment environment;

    public List<DBProvider> getDbProviderList() {
        return dbProviderList;
    }

    public JdbcRegisterContext setDbProviderList(List<DBProvider> dbProviderList) {
        this.dbProviderList = dbProviderList;
        return this;
    }

    public List<PoolProvider> getPoolProviderList() {
        return poolProviderList;
    }

    public JdbcRegisterContext setPoolProviderList(List<PoolProvider> poolProviderList) {
        this.poolProviderList = poolProviderList;
        return this;
    }

    public String getPrimaryId() {
        return primaryId;
    }

    public JdbcRegisterContext setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
        return this;
    }

    public Set<String> getEnabledIds() {
        return enabledIds;
    }

    public JdbcRegisterContext setEnabledIds(Set<String> enabledIds) {
        this.enabledIds = enabledIds;
        return this;
    }

    public Set<String> getExcludeIds() {
        return excludeIds;
    }

    public JdbcRegisterContext setExcludeIds(Set<String> excludeIds) {
        this.excludeIds = excludeIds;
        return this;
    }

    public List<JdbcDefinition> getJdbcDefinitionList() {
        return jdbcDefinitionList;
    }

    public JdbcRegisterContext setJdbcDefinitionList(List<JdbcDefinition> jdbcDefinitionList) {
        this.jdbcDefinitionList = jdbcDefinitionList;
        return this;
    }

    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    public JdbcRegisterContext setRegistry(BeanDefinitionRegistry registry) {
        this.registry = registry;
        return this;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public JdbcRegisterContext setEnvironment(Environment environment) {
        this.environment = environment;
        return this;
    }
}
