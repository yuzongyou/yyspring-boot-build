package com.duowan.common.jdbc.sqlbuilder;

import java.util.List;

/**
 * 条件语句构造器
 *
 * @author Arvin
 */
public interface WhereBuilder {

    /**
     * 获取Where子句
     *
     * @return 返回where子句
     */
    String getWhereSql();

    /**
     * 获取数组格式的参数
     *
     * @return 返回数组格式的参数
     */
    Object[] getArrayParam();

    /**
     * 获取 List 格式的参数
     *
     * @return 返回参数列表
     */
    List<Object> getListParam();

    /**
     * 获取Sql参数列表
     *
     * @return 返回参数列表
     */
    List<SqlParam> getSqlParamList();

    /**
     * 设置父级的 WhereBuilder
     *
     * @param parent 父级WhereBuilder
     * @return 返回this
     */
    WhereBuilder setParent(WhereBuilder parent);

    /**
     * 返回条件数量，不包含父级条件的数量
     *
     * @return 返回数量
     */
    int conditionCount();

    /**
     * 添加 AND
     *
     * @param subSql 子条件
     * @param params 参数
     * @return 返回this
     */
    WhereBuilder and(String subSql, Object... params);

    /**
     * 添加 OR
     *
     * @param subSql 子条件
     * @param params 参数
     * @return 返回this
     */
    WhereBuilder or(String subSql, Object... params);

    /**
     * 添加 AND 条件， 指定的列等于具体的值
     *
     * @param columnName 列名
     * @param value      值，如果为null则会丢弃条件
     * @return 返回this
     */
    WhereBuilder andEqual(String columnName, Object value);

    /**
     * 添加 OR 条件， 指定的列等于具体的值， 会给两个条件加上括号
     *
     * @param columnName 列名
     * @param value      值，如果为null则会丢弃条件
     * @return 返回this
     */
    WhereBuilder orEqual(String columnName, Object value);

    /**
     * 添加 AND NOT EQUAL 条件， 指定的列等于具体的值
     *
     * @param columnName 列名
     * @param value      值，如果为null则会丢弃条件
     * @return 返回this
     */
    WhereBuilder andNotEqual(String columnName, Object value);

    /**
     * 添加 OR NOT EQUAL 条件， 指定的列等于具体的值， 会给两个条件加上括号
     *
     * @param columnName 列名
     * @param value      值，如果为null则会丢弃条件
     * @return 返回this
     */
    WhereBuilder orNotEqual(String columnName, Object value);

    /**
     * 添加 AND LIKE 条件
     *
     * @param columnName 列名
     * @param value      值，如果为null则不会添加该条件
     * @return 返回this
     */
    WhereBuilder andLike(String columnName, String value);

    /**
     * 添加 OR LIKE 条件
     *
     * @param columnName 列名
     * @param value      值，如果为null则不会添加该条件
     * @return 返回this
     */
    WhereBuilder orLike(String columnName, String value);

    /**
     * 添加 AND is null
     *
     * @param columnName 列名
     * @return 返回this
     */
    WhereBuilder andIsNull(String columnName);

    /**
     * 添加 OR is null
     *
     * @param columnName 列名
     * @return 返回this
     */
    WhereBuilder orIsNull(String columnName);

    /**
     * 添加 AND is null or blank, 添加为 (xxx is null or xxx = '')
     *
     * @param columnName 列名
     * @return 返回this
     */
    WhereBuilder andIsNullOrBlank(String columnName);

    /**
     * 添加 OR is null or blank, 添加为 (xxx is null or xxx = '')
     *
     * @param columnName 列名
     * @return 返回this
     */
    WhereBuilder orIsNullOrBlank(String columnName);

    /**
     * 添加 AND is not null
     *
     * @param columnName 列名
     * @return 返回this
     */
    WhereBuilder andIsNotNull(String columnName);

    /**
     * 添加 OR is not null
     *
     * @param columnName 列名
     * @return 返回this
     */
    WhereBuilder orIsNotNull(String columnName);

