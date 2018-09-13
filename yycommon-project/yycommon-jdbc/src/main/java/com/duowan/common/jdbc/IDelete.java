package com.duowan.common.jdbc;

/**
 * 删除
 *
 * @author Arvin
 */
public interface IDelete {

    /**
     * 根据主键删除一个数据
     *
     * @param modelType        模型类型
     * @param primaryKeyValues 主键值
     * @param <T>              模型类型
     * @return 返回数据库操作影响行数
     */
    <T> int deleteByPrimaryKey(Class<T> modelType, Object... primaryKeyValues);

    /**
     * 根据主键删除对象
     *
     * @param model 要删除的模型对象
     * @return 返回数据库操作影响行数
     */
    int deleteByPrimaryKey(Object model);

    /**
     * 根据主键删除一个数据
     *
     * @param tableName        指定要操作的数据库表名
     * @param modelType        模型类型
     * @param primaryKeyValues 主键值
     * @param <T>              模型类型
     * @return 返回数据库操作影响行数
     */
    <T> int deleteByPrimaryKey(String tableName, Class<T> modelType, Object... primaryKeyValues);

    /**
     * 根据主键删除对象
     *
     * @param tableName 指定要操作的数据库表名
     * @param model     要删除的模型对象
     * @return 返回数据库操作影响行数
     */
    int deleteByPrimaryKey(String tableName, Object model);

}
