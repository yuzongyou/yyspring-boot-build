package com.duowan.common.jdbc;

import java.util.List;

/**
 * 插入语句
 *
 * @author Arvin
 */
public interface IInsert {

    /**
     * 执行一条插入语句
     *
     * @param sql    要执行的sql
     * @param params 参数
     * @return 返回影响行数
     */
    int insert(String sql, Object... params);

    /**
     * 执行一条插入语句
     *
     * @param params 参数
     * @param sql    要执行的sql
     * @return 返回影响行数
     */
    int insert(List<Object> params, String sql);

    /**
     * 执行一个sql并且返回自增主键
     *
     * @param sql         要执行的sql
     * @param keyCallback 主键回调函数
     * @param params      参数
     * @return 返回影响行数
     */
    int insert(String sql, GenerateKeyCallback keyCallback, Object... params);

    /**
     * 执行一个sql并且返回自增主键
     *
     * @param sql         要执行的sql
     * @param keyCallback 主键回调函数
     * @param params      参数
     * @return 返回影响行数
     */
    int insert(String sql, List<Object> params, GenerateKeyCallback keyCallback);

    /**
     * 批量进行插入
     *
     * @param sql        要执行的 sql
     * @param paramsList 参数列表
     * @return 返回影响行数
     */
    int batchInsert(String sql, List<Object[]> paramsList);

    /**
     * 批量进行插入
     *
     * @param sql        要执行的 sql
     * @param paramsList 参数列表
     * @return 返回影响行数
     */
    int batchInsert(List<List<Object>> paramsList, String sql);

    /**
     * 批量插入并且返回数据库自增的主键
     *
     * @param sql         要执行的sql
     * @param paramsList  参数列表
     * @param keyCallback 主键回调函数
     * @return 返回数据库影响行数
     */
    int batchInsert(String sql, List<Object[]> paramsList, GenerateKeyCallback keyCallback);

    /**
     * 批量插入并且返回数据库自增的主键
     *
     * @param paramsList  参数列表
     * @param sql         要执行的sql
     * @param keyCallback 主键回调函数
     * @return 返回数据库影响行数
     */
    int batchInsert(List<List<Object>> paramsList, String sql, GenerateKeyCallback keyCallback);

    /**
     * 插入一个对象
     *
     * @param model 模型对象
     * @return 返回数据操作影响行数
     */
    int insert(Object model);

    /**
     * 插入一个对象
     *
     * @param model                模型对象
     * @param fillAutoIncrementKey 是否自动填充自增主键, 只有自增主键才生效
     * @return 返回数据操作影响行数
     */
    int insert(Object model, boolean fillAutoIncrementKey);

    /**
     * 插入一个对象
     *
     * @param tableName 指定数据库表名称
     * @param model     模型对象
     * @return 返回数据操作影响行数
     */
    int insert(String tableName, Object model);

    /**
     * 插入一个对象
     *
     * @param tableName            指定数据库表名称
     * @param model                模型对象
     * @param fillAutoIncrementKey 是否自动填充自增主键
     * @return 返回数据操作影响行数
     */
    int insert(String tableName, Object model, boolean fillAutoIncrementKey);
}
