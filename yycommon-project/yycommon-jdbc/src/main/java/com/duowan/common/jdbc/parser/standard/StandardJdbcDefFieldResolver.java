package com.duowan.common.jdbc.parser.standard;

import com.duowan.common.jdbc.model.JdbcDef;

/**
 * 标准 JdbcDefField Resolver
 *
 * @author Arvin
 */
public interface StandardJdbcDefFieldResolver {

    /**
     * 处理配置属性
     *
     * @param jdbcDef jdbc定义
     * @param key     完整KEY
     * @param subKey  子key
     * @param value   值
     * @return 如果识别并处理了就返回true，否咋返回false
     */
    boolean resolve(JdbcDef jdbcDef, String key, String subKey, String value);
}
