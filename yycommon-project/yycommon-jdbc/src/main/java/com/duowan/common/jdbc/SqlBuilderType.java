package com.duowan.common.jdbc;

/**
 * Sql 构建类型
 *
 * @author Arvin
 */
public enum SqlBuilderType {

    /**
     * 插入语句
     */
    INSERT,

    /**
     * 更新语句
     */
    UPDATE,

    /**
     * 删除语句
     */
    DELETE,

    /**
     * Where 子句
     */
    WHERE,

    /**
     * SELECT 语句
     */
    SELECT,

    /**
     * 创表语句
     */
    CREATE,

    /**
     * 删除表语句
     */
    DROP
}
