package com.duowan.common.jdbc.dialect;

/**
 * 数据库方言， mysql，oracle，sqlserver ......
 *
 * @author Arvin
 */
public interface Dialect {

    /**
     * 解析一个查询语句为计算总数的sql
     *
     * @param sql sql语句
     * @return 返回计算总数的语句
     */
    String getCountSql(String sql);

    /**
     * 解析一个查询语句为查询分页数据的语句
     *
     * @param sql      sql语句
     * @param pageNo   页码
     * @param pageSize 查询数量
     * @return 返回查询数据的SQL
     */
    String getPagingSql(String sql, int pageNo, int pageSize);

    /**
     * 转换为限制条数的更新 sql， 包括UPDATE 和DELETE
     *
     * @param sql         要转换的sql
     * @param startPageNo 要从哪一页开始更新操作
     * @param limit       要更新多少条
     * @return 返回带有限制数量的语句
     */
    String getUpdateLimitSql(String sql, int startPageNo, int limit);

    /**
     * 包装列名，主要处理关键字，不同数据库的方式还不一样
     *
     * @param columnName 列名称
     * @return 返回包裹后的名称
     */
    String wrapColumnName(String columnName);

    /**
     * 包装表名，主要处理关键字，不同数据库的方式还不一样
     *
     * @param tableName 表名称
     * @return 返回包裹后的名称
     */
    String wrapTableName(String tableName);
}
