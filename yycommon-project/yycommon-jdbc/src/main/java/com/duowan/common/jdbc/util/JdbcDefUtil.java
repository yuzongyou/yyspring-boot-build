package com.duowan.common.jdbc.util;

import com.duowan.common.jdbc.model.DBType;
import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.jdbc.model.PoolType;
import com.duowan.common.jdbc.parser.JdbcDefParser;
import com.duowan.common.jdbc.provider.dbtype.DBProvider;
import com.duowan.common.jdbc.provider.pooltype.PoolProvider;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.*;

/**
 * Jdbc 定义工具类
 *
 * @author Arvin
 */
public abstract class JdbcDefUtil {

    /**
     * JDBC 扩展配置前缀
     */
    public static final String JDBC_EXTEND_PREFIX = "extend.jdbc.";

    /**
     * 返回 JDBC 自定义组件实例列表,使用：
     * extend.jdbc.[appId].[extendType].[extendKey]=[extendValue]
     * 如，扩展自定义的 JdbcDefParser
     * # 下面的配置将实例化指定包下面实现了 JdbcDefParser 接口且包含默认构造函数的类
     * extend.jdbc.test.parser.packages=com.duowan.test.parser,net.oschina.test.parser
     * # 下面的配置直接指定某个parser
     * extend.jdbc.test.parser.classes=com.duowan.test.parser.CustomJdbcDefParser,net.oschina.test.parser.CustomJdbcDefParser
     *
     * @param configMap     配置MAP
     * @param environment   环境
     * @param componentType 组件类型
     * @param packageSuffix 包后缀
     * @param classesSuffix 类后缀
     * @param <T>           结果类型
     * @return 始终返回非 null
     */
    public static <T> List<T> lookupJdbcDefExtendComponents(Map<String, String> configMap, Environment environment, Class<T> componentType, String packageSuffix, String classesSuffix) {
        List<T> componentList = new ArrayList<>();

        // 获取自定义的 JdbcDefParser
        if (null == configMap || configMap.isEmpty()) {
            return componentList;
        }

        for (Map.Entry<String, String> entry : configMap.entrySet()) {

            String key = entry.getKey();

            String value = entry.getValue();
            if (StringUtils.isBlank(value)) {
                value = "";
            }

            // 不是可识别的参数
            if (StringUtils.isAnyBlank(key, value) || !key.startsWith(JDBC_EXTEND_PREFIX)) {
                continue;
            }

            // 指定包下面的所有实现了 JdbcDefParser 且包含默认构造函数的都创建实例
            if (StringUtils.isNotBlank(packageSuffix) && key.endsWith(packageSuffix)) {
                List<T> subComponentList = ReflectUtil.scanAndInstanceByDefaultConstructor(componentType, value);
                if (null != subComponentList && !subComponentList.isEmpty()) {
                    componentList.addAll(subComponentList);
                }
                continue;
            }

            // 指定实现了 JdbcDefParser 且含默认构造函数的都创建实例
            if (StringUtils.isNotBlank(classesSuffix) && key.endsWith(classesSuffix)) {
                List<T> subComponentList = ReflectUtil.newInstancesByDefaultConstructor(componentType, value);
                if (null != subComponentList && !subComponentList.isEmpty()) {
                    componentList.addAll(subComponentList);
                }
            }
        }

        // 过滤重复的
        return CommonUtil.filterDuplicateInstance(componentList);
    }

    /**
     * 返回 JDBC 定义解析器列表, 可以自定义解析器，使用：
     * extend.jdbc.[appId].[extendType].[extendKey]=[extendValue]
     * 如，扩展自定义的 JdbcDefParser
     * # 下面的配置将实例化指定包下面实现了 JdbcDefParser 接口且包含默认构造函数的类
     * extend.jdbc.test.parser.packages=com.duowan.test.parser,net.oschina.test.parser
     * # 下面的配置直接指定某个parser
     * extend.jdbc.test.parser.classes=com.duowan.test.parser.CustomJdbcDefParser,net.oschina.test.parser.CustomJdbcDefParser
     *
     * @param configMap   配置MAP
     * @param environment 环境
     * @return 始终返回非 null
     */
    public static List<JdbcDefParser> lookupJdbcDefParsers(Map<String, String> configMap, Environment environment) {

        List<JdbcDefParser> parserList = new ArrayList<>();

        CommonUtil.appendList(parserList, ReflectUtil.scanAndInstanceByDefaultConstructor(JdbcDefParser.class, JdbcDefParser.class.getPackage().getName()));

        CommonUtil.appendList(parserList, lookupJdbcDefExtendComponents(configMap, environment,
                JdbcDefParser.class, "parser.packages", "parser.classes"));

        // 过滤重复的
        return CommonUtil.filterDuplicateInstance(parserList);
    }

