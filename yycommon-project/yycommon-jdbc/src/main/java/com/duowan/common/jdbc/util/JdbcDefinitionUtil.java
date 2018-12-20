package com.duowan.common.jdbc.util;

import com.duowan.common.jdbc.model.DBType;
import com.duowan.common.jdbc.model.JdbcDefinition;
import com.duowan.common.jdbc.model.PoolType;
import com.duowan.common.jdbc.model.RiseJdbcDefinition;
import com.duowan.common.jdbc.provider.dbtype.DBProvider;
import com.duowan.common.jdbc.provider.pooltype.PoolProvider;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Jdbc 定义工具类
 *
 * @author Arvin
 */
public class JdbcDefinitionUtil {

    private JdbcDefinitionUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 提取启用的列表
     *
     * @param excludeIds         要禁用的ID列表
     * @param jdbcDefinitionList jdbcDef 定义列表
     * @return 返回所有启用的JdbcDef定义
     */
    public static List<JdbcDefinition> filterExcludeDefList(Set<String> excludeIds, List<JdbcDefinition> jdbcDefinitionList) {

        if (excludeIds == null || excludeIds.isEmpty() || null == jdbcDefinitionList || jdbcDefinitionList.isEmpty()) {
            return jdbcDefinitionList;
        }

        List<JdbcDefinition> resultList = new ArrayList<>();
        for (JdbcDefinition jdbcDefinition : jdbcDefinitionList) {
            if (!isJdbcDefInIdSet(excludeIds, jdbcDefinition)) {
                resultList.add(jdbcDefinition);
            }
        }
        return resultList;
    }

    /**
     * 提取启用的列表
     *
     * @param enabledIds         要启用的ID列表
     * @param jdbcDefinitionList jdbcDef 定义列表
     * @return 返回所有启用的JdbcDef定义
     */
    public static List<JdbcDefinition> extractEnabledJdbcDefList(Set<String> enabledIds, List<JdbcDefinition> jdbcDefinitionList) {

        if (enabledIds == null || enabledIds.isEmpty() || null == jdbcDefinitionList || jdbcDefinitionList.isEmpty()) {
            return jdbcDefinitionList;
        }

        List<JdbcDefinition> resultList = new ArrayList<>();
        for (JdbcDefinition jdbcDefinition : jdbcDefinitionList) {
            if (isJdbcDefInIdSet(enabledIds, jdbcDefinition)) {
                resultList.add(jdbcDefinition);
            }
        }
        return resultList;
    }

