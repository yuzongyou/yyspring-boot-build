package com.duowan.common.jdbc.provider.dbtype;

import com.duowan.common.jdbc.model.JdbcDef;
import org.springframework.core.env.Environment;

/**
 * 数据库提供者
 *
 * @author Arvin
 */
public interface DBProvider {

    /**
     * 是否支持指定 Jdbc定义的数据库配置
     *
     * @param jdbcDef Jdbc定义
     * @return true 支持，false 不支持
     */
    boolean support(JdbcDef jdbcDef);

    /**
     * 提供数据库驱动
     *
     * @return 返回数据库驱动，如果不支持的话就返回null
     */
    String provideDriverClass();

    /**
     * 获取JDBC实现类
     *
     * @param jdbcDef     jdbc 定义
     * @param environment 环境
     * @return 返回类名
     */
    Class<?> lookupJdbcImplClass(JdbcDef jdbcDef, Environment environment);
}
