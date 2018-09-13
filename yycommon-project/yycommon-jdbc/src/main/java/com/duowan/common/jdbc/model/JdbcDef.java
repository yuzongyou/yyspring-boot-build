package com.duowan.common.jdbc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * JdbcDef Jdbc 配置定义
 *
 * @author Arvin
 */
public class JdbcDef {

    /**
     * 数据源ID，JVM中唯一标识一个数据源，是后续构造 DataSource, JdbcTemplate, TransactionManager, TransactionTemplate， Jdbc 的前缀
     */
    private String id;

    /**
     * 数据库类型， 默认是 MYSQL
     */
    private DBType dbType = DBType.MYSQL;

    /**
     * 数据库帐号
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 是否主数据源，默认是 false，如果都为false会以第一个 JdbcDef 为默认数据源
     */
    private boolean primary = false;

    /**
     * JDBC 连接URL
     */
    private String jdbcUrl;


    /**
     * 是否支持事务，默认不支持
     */
    private boolean supportTx = false;

    /**
     * 是否启用声明式事务，默认是启用，当然首先 要支持事务
     * <p>
     * 声明式事务管理约定：
     * 以以下结尾的Bean会进行类的自动代理：
     * Service, ServiceImpl
     * <p>
     * 以下开头的方法名，将会开启事务： add*，save*，insert*，update*，delete*，remove*，drop*，audit*，create*，apply*
     * <p>
     * 以下方法是只读的：get*,find*,select*,query*
     * <p>
     * 因此在实际开发的时候，需要谨慎命名方法名
     * <p>
     * 注意： 如果你想覆盖默认的配置，比如你的方法名称是 get*, 但是你又想支持事务，那么你可以使用 @Transactional 注解
     * 同样的，如果你的方法名是 add*, 但是又不想支持事务，也可以用
     *
     * @Transactional(propagation = Propagation.NOT_SUPPORTED)
     * 即使用 @Transactional 可以覆盖默认的事务配置
     */
    private boolean proxyTx = false;

    /**
     * 是否初始化 JDBC 实例，默认是启用的，会创建 Jdbc接口实现类
     */
    private boolean initJdbc = true;

    /**
     * 数据库连接池类型，默认是 Druid
     */
    private PoolType poolType = PoolType.DRUID;

    /**
     * 数据库连接池使用的数据源，如果设置了将使用这个类，否则根据连接池类型自己计算
     */
    private String dsclazz;

    /**
     * JDBC 驱动包
     */
    private String driverClass;

    /**
     * 连接池配置，会把 pool.param.xxx 所有的填充进来
     */
    private Map<String, String> poolConfig = new HashMap<String, String>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DBType getDbType() {
        return dbType;
    }

    public void setDbType(DBType dbType) {
        this.dbType = dbType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public boolean isSupportTx() {
        return supportTx;
    }

    public void setSupportTx(boolean supportTx) {
        this.supportTx = supportTx;
    }

    public boolean isProxyTx() {
        return proxyTx;
    }

    public void setProxyTx(boolean proxyTx) {
        this.proxyTx = proxyTx;
    }

    public boolean isInitJdbc() {
        return initJdbc;
    }

    public void setInitJdbc(boolean initJdbc) {
        this.initJdbc = initJdbc;
    }

    public PoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(PoolType poolType) {
        this.poolType = poolType;
    }

    public String getDsclazz() {
        return dsclazz;
    }

    public void setDsclazz(String dsclazz) {
        this.dsclazz = dsclazz;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public Map<String, String> getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(Map<String, String> poolConfig) {
        this.poolConfig = poolConfig;
    }
}
