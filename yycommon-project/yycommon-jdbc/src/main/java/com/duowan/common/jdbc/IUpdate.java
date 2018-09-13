package com.duowan.common.jdbc;

import java.util.List;

/**
 * 更新操作
 *
 * @author Arvin
 */
public interface IUpdate {

    /**
     * 根据主键更新模型对象
     *
     * @param model 模型对象
     * @return 返回数据库影响行数
     */
    int updateByPrimaryKey(Object model);

    /**
     * 根据主键更新模型对象
     *
     * @param tableName 指定数据库表名称
     * @param model     模型对象
     * @return 返回数据库影响行数
     */
    int updateByPrimaryKey(String tableName, Object model);

    /**
     * 执行更新语句
     *
     * @param sql    要执行的sql
     * @param params 参数
     * @return 返回影响行数
     */
    int update(String sql, Object... params);

    /**
     * 执行更新语句
     *
     * @param paramList 参数
     * @param sql       要执行的sql
     * @return 返回影响行数
     */
    int update(List<Object> paramList, String sql);

    /**
     * 批量更新
     *
     * @param sql        要更新的sql
     * @param paramsList 参数列表
     * @return 返回影响行数
     */
    int batchUpdate(String sql, List<Object[]> paramsList);

    /**
     * 批量更新
     *
     * @param paramsList 参数列表
     * @param sql        要更新的sql
     * @return 返回影响行数
     */
    int batchUpdate(List<List<Object>> paramsList, String sql);

    /**
     * 仅更新给定限制的条数
     *
     * @param sql         sql语句，不带限制语句如 mysql 的是 limit
     * @param params      参数
     * @param limit       要限制的条数
     * @return 返回更新的数量
     */
    int updateLimit(String sql, int limit, Object... params);

    /**
     * 仅更新给定限制的条数
     *
     * @param sql         sql语句，不带限制语句如 mysql 的是 limit
     * @param paramList   参数
     * @param limit       要限制的条数
     * @return 返回更新的数量
     */
    int updateLimit(List<Object> paramList, String sql, int limit);
}
