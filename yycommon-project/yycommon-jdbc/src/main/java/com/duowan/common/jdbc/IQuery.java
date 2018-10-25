package com.duowan.common.jdbc;

import com.duowan.common.jdbc.sqlbuilder.AbstractSelectBuilder;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

/**
 * Jdbc 查询接口定义
 *
 * @author Arvin
 */
public interface IQuery {

    /**
     * 根据主键查询对象，参数可以数数组，但是不能是List
     *
     * @param modelType        要求的类型
     * @param primaryKeyValues 主键参数列表，顺序就是模型类定义主键的顺序
     * @param <T>              返回类型
     * @return 不存在则返回null
     */
    <T> T queryByPrimaryKey(Class<T> modelType, Object... primaryKeyValues);

    /**
     * 根据主键查询对象，参数可以数数组，但是不能是List
     *
     * @param tableName        自定义数据库表名称
     * @param modelType        要求的类型
     * @param primaryKeyValues 主键参数列表，顺序就是模型类定义主键的顺序
     * @param <T>              返回类型
     * @return 不存在则返回null
     */
    <T> T queryByPrimaryKey(String tableName, Class<T> modelType, Object... primaryKeyValues);

    /**
     * 查询单个对象, 参数可以数数组，但是不能是List
     *
     * @param sql         查询sql，该sql必须是只能返回一个行记录
     * @param requireType 要求的类型
     * @param params      参数
     * @param <T>         返回类型
     * @return 不存在则返回null
     */
    <T> T queryModel(String sql, Class<T> requireType, Object... params);

    /**
     * 查询单个对象, 参数是List
     *
     * @param sql         查询sql，该sql必须是只能返回一个行记录
     * @param paramList   参数列表
     * @param requireType 要求的类型
     * @param <T>         返回类型
     * @return 不存在则返回null
     */
    <T> T queryModel(String sql, List<Object> paramList, Class<T> requireType);

    /**
     * 查询单个对象, 参数可以数数组，但是不能是List
     *
     * @param sql         查询sql，该sql必须是只能返回一个行记录
     * @param rowMapper   自定义行映射
     * @param requireType 要求的类型
     * @param params      参数
     * @param <T>         返回类型
     * @return 不存在则返回null
     */
    <T> T queryModel(String sql, RowMapper<T> rowMapper, Class<T> requireType, Object... params);

    /**
     * 查询单个对象, 参数是List
     *
     * @param sql         查询sql，该sql必须是只能返回一个行记录
     * @param paramList   参数列表
     * @param requireType 要求的类型
     * @param rowMapper   自定义行映射
     * @param <T>         返回类型
     * @return 不存在则返回null
     */
    <T> T queryModel(String sql, List<Object> paramList, Class<T> requireType, RowMapper<T> rowMapper);

    /**
     * 查询单个MAP, 参数可以数数组，但是不能是List
     *
     * @param sql    查询sql，该sql必须是只能返回一个行记录
     * @param params 参数
     * @return 不存在则返回null
     */
    Map<String, Object> queryMap(String sql, Object... params);

    /**
     * 查询单个MAP, 参数数列表
     *
     * @param paramList 参数列表
     * @param sql       查询sql，该sql必须是只能返回一个行记录
     * @return 不存在则返回null
     */
    Map<String, Object> queryMap(List<Object> paramList, String sql);

    /**
     * 查询多个对象, 参数可以数数组，但是不能是List
     *
     * @param sql         查询sql
     * @param requireType 要求的类型
     * @param params      参数
     * @param <T>         返回类型
     * @return 不存在则返回空list
     */
    <T> List<T> queryModelList(String sql, Class<T> requireType, Object... params);

    /**
     * 查询多个对象, 参数是List
     *
     * @param sql         查询sql
     * @param paramList   参数列表
     * @param requireType 要求的类型
     * @param <T>         返回类型
     * @return 不存在则返回空list
     */
    <T> List<T> queryModelList(String sql, List<Object> paramList, Class<T> requireType);

    /**
     * 查询多个对象, 参数可以数数组，但是不能是List
     *
     * @param sql         查询sql
     * @param rowMapper   行映射
     * @param requireType 要求的类型
     * @param params      参数
     * @param <T>         返回类型
     * @return 不存在则返回空list
     */
    <T> List<T> queryModelList(String sql, RowMapper<T> rowMapper, Class<T> requireType, Object... params);

    /**
     * 查询多个对象, 参数是List
     *
     * @param sql         查询sql
     * @param paramList   参数列表
     * @param requireType 要求的类型
     * @param rowMapper   行映射
     * @param <T>         返回类型
     * @return 不存在则返回空list
     */
    <T> List<T> queryModelList(String sql, List<Object> paramList, Class<T> requireType, RowMapper<T> rowMapper);

    /**
     * 查询多个MAP, 参数可以数数组，但是不能是List
     *
     * @param sql    查询sql
     * @param params 参数
     * @return 不存在则返回空list
     */
    List<Map<String, Object>> queryMapList(String sql, Object... params);

    /**
     * 查询多个MAP, 参数数列表
     *
     * @param paramList 参数列表
     * @param sql       查询sql
     * @return 不存在则返回空list
     */
    List<Map<String, Object>> queryMapList(List<Object> paramList, String sql);

    /**
     * 查询Integer
     *
     * @param sql    sql
     * @param params 参数
     * @return 不存在则返回null
     */
    Integer queryInteger(String sql, Object... params);

