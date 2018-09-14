package com.duowan.common.jdbc.provider.pooltype;

import com.duowan.common.jdbc.model.JdbcDefinition;

import java.util.Map;

/**
 * 连接池提供者
 *
 * @author Arvin
 */
public interface PoolProvider {

    /**
     * 是否支持指定 Jdbc定义的数据库配置
     *
     * @param jdbcDefinition Jdbc定义
     * @return true 支持，false 不支持
     */
    boolean support(JdbcDefinition jdbcDefinition);

    /**
     * 提供一个连接池类
     *
     * @return 返回连接池类全路径，如果不支持就返回 null
     */
    String provideDsClass();

    /**
     * 应用默认配置
     *
     * @param poolConfig 连接池配置
     * @return 返回经过默认配置好的配置MAP
     */
    Map<String, String> applyDefaultPoolConfig(Map<String, String> poolConfig);

    /**
     * 数据库驱动属性名称
     *
     * @return 返回名称
     */
    String getDriverFieldName();

    /**
     * 用户名属性名称
     *
     * @return 返回名称
     */
    String getUsernameFieldName();

    /**
     * 密码属性名称
     *
     * @return 返回名称
     */
    String getPasswordFieldName();

    /**
     * JDBCURL 属性名称
     *
     * @return 返回名称
     */
    String getJdbcUrlFieldName();
}
