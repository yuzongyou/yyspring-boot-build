package com.duowan.common.jdbc.statement;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.SqlProvider;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Arvin
 */
public class SimplePreparedStatementCreator implements PreparedStatementCreator, SqlProvider {

    private final boolean generateKey;

    private final String sql;

    private final PreparedStatementSetter preparedStatementSetter;

    public SimplePreparedStatementCreator(String sql, boolean generateKey) {
        this(sql, generateKey, null);
    }

    public SimplePreparedStatementCreator(String sql, boolean generateKey, PreparedStatementSetter preparedStatementSetter) {
        Assert.notNull(sql, "SQL must not be null");
        this.sql = sql;
        this.generateKey = generateKey;
        this.preparedStatementSetter = preparedStatementSetter;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement preparedStatement;
        if (generateKey) {
            preparedStatement = con.prepareStatement(this.sql, Statement.RETURN_GENERATED_KEYS);
        } else {
            preparedStatement = con.prepareStatement(this.sql);
        }

        if (null != preparedStatementSetter) {
            preparedStatementSetter.setValues(preparedStatement);
        }

        return preparedStatement;
    }

    @Override
    public String getSql() {
        return this.sql;
    }
}
