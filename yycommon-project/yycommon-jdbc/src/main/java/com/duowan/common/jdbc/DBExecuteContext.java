package com.duowan.common.jdbc;

/**
 * @author Arvin
 */
public class DBExecuteContext {

    /** 执行语句 */
    private final String sql;

    /** 参数 */
    private final Object[] params;

    public DBExecuteContext(String sql, Object[] params) {
        this.sql = sql;
        this.params = params;
    }

    public String getSql() {
        return sql;
    }

    public Object[] getParams() {
        return params;
    }
}