    /**
     * 查询 Long
     *
     * @param sql    sql
     * @param params 参数
     * @return 不存在则返回null
     */
    Long queryLong(String sql, Object... params);

    /**
     * 查询 String
     *
     * @param sql    sql
     * @param params 参数
     * @return 不存在则返回null
     */
    String queryString(String sql, Object... params);

    /**
     * 查询 单列 数据，只能返回单列单行数据
     *
     * @param sql          查询语句
     * @param defaultValue 默认值（如果查询返回null则使用默认值）
     * @param requireType  要查询的类型
     * @param params       参数
     * @param <T>          结果类型
     * @return 没有就返回 0
     */
    <T> T querySingleColumn(String sql, T defaultValue, Class<T> requireType, Object... params);

    /**
     * 查询 单列 数据，只能返回单列单行数据
     *
     * @param paramList    参数
     * @param defaultValue 默认值（如果查询返回null则使用默认值）
     * @param requireType  要查询的类型
     * @param sql          查询语句
     * @param <T>          结果类型
     * @return 没有就返回 0
     */
    <T> T querySingleColumn(List<Object> paramList, T defaultValue, Class<T> requireType, String sql);

    /**
     * 查询 单列 数据，只能返回单列数据
     *
     * @param sql         查询语句
     * @param requireType 列类型
     * @param params      参数
     * @param <T>         结果类型
     * @return 不存在则返回空list
     */
    <T> List<T> querySingleColumnList(String sql, Class<T> requireType, Object... params);

    /**
     * 查询 单列 数据，只能返回单列数据
     *
     * @param paramList   参数
     * @param requireType 列类型
     * @param sql         查询语句
     * @param <T>         结果类型
     * @return 不存在则返回空list
     */
    <T> List<T> querySingleColumnList(List<Object> paramList, Class<T> requireType, String sql);

    /**
     * 查询字符串列表
     *
     * @param sql    sql
     * @param params 参数
     * @return 返回字符串列表，不存在则返回空列表
     */
    List<String> queryStringList(String sql, Object... params);

    /**
     * 查询Integer列表
     *
     * @param sql    sql
     * @param params 参数
     * @return 返回字符串列表，不存在则返回空列表
     */
    List<Integer> queryIntegerList(String sql, Object... params);

    /**
     * 查询Integer列表
     *
     * @param sql    sql
     * @param params 参数
     * @return 返回字符串列表，不存在则返回空列表
     */
    List<Long> queryLongList(String sql, Object... params);

    /**
     * 查询分页数据对象
     *
     * @param sql         查询sql
     * @param requireType 数据类型
     * @param pageNo      页码
     * @param pageSize    查询数量
     * @param params      参数列表
     * @param <T>         结果类型
     * @return 始终返回一个非null
     */
    <T> Page<T> queryPage(String sql, Class<T> requireType, int pageNo, int pageSize, Object... params);

    /**
     * 查询分页数据对象
     *
     * @param sql         查询sql
     * @param requireType 数据类型
     * @param paramList   参数列表
     * @param pageNo      页码
     * @param pageSize    查询数量
     * @param <T>         结果类型
     * @return 始终返回一个非null
     */
    <T> Page<T> queryPage(String sql, Class<T> requireType, List<Object> paramList, int pageNo, int pageSize);

    /**
     * 分页查询
     *
     * @param requiredType   类型
     * @param queryCondition 条件
     * @param pageNo         页码
     * @param pageSize       每页数量
     * @param tableName      指定数据库表名称
     * @param configurer     配置
     * @param <T>            模型类型
     * @return 数据列表
     */
    <T> Page<T> queryPage(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize, String tableName, SqlBuilderConfigurer<AbstractSelectBuilder<?>> configurer);

    /**
     * 分页查询
     *
     * @param requiredType   类型
     * @param queryCondition 条件
     * @param pageNo         页码
     * @param pageSize       每页数量
     * @param tableName      指定数据库表名称
     * @param <T>            模型类型
     * @return 数据列表
     */
    <T> Page<T> queryPage(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize, String tableName);

    /**
     * 分页查询
     *
     * @param requiredType   类型
     * @param queryCondition 条件
     * @param pageNo         页码
     * @param pageSize       每页数量
     * @param <T>            模型类型
     * @return 数据列表
     */
    <T> Page<T> queryPage(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize);


    /**
     * 查询模型列表
     *
     * @param requiredType   类型
     * @param queryCondition 条件
     * @param pageNo         页码
     * @param pageSize       每页数量
     * @param tableName      指定数据库表名称
     * @param configurer     配置
     * @param <T>            模型类型
     * @return 数据列表
     */
    <T> List<T> queryModelList(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize, String tableName, SqlBuilderConfigurer<AbstractSelectBuilder<?>> configurer);

    /**
     * 查询模型列表
     *
     * @param requiredType   类型
     * @param queryCondition 条件
     * @param pageNo         页码
     * @param pageSize       每页数量
     * @param tableName      指定数据库表名称
     * @param <T>            模型类型
     * @return 数据列表
     */
    <T> List<T> queryModelList(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize, String tableName);

    /**
     * 查询模型列表
     *
     * @param requiredType   类型
     * @param queryCondition 条件
     * @param pageNo         页码
     * @param pageSize       每页数量
     * @param <T>            模型类型
     * @return 数据列表
     */
    <T> List<T> queryModelList(Class<T> requiredType, Object queryCondition, int pageNo, int pageSize);
}
