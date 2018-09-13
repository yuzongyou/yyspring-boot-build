package com.duowan.common.jdbc.parser.standard;

import com.duowan.common.jdbc.model.DBType;
import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.jdbc.parser.JdbcDefParser;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 标准的 JdbcDef 解析器
 * <pre>
 * 定义一个 jdbc 数据源：
 * 基本前缀：
 * jdbc.std.[DatabaseType].[DatasourceName].
 *
 * jdbc.std:        jdbc 固定前缀
 * DatabaseType:    数据库类型，目前仅支持 mysql， 后续可增加其他支持如sqlserver, oracle......
 * DatasourceName:  数据源标识，如 user, 这个很关键，后续自动注册的 Bean 的名称会根据这个来定义，比如 名称为 user，那么会注册Bean：
 * userDataSource, userJdbcTemplate, userTransactionManager, userTransactionTemplate
 * 数据库基本配置：
 * 前缀均为： jdbc.std.[DatabaseType].[DatasourceName].
 *
 * jdbcUrl:        必填，数据库连接，如：jdbc:mysql://localhost:3306/test
 *
 * username:       选填，用户名，默认是root
 * password:       选填，密码，默认是空字符串
 * supportTx:      选填，是否需要支持事务， 默认是 false， 如果支持则会创建相应的 TransactionManager, TransactionTemplate，否则不会创建
 * proxyTx:        选填，是否支持声明式事务，默认是 false，支持声明式事务，创建，TransactionInterceptor
 * initJdbc:       选填，默认是true，即会注册 Jdbc 接口的Bean
 * driverClass:    选填，JDBC驱动包，默认是根据数据库类型自动导入，如mysql=com.mysql.jdbc.Driver
 *
 * 连接池配置：
 * 连接池，默认是启用的，全部都可以使用默认配置，配置前缀均为：
 * jdbc.std.[DatabaseType].[DatasourceName].pool
 *
 * type:           选填，数据源提供者，默认是采用 druid，支持 dbcp, dbcp2, druid, tomcat, HikarCP
 * dsclazz:        选填，数据源具体实现类，如果为空则根据provider来配置默认的数据库连接池，不为空则使用指定的数据量连接池
 *
 * 指定了类型之后，查看各自数据库连接池的配置，支持数据库的配置属性， 比如 druid, 查阅相关文档，配置即可
 * 比如要指定 druid 连接池的配置： com.alibaba.druid.pool.DruidDataSource
 * maxActive=xxx
 * </pre>
 *
 * @author Arvin
 */
public class StandardJdbcDefParser implements JdbcDefParser {

    private static final Logger logger = LoggerFactory.getLogger(StandardJdbcDefParser.class);

    /**
     * 属性解析器
     */
    private static final List<StandardJdbcDefFieldResolver> RESOLVER_LIST = lookupJdbcDefFieldResolvers();

    /**
     * 搜索解析器
     *
     * @return 返回解析器列表
     */
    private static List<StandardJdbcDefFieldResolver> lookupJdbcDefFieldResolvers() {

        return ReflectUtil.scanAndInstanceByDefaultConstructor(
                StandardJdbcDefFieldResolver.class, StandardJdbcDefFieldResolver.class.getPackage().getName());

    }

    @Override
    public List<? extends JdbcDef> parser(Map<String, String> configMap, Environment environment) {

        // 查找所有已 jdbc.std 开头的配置
        Map<String, JdbcDef> jdbcDefMap = new HashMap<>();

        if (configMap == null || configMap.isEmpty()) {
            return new ArrayList<>();
        }

        final String regex = "(?i)jdbc\\.std\\.([^\\.]+)\\.([^\\.]+)\\.(.*)$";

        for (Map.Entry<String, String> entry : configMap.entrySet()) {

            String key = entry.getKey();

            // 不是可识别的参数
            if (StringUtils.isBlank(key) || !key.startsWith("jdbc.std.")) {
                continue;
            }

            String value = entry.getValue();
            if (StringUtils.isBlank(value)) {
                value = "";
            }

            String dbType = key.replaceFirst(regex, "$1");
            String id = key.replaceFirst(regex, "$2");
            String config = key.replaceFirst(regex, "$3");

            JdbcDef jdbcDef = jdbcDefMap.get(id);
            if (null == jdbcDef) {
                jdbcDef = new JdbcDef();
                jdbcDefMap.put(id, jdbcDef);
                DBType type = DBType.parse(dbType);
                AssertUtil.assertNotNull(type, "[" + key + "] 未指定数据库类型！");
                jdbcDef.setDbType(type);
                jdbcDef.setId(id);
            }

            DBType type = DBType.parse(dbType);
            AssertUtil.assertNotNull(type, "[" + key + "] 未指定数据库类型！");
            AssertUtil.assertTrue(jdbcDef.getDbType().equals(type), "同一个数据源ID[" + id + "]中数据库类型不一致！");

            // 处理jdbcDef的其他属性
            resolveJdbcDefProperty(jdbcDef, key, config, value);
        }

        return new ArrayList<>(jdbcDefMap.values());
    }

    /**
     * 解析配置属性
     *
     * @param jdbcDef Jdbc定义
     * @param key     完整的 key
     * @param subKey  配置项KEY
     * @param value   值
     * @return 链式调用
     */
    private static JdbcDef resolveJdbcDefProperty(JdbcDef jdbcDef, String key, String subKey, String value) {

        boolean hadResolved = false;
        if (RESOLVER_LIST != null) {
            for (StandardJdbcDefFieldResolver resolver : RESOLVER_LIST) {
                if (resolver.resolve(jdbcDef, key, subKey, value)) {
                    hadResolved = true;
                    break;
                }
            }
        }

        if (!hadResolved) {
            logger.warn("无效的STD JDBC属性[" + key + "]=[" + value + "]");
        }
        return jdbcDef;
    }

}
