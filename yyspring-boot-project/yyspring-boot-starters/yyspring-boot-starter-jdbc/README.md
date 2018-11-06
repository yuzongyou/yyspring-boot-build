
# JDBC 支持
    1. 自动识别 jdbc 数据源并自动注册 DataSource, JdbcTemplate, TransactionManager, TransactionTemplate
    2. 实现数据源激活策略并提供扩展（多模块项目定义了公共数据源，但是不同模块用到的数据源不尽相同）

# JDBC 配置约定
    定义一个 jdbc 数据源：
    基本前缀：
        yyspring.jdbc.standards.[DatasourceName].
        
        yyspring.jdbc.standards:        jdbc 固定前缀
        DatasourceName:     数据源标识，如 user, 这个很关键，后续自动注册的 Bean 的名称会根据这个来定义，比如 名称为 user，那么会注册Bean：
                         userDataSource, userJdbcTemplate, userTransactionManager, userTransactionTemplate
    数据库基本配置：
        前缀均为： jdbc.std.[DatabaseType].[DatasourceName].
        
        dbType:         数据库类型，目前仅支持 mysql， 后续可增加其他支持如sqlserver, oracle......
        jdbcUrl:        必填，数据库连接，如：jdbc:mysql://localhost:3306/test
        
        username:       选填，用户名，默认是root
        password:       选填，密码，默认是空字符串
        supportTx:      选填，是否需要支持事务， 默认是 false， 如果支持则会创建相应的 TransactionManager, TransactionTemplate，否则不会创建
        proxyTx:        选填，是否支持声明式事务，默认是 false，支持声明式事务，创建，TransactionInterceptor
        initJdbc:       选填，默认是true，即会注册 Jdbc 接口的Bean
        driverClass:    选填，JDBC驱动包，默认是根据数据库类型自动导入，如mysql=com.mysql.jdbc.Driver

    连接池配置：
        连接池，默认是启用的，全部都可以使用默认配置，配置前缀均为：
            jdbc.std.[DatabaseType].[DatasourceName].pool
        
        type:           选填，数据源提供者，默认是采用 druid，支持 dbcp, dbcp2, druid, tomcat, HikarCP
        dsclazz:        选填，数据源具体实现类，如果为空则根据provider来配置默认的数据库连接池，不为空则使用指定的数据量连接池
        
        指定了类型之后，查看各自数据库连接池的配置，支持数据库的配置属性， 比如 druid, 查阅相关文档，配置即可
        比如要指定 druid 连接池的配置： com.alibaba.druid.pool.DruidDataSource
        maxActive=xxx

# 一个问题
    在测试过程中，开始为了图方便，用 create / drop 语句来测试事务情况，但是断点调试进去，发现事务有执行回滚，但是表还是创建了
    
    后来换 insert/update/delete 语句，发现事务正常，查看mysql文档，发现事务是作用于表的，create/drop 语句不在范围内
    
    https://dev.mysql.com/doc/refman/5.7/en/commit.html

# 启用 JDBC 的规则
    # 配置启用的 JDBC IDS， 中间用英文逗号分隔，如果为空或为配置都会启用所有的 JDBC, 允许使用通配符 *
    yyspring.jdbc.enabled-ids=xxx,ttt

# 禁用 JDBC 的规则
    # 配置禁用的 JDBC IDS， 中间用英文逗号分隔，允许使用通配符 *
    yyspring.jdbc.exclude-ids=xxx,ttt

# 指定 primary Jdbc 定义
    # 一个应用只能有一个 primary 的Jdbc 定义
    yyspring.jdbc.primary-id=xxx

# Jdbc 基础支持
    支持基本的数据库操作, 自动注册 Jdbc 实例，可直接注入相应的Bean，比如你的数据库标识是
    
    user, 那么框架自动注入名称为 userJdbc 的Bean， 可直接注入：
    
    @Resource(name = "userJdbc")
    private Jdbc jdbc;
    
    如果你只有一个数据源的话，可直接用以下方式注入：
    
    @Autowired
    private Jdbc jdbc;

