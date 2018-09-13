package com.duowan.common.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

/**
 * JDBC 操作接口定义，提供常用的接口
 *
 * @author Arvin
 */
public interface Jdbc extends IQuery, IInsert, IUpdate, IDelete {

    /**
     * 获取数据源
     *
     * @return 返回数据源对象
     */
    DataSource getDataSource();

    /**
     * 设置数据源
     *
     * @param dataSource 数据源
     */
    void setDataSource(DataSource dataSource);

    /**
     * 获取JdbcTemplate
     *
     * @return 返回jdbcTemplate
     */
    JdbcTemplate getJdbcTemplate();

    /**
     * 设置 JDBC template
     *
     * @param jdbcTemplate 设置 jdbcTemplate
     */
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    /**
     * 设置事务执行模版
     *
     * @param transactionTemplate 事务执行模版
     */
    void setTransactionTemplate(TransactionTemplate transactionTemplate);

    /**
     * 获取事务执行模版， 更加灵活的事务处理方式
     *
     * @return 返回事务模版对象
     */
    TransactionTemplate getTransactionTemplate();

}