    /**
     * 获取默认的 JdbcDef DB provider 实例对象
     *
     * @param configMap   配置map
     * @param environment 环境
     * @return 返回非 null list
     */
    public static List<DBProvider> lookupJdbcDefDbProviders(Map<String, String> configMap, Environment environment) {

        List<DBProvider> providerList = new ArrayList<>();

        CommonUtil.appendList(providerList, ReflectUtil.scanAndInstanceByDefaultConstructor(DBProvider.class, DBProvider.class.getPackage().getName()));

        CommonUtil.appendList(providerList, lookupJdbcDefExtendComponents(configMap, environment,
                DBProvider.class, "db.provider.packages", "db.provider.classes"));

        return CommonUtil.filterDuplicateInstance(providerList);
    }

    /**
     * 获取默认的 JdbcDef Pool provider 实例对象
     *
     * @param configMap   配置map
     * @param environment 环境
     * @return 返回非 null list
     */
    public static List<PoolProvider> lookupJdbcDefPoolProviders(Map<String, String> configMap, Environment environment) {

        List<PoolProvider> providerList = new ArrayList<>();

        CommonUtil.appendList(providerList, ReflectUtil.scanAndInstanceByDefaultConstructor(PoolProvider.class, PoolProvider.class.getPackage().getName()));

        CommonUtil.appendList(providerList, lookupJdbcDefExtendComponents(configMap, environment,
                PoolProvider.class, "pool.provider.packages", "pool.provider.classes"));

        return CommonUtil.filterDuplicateInstance(providerList);
    }

    /**
     * 解析 JDBC 定义列表
     *
     * @param configMap   配置MAP
     * @param parserList  解析器列表
     * @param environment 环境
     * @return 返回非 null 列表
     */
    public static List<JdbcDef> parseJdbcDefList(Map<String, String> configMap, List<JdbcDefParser> parserList, Environment environment) {

        List<JdbcDef> resultList = new ArrayList<>();

        for (JdbcDefParser parser : parserList) {
            List<? extends JdbcDef> subList = parser.parser(configMap, environment);
            if (null != subList && !subList.isEmpty()) {
                resultList.addAll(subList);
            }
        }

        return resultList;
    }

    /**
     * 提取启用的列表：
     * extend.jdbc.[appId].[extendType].[extendKey]=[extendValue]
     * # 下面的配置直接指定哪些 jdbc 定义的 ID 是启用的
     * extend.jdbc.test.enabled.ids=user,user1
     *
     * @param enabledIds  要启用的ID列表
     * @param jdbcDefList jdbcDef 定义列表
     * @return 返回所有启用的JdbcDef定义
     */
    public static List<JdbcDef> extractEnabledJdbcDefList(Set<String> enabledIds, List<JdbcDef> jdbcDefList) {

        if (enabledIds == null || enabledIds.isEmpty() || null == jdbcDefList || jdbcDefList.isEmpty()) {
            return jdbcDefList;
        }

        List<JdbcDef> resultList = new ArrayList<>();
        for (JdbcDef jdbcDef : jdbcDefList) {
            if (isJdbcDefEnabled(enabledIds, jdbcDef)) {
                resultList.add(jdbcDef);
            }
        }
        return resultList;
    }