# 声明式事务支持
    默认情况下，事务和声明式事务都是开启的，上前面的章节《JDBC 配置约定》 中定义了事务相关的两个配置，开发者可以根据需要进行开启或关闭
    
    声明式事务管理约定：
        以以下结尾的Bean会进行类的自动代理：
            Service, ServiceImpl
            
        以下开头的方法名，将会开启事务：
            add*
            save* 
            insert*
            update*
            delete*
            remove*
            drop* 
            audit*
            create*
            apply*
            
        以下方法是只读的：
            get*
            find*
            select*
            query*
            
        因此在实际开发的时候，需要谨慎命名方法名
        
    注意： 如果你想覆盖默认的配置，比如你的方法名称是 get*, 但是你又想支持事务，那么你可以使用 @Transactional 注解
          同样的，如果你的方法名是 add*, 但是又不想支持事务，也可以用 
                @Transactional(propagation = Propagation.NOT_SUPPORTED)
          即使用 @Transactional 可以覆盖默认的事务配置

# 定义升龙数据源别名
    前缀：
        yyspring.jdbc.rise-riseAlias.[instanceName]=aliasInstanceName
        
        实例：
        yyspring.jdbc.rise-riseAlias.cloudapp_cloudmysql1218=cloudapp_cloudmysql1219

# 定义升龙Mysql数据源
    基本前缀：
        yyspring.jdbc.rises.[DatasourceName].
        
        yyspring.jdbc.rises:       升龙 jdbc 固定前缀
        DatabaseType:    
        DatasourceName:  数据源标识，如 user, 这个很关键，后续自动注册的 Bean 的名称会根据这个来定义，比如 名称为 user，那么会注册Bean：
                         userDataSource, userJdbcTemplate, userTransactionManager, userTransactionTemplate
    数据库基本配置：
        前缀均为： yyspring.jdbc.rises.[DatasourceName].
        
        dbType:         数据库类型，目前仅支持 mysql， 后续可增加其他支持如sqlserver, oracle......
        name:           必填，对应升龙数据源实例名称，如 cloudapp_cloudmysql2915
        slave:          选填，是否启用从库，默认不启用，从库不支持事务
        schema:         选填，默认是读取 xxx_default_db 的值，默认是 rise_db
        jdbcUrlParams:  选填，jdbcUrl的参数，不带 ? 号， 默认是： useUnicode=true&characterEncoding=utf8&&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true
        
        supportTx:      选填，是否需要支持事务， 默认是 false， 如果支持则会创建相应的 TransactionManager, TransactionTemplate，否则不会创建
        proxyTx:        选填，是否支持声明式事务，默认是 false，支持声明式事务，创建，TransactionInterceptor
        initJdbc:       选填，默认是true，即会注册 Jdbc 接口的Bean

    连接池配置：
        连接池，默认是启用的，全部都可以使用默认配置，配置前缀均为：
            yyspring.jdbc.rises.[DatasourceName].poolConfig
        
        type:           选填，数据源提供者，默认是采用 druid，支持 dbcp, dbcp2, druid, tomcat, HikarCP
        dsclazz:        选填，数据源具体实现类，如果为空则根据provider来配置默认的数据库连接池，不为空则使用指定的数据量连接池
        
        指定了类型之后，查看各自数据库连接池的配置，支持数据库的配置属性， 比如 druid, 查阅相关文档，配置即可
        比如要指定 druid 连接池的配置： com.alibaba.druid.pool.DruidDataSource
        maxActive=xxx
    
    开发环境配置：
        定义以下变量即可：
            cloudapp_cloudmysql2915_default_db:rise_db
            cloudapp_cloudmysql2915_port:3543
            cloudapp_cloudmysql2915_host:d68193.mysql.yyclouds.com
            cloudapp_cloudmysql2915_slave_host:d68193.slave.mysql.yyclouds.com
            cloudapp_cloudmysql2915_user:************
            cloudapp_cloudmysql2915_password:****************
    
        