    /**
     * 判断jdbc定义是否启用
     *
     * @param jdbcIds        启用的JdbcDef id ，可以使用通配符 *
     * @param jdbcDefinition JdbcDefinition 定义
     * @return 返回是否启用
     */
    private static boolean isJdbcDefInIdSet(Set<String> jdbcIds, JdbcDefinition jdbcDefinition) {
        if (jdbcIds == null || jdbcIds.isEmpty()) {
            return true;
        }
        if (jdbcIds.contains(jdbcDefinition.getId())) {
            return true;
        }
        // 通配符检查
        for (String jdbcId : jdbcIds) {
            if (CommonUtil.isStartWildcardMatch(jdbcDefinition.getId(), jdbcId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 将 JdbcDefinition.id = primaryId 的定义对象设置为 primary
     *
     * @param primaryId          主ID
     * @param jdbcDefinitionList 定义列表
     * @return 返回原jdbc定义列表
     */
    public static List<JdbcDefinition> applyPrimaryJdbcDefList(String primaryId, List<JdbcDefinition> jdbcDefinitionList) {

        if (null == jdbcDefinitionList || jdbcDefinitionList.isEmpty()) {
            return jdbcDefinitionList;
        }
        for (JdbcDefinition jdbcDefinition : jdbcDefinitionList) {
            jdbcDefinition.setPrimary(jdbcDefinition.getId().equals(primaryId));
        }

        return jdbcDefinitionList;
    }

    /**
     * 获取支持 处理指定 JdbcDefinition 的DBProvider
     *
     * @param providerList   DB provider 列表
     * @param jdbcDefinition 定义
     * @return 如果有就返回
     */
    public static DBProvider lookupDBProvider(List<DBProvider> providerList, JdbcDefinition jdbcDefinition) {

        if (null == providerList || providerList.isEmpty()) {
            return null;
        }

        for (DBProvider provider : providerList) {
            if (provider.support(jdbcDefinition)) {
                return provider;
            }
        }

        return null;
    }


    /**
     * 获取支持 处理指定 JdbcDefinition 的 PoolProvider
     *
     * @param providerList   Pool provider 列表
     * @param jdbcDefinition 定义
     * @return 如果有就返回
     */
    public static PoolProvider lookupPoolProvider(List<PoolProvider> providerList, JdbcDefinition jdbcDefinition) {

        if (null == providerList || providerList.isEmpty()) {
            return null;
        }

        for (PoolProvider provider : providerList) {
            if (provider.support(jdbcDefinition)) {
                return provider;
            }
        }

        return null;
    }

    /**
     * 填充默认配置
     *
     * @param jdbcDefinitionList JdbcDefinition 列表
     * @return 返回填充默认配置后的列表
     */
    public static List<JdbcDefinition> fillDefaultConfig(List<JdbcDefinition> jdbcDefinitionList) {
        if (null == jdbcDefinitionList || jdbcDefinitionList.isEmpty()) {
            return jdbcDefinitionList;
        }
        for (JdbcDefinition jdbcDefinition : jdbcDefinitionList) {
            fillDefaultConfig(jdbcDefinition);
        }
        return jdbcDefinitionList;
    }

    /**
     * 填充默认配置
     *
     * @param jdbcDefinition jdbc 定义
     * @return Jdbc 定义
     */
    public static JdbcDefinition fillDefaultConfig(JdbcDefinition jdbcDefinition) {
        if (null == jdbcDefinition) {
            return null;
        }

        jdbcDefinition.setUsername(filterDefaultUsername(jdbcDefinition.getUsername(), "root"));
        jdbcDefinition.setPassword(filterDefaultPassword(jdbcDefinition.getPassword(), "admin"));
        jdbcDefinition.setDbType(filterDefaultDBType(jdbcDefinition.getDbType(), DBType.MYSQL));
        jdbcDefinition.setPoolType(filterDefaultPoolType(jdbcDefinition.getPoolType(), PoolType.DRUID));

        return jdbcDefinition;
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

    public static JdbcDefinition fillDefaultPoolAndDriverConfig(JdbcDefinition jdbcDefinition, DBProvider dbProvider, PoolProvider poolProvider) {

        jdbcDefinition.setDsclazz(filterDefaultDataSourceClass(poolProvider, jdbcDefinition.getDsclazz()));
        jdbcDefinition.setDriverClass(filterDefaultDriverClass(dbProvider, jdbcDefinition.getDriverClass()));

        return jdbcDefinition;
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
     * 自动填充环境变量属性，升龙会将数据库属性设置到系统环境变量中
     *
     * @param defList     定义列表
     * @param environment 系统环境
     * @return 返回填充好属性后的定义列表
     */
    public static List<JdbcDefinition> autoFillProperties(List<JdbcDefinition> defList, Environment environment) {
        List<JdbcDefinition> resultList = new ArrayList<>();

        for (JdbcDefinition jdbcDefinition : defList) {

            if (jdbcDefinition instanceof RiseJdbcDefinition) {

                List<RiseJdbcDefinition> subList = autoFillProperties((RiseJdbcDefinition) jdbcDefinition, environment);

                if (!subList.isEmpty()) {
                    resultList.addAll(subList);
                }
            } else {
                resultList.add(jdbcDefinition);
            }
        }

        return resultList;

    }

    /**
     * 自动填充环境变量属性，升龙会将数据库属性设置到系统环境变量中
     *
     * @param jdbcDef     升龙数据源定义
     * @param environment 系统环境
     * @return 返回填充好属性后的定义列表
     */
    public static List<RiseJdbcDefinition> autoFillProperties(RiseJdbcDefinition jdbcDef, Environment environment) {
        List<RiseJdbcDefinition> resultList = new ArrayList<>();
        resultList.add(jdbcDef);

        String dsName = jdbcDef.getName();
        String schema = jdbcDef.getSchema();
        if (StringUtils.isBlank(schema)) {
            jdbcDef.setSchema(lookupRiseDsEnvVar(environment, dsName, "default_db", "rise_db"));
        }

        jdbcDef.setUsername(lookupRiseDsEnvVar(environment, dsName, "user", ""));
        jdbcDef.setPassword(lookupRiseDsEnvVar(environment, dsName, "password", ""));

        String host = lookupRiseDsEnvVar(environment, dsName, "host", null);
        AssertUtil.assertNotBlank(host, "找不到环境变量: [" + dsName + "_host]对应的值");

        String port = lookupRiseDsEnvVar(environment, dsName, "port", null);
        AssertUtil.assertNotBlank(host, "找不到环境变量: [" + dsName + "_port]对应的值");

        String jdbcUrlParams = jdbcDef.getJdbcUrlParam();
        if (StringUtils.isBlank(jdbcUrlParams)) {
            jdbcDef.setJdbcUrlParam("useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&rewriteBatchedStatements=true");
        }

        // jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF8
        String jdbcUrlTpl = "jdbc:mysql://%s:%s/%s?%s";

        String jdbcUrl = String.format(jdbcUrlTpl, host, port, jdbcDef.getSchema(), jdbcDef.getJdbcUrlParam());

        jdbcDef.setJdbcUrl(jdbcUrl);

        // 判断是否包含从库配置
        if (jdbcDef.isSlaveEnabled()) {
            String slaveHost = lookupRiseDsEnvVar(environment, dsName, "slave_host", null);
            if (StringUtils.isNotBlank(slaveHost)) {
                resultList.add(createSlaveRiseJdbcDef(jdbcDef, port, jdbcUrlTpl, slaveHost));
            }
        }

        return resultList;
    }

    private static RiseJdbcDefinition createSlaveRiseJdbcDef(RiseJdbcDefinition sourceJdbcDef, String port, String jdbcUrlTpl, String slaveHost) {
        RiseJdbcDefinition slaveJdbcDef = sourceJdbcDef.cloneInstance();
        slaveJdbcDef.setPrimary(false);
        slaveJdbcDef.setSupportTx(false);
        slaveJdbcDef.setProxyTx(false);
        slaveJdbcDef.setId("slave" + CommonUtil.firstLetterToUpperCase(sourceJdbcDef.getId()));
        slaveJdbcDef.setJdbcUrl(String.format(jdbcUrlTpl, slaveHost, port, sourceJdbcDef.getSchema(), sourceJdbcDef.getJdbcUrlParam()));
        return slaveJdbcDef;
    }

    private static String lookupRiseDsEnvVar(Environment environment, String dsName, String field, String defVal) {

        String key = dsName + "_" + field;

        String value = environment.getProperty(key);
        if (StringUtils.isBlank(value)) {
            value = System.getProperty(key);
        }

        if (StringUtils.isBlank(value)) {
            value = System.getenv(key);
        }

        value = StringUtils.isBlank(value) ? defVal : value;
        if (StringUtils.isBlank(value)) {
            return value;
        }
        value = environment.resolvePlaceholders(value);
        return value;
    }
}