    /**
     * 判断jdbc定义是否启用
     *
     * @param enabledIds 启用的JdbcDef id ，可以使用通配符 *
     * @param jdbcDef    JdbcDef 定义
     * @return 返回是否启用
     */
    private static boolean isJdbcDefEnabled(Set<String> enabledIds, JdbcDef jdbcDef) {
        if (enabledIds == null || enabledIds.isEmpty()) {
            return true;
        }
        if (enabledIds.contains(jdbcDef.getId())) {
            return true;
        }
        // 通配符检查
        for (String enabledId : enabledIds) {
            if (CommonUtil.isStartWildcardMatch(jdbcDef.getId(), enabledId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 将 JdbcDef.id = primaryId 的定义对象设置为 primary
     *
     * @param primaryId   主ID
     * @param jdbcDefList 定义列表
     * @return 返回原jdbc定义列表
     */
    public static List<JdbcDef> applyPrimaryJdbcDefList(String primaryId, List<JdbcDef> jdbcDefList) {

        if (null == jdbcDefList || jdbcDefList.isEmpty()) {
            return jdbcDefList;
        }
        for (JdbcDef jdbcDef : jdbcDefList) {
            jdbcDef.setPrimary(jdbcDef.getId().equals(primaryId));
        }

        return jdbcDefList;
    }

    /**
     * 获取支持 处理指定 JdbcDef 的DBProvider
     *
     * @param providerList DB provider 列表
     * @param jdbcDef      定义
     * @return 如果有就返回
     */
    public static DBProvider lookupDBProvider(List<DBProvider> providerList, JdbcDef jdbcDef) {

        if (null == providerList || providerList.isEmpty()) {
            return null;
        }

        for (DBProvider provider : providerList) {
            if (provider.support(jdbcDef)) {
                return provider;
            }
        }

        return null;
    }


    /**
     * 获取支持 处理指定 JdbcDef 的 PoolProvider
     *
     * @param providerList Pool provider 列表
     * @param jdbcDef      定义
     * @return 如果有就返回
     */
    public static PoolProvider lookupPoolProvider(List<PoolProvider> providerList, JdbcDef jdbcDef) {

        if (null == providerList || providerList.isEmpty()) {
            return null;
        }

        for (PoolProvider provider : providerList) {
            if (provider.support(jdbcDef)) {
                return provider;
            }
        }

        return null;
    }

    /**
     * 填充默认配置
     *
     * @param jdbcDefList JdbcDef 列表
     * @return 返回填充默认配置后的列表
     */
    public static List<JdbcDef> fillDefaultConfig(List<JdbcDef> jdbcDefList) {
        if (null == jdbcDefList || jdbcDefList.isEmpty()) {
            return jdbcDefList;
        }
        for (JdbcDef jdbcDef : jdbcDefList) {
            fillDefaultConfig(jdbcDef);
        }
        return jdbcDefList;
    }

    /**
     * 填充默认配置
     *
     * @param jdbcDef jdbc 定义
     * @return Jdbc 定义
     */
    public static JdbcDef fillDefaultConfig(JdbcDef jdbcDef) {
        if (null == jdbcDef) {
            return null;
        }

        jdbcDef.setUsername(filterDefaultUsername(jdbcDef.getUsername(), "root"));
        jdbcDef.setPassword(filterDefaultPassword(jdbcDef.getPassword(), "admin"));
        jdbcDef.setDbType(filterDefaultDBType(jdbcDef.getDbType(), DBType.MYSQL));
        jdbcDef.setPoolType(filterDefaultPoolType(jdbcDef.getPoolType(), PoolType.DRUID));

        return jdbcDef;
    }

    private static PoolType filterDefaultPoolType(PoolType poolType, PoolType def) {
        return null == poolType ? def : poolType;
    }

    private static DBType filterDefaultDBType(DBType dbType, DBType def) {
        return null == dbType ? def : dbType;
    }

    private static String filterDefaultPassword(String password, String def) {
        return StringUtils.isBlank(password) ? def : password;
    }

    private static String filterDefaultUsername(String username, String def) {
        return StringUtils.isBlank(username) ? def : username;
    }

    public static JdbcDef fillDefaultPoolAndDriverConfig(JdbcDef jdbcDef, DBProvider dbProvider, PoolProvider poolProvider) {

        jdbcDef.setDsclazz(filterDefaultDataSourceClass(poolProvider, jdbcDef.getDsclazz()));
        jdbcDef.setDriverClass(filterDefaultDriverClass(dbProvider, jdbcDef.getDriverClass()));

        return jdbcDef;
    }

    private static String filterDefaultDriverClass(DBProvider provider, String driverClass) {

        if (StringUtils.isNotBlank(driverClass)) {
            return driverClass;
        }

        AssertUtil.assertNotNull(provider, "DBProvider 不能为空，无法识别[driverClass]");

        return provider.provideDriverClass();
    }

    private static String filterDefaultDataSourceClass(PoolProvider provider, String dsclazz) {

        if (StringUtils.isNotBlank(dsclazz)) {
            return dsclazz;
        }
        AssertUtil.assertNotNull(provider, "PoolProvider 不能为空，无法识别[DataSourceClass]");

        return provider.provideDsClass();
    }

    /**
     * 删除Jdbc 以 给定后缀结尾的所有 扩展项目
     *
     * @param configMap 配置map
     * @param suffixes  配置后缀名称
     */
    public static void removeJdbcExtendConfigs(Map<String, String> configMap, String... suffixes) {

        if (configMap == null || configMap.isEmpty() || StringUtils.isAllBlank(suffixes)) {
            return;
        }

        final String prefix = JDBC_EXTEND_PREFIX;

        Set<String> keys = new HashSet<>(configMap.keySet());

        for (String key : keys) {
            if (StringUtils.isNotBlank(key) && key.startsWith(prefix) && CommonUtil.isAnySuffixMatch(key, suffixes)) {
                configMap.remove(key);
            }
        }

    }
}
