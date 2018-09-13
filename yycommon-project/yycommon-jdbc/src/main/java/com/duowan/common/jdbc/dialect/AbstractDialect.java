package com.duowan.common.jdbc.dialect;

import com.duowan.common.jdbc.CountSqlParser;

/**
 * @author Arvin
 */
public abstract class AbstractDialect implements Dialect {

    /**
     * 计算总数的 sql 解析器
     */
    protected CountSqlParser countSqlParser = new CountSqlParser();

    @Override
    public String getCountSql(String sql) {
        return countSqlParser.getSmartCountSql(sql);
    }

    protected int getStartRow(int pageNo, int pageSize) {
        return pageNo > 0 ? (pageNo - 1) * pageSize : 0;
    }

    protected int getEndRow(int pageNo, int pageSize) {
        int startRow = getStartRow(pageNo, pageSize);
        return startRow + pageSize * (pageNo > 0 ? 1 : 0);
    }
}
