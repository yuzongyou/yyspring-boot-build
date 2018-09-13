package com.duowan.common.jdbc.parser.rise;

import com.duowan.common.jdbc.model.RiseJdbcDef;

import java.util.Map;

/**
 * Rise JdbcDefField Resolver
 *
 * @author Arvin
 */
public interface RiseJdbcDefFieldResolver {

    /**
     * 处理配置属性
     *
     * @param aliasMap 别名MAP
     * @param jdbcDef  jdbc定义
     * @param key      完整KEY
     * @param subKey   子key
     * @param value    值
     * @return 如果识别并处理了就返回true，否咋返回false
     */
    boolean resolve(Map<String, String> aliasMap, RiseJdbcDef jdbcDef, String key, String subKey, String value);
}
