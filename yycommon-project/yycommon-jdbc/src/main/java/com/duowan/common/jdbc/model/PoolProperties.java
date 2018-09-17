package com.duowan.common.jdbc.model;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.tomcat.jdbc.pool.DataSource;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/17 9:51
 */
public class PoolProperties {

    /** Druid 数据库连接池配置 */
    private DruidDataSource druid;

    /** Hikari 连接池配置 */
    private HikariDataSource hikari;

    /** Dbcp2 连接池配置 */
    private BasicDataSource dbcp2;

    /** c3p0 数据库连接池配置 */
    private ComboPooledDataSource c3p0;

    /** Tomcat 连接池配置 */
    private DataSource tomcat;

    public DruidDataSource getDruid() {
        return druid;
    }

    public void setDruid(DruidDataSource druid) {
        this.druid = druid;
    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    public void setHikari(HikariDataSource hikari) {
        this.hikari = hikari;
    }

    public BasicDataSource getDbcp2() {
        return dbcp2;
    }

    public void setDbcp2(BasicDataSource dbcp2) {
        this.dbcp2 = dbcp2;
    }

    public ComboPooledDataSource getC3p0() {
        return c3p0;
    }

    public void setC3p0(ComboPooledDataSource c3p0) {
        this.c3p0 = c3p0;
    }

    public DataSource getTomcat() {
        return tomcat;
    }

    public void setTomcat(DataSource tomcat) {
        this.tomcat = tomcat;
    }
}
