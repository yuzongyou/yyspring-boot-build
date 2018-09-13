package com.duowan.common.jdbc.statement;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Arvin
 */
public class BatchInsertPreparedStatementSetter implements BatchPreparedStatementSetter {

    /** 参数列表 */
    private final List<Object[]> paramsList;

    public BatchInsertPreparedStatementSetter(List<Object[]> paramsList) {
        this.paramsList = paramsList;
    }

    @Override
    public void setValues(PreparedStatement ps, int i) throws SQLException {
        Object[] params = paramsList.get(i);
        setStatementParameters(params, ps, new int[0]);
    }

    @Override
    public int getBatchSize() {
        return null == paramsList ? 0 : paramsList.size();
    }

    protected static void setStatementParameters(Object[] values, PreparedStatement ps, int[] columnTypes)
            throws SQLException {

        int colIndex = 0;
        for (Object value : values) {
            colIndex++;
            if (value instanceof SqlParameterValue) {
                SqlParameterValue paramValue = (SqlParameterValue) value;
                StatementCreatorUtils.setParameterValue(ps, colIndex, paramValue, paramValue.getValue());
            } else {
                int colType;
                if (columnTypes == null || columnTypes.length < colIndex) {
                    colType = SqlTypeValue.TYPE_UNKNOWN;
                } else {
                    colType = columnTypes[colIndex - 1];
                }
                StatementCreatorUtils.setParameterValue(ps, colIndex, colType, value);
            }
        }
    }
}