    /**
     * 添加 AND is not null or not blank, 添加为 (xxx is not null or xxx != '')
     *
     * @param columnName 列名
     * @return 返回this
     */
    WhereBuilder andIsNotNullOrBlank(String columnName);

    /**
     * 添加 OR is not null or not blank, 添加为 (xxx is not null or xxx != '')
     *
     * @param columnName 列名
     * @return 返回this
     */
    WhereBuilder orIsNotNullOrBlank(String columnName);

    /**
     * 添加 AND BETWEEN 条件
     *
     * @param columnName 列名
     * @param begin      开始区间
     * @param end        结束区间
     * @return 返回this
     */
    WhereBuilder andBetween(String columnName, Object begin, Object end);

    /**
     * 添加 OR BETWEEN 条件
     *
     * @param columnName 列名
     * @param begin      开始区间
     * @param end        结束区间
     * @return 返回this
     */
    WhereBuilder orBetween(String columnName, Object begin, Object end);

    /**
     * 添加 AND IN 子句
     *
     * @param columnName 列名
     * @param subSelect  子查询SQL
     * @param params     参数
     * @return 返回this
     */
    WhereBuilder andIn(String columnName, String subSelect, Object... params);

    /**
     * 添加 OR IN 子句
     *
     * @param columnName 列名
     * @param subSelect  子查询SQL
     * @param params     参数
     * @return 返回this
     */
    WhereBuilder orIn(String columnName, String subSelect, Object... params);

    /**
     * 添加 AND NOT IN 子句
     *
     * @param columnName 列名
     * @param subSelect  子查询SQL
     * @param params     参数
     * @return 返回this
     */
    WhereBuilder andNotIn(String columnName, String subSelect, Object... params);

    /**
     * 添加 OR NOT IN 子句
     *
     * @param columnName 列名
     * @param subSelect  子查询SQL
     * @param params     参数
     * @return 返回this
     */
    WhereBuilder orNotIn(String columnName, String subSelect, Object... params);

    /**
     * 添加 AND xx &gt;= ?
     *
     * @param columnName  列名
     * @param value       值，如果为null则丢弃条件
     * @param acceptEqual 接受等于
     * @return 返回this
     */
    WhereBuilder andGreaterThan(String columnName, Object value, boolean acceptEqual);

    /**
     * 添加 OR xx &gt;= ?
     *
     * @param columnName  列名
     * @param value       值，如果为null则丢弃条件
     * @param acceptEqual 接受等于
     * @return 返回this
     */
    WhereBuilder orGreaterThan(String columnName, Object value, boolean acceptEqual);


    /**
     * 添加 AND xx &lt;= ?
     *
     * @param columnName  列名
     * @param value       值，如果为null则丢弃条件
     * @param acceptEqual 接受等于
     * @return 返回this
     */
    WhereBuilder andLessThan(String columnName, Object value, boolean acceptEqual);

    /**
     * 添加 OR xx &lt;= ?
     *
     * @param columnName  列名
     * @param value       值，如果为null则丢弃条件
     * @param acceptEqual 接受等于
     * @return 返回this
     */
    WhereBuilder orLessThan(String columnName, Object value, boolean acceptEqual);

    /**
     * 添加 AND exists
     *
     * @param subSelect 子查询语句
     * @param params    参数
     * @return 返回 this
     */
    WhereBuilder andExists(String subSelect, Object... params);

    /**
     * 添加 OR exists
     *
     * @param subSelect 子查询语句
     * @param params    参数
     * @return 返回 this
     */
    WhereBuilder orExists(String subSelect, Object... params);

    /**
     * 添加 AND NOT exists
     *
     * @param subSelect 子查询语句
     * @param params    参数
     * @return 返回 this
     */
    WhereBuilder andNotExists(String subSelect, Object... params);

    /**
     * 添加 OR NOT exists
     *
     * @param subSelect 子查询语句
     * @param params    参数
     * @return 返回 this
     */
    WhereBuilder orNotExists(String subSelect, Object... params);
}
