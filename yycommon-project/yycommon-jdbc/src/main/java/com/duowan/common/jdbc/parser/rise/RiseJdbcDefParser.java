package com.duowan.common.jdbc.parser.rise;

import com.duowan.common.jdbc.model.DBType;
import com.duowan.common.jdbc.model.RiseJdbcDef;
import com.duowan.common.jdbc.parser.JdbcDefParser;
import com.duowan.common.jdbc.util.RiseUtil;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
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
 * 解析升龙的Mysql数据源， 升龙的Mysql数据源在部署的时候会把数据源的参数写入到环境变量中：
 * <pre>
 *     cloudapp_cloudmysql2915_default_db:rise_db
 *     cloudapp_cloudmysql2915_port:3543
 *     cloudapp_cloudmysql2915_host:d68193.mysql.yyclouds.com
 *     cloudapp_cloudmysql2915_slave_host:d68193.slave.mysql.yyclouds.com
 *     cloudapp_cloudmysql2915_user:************
 *     cloudapp_cloudmysql2915_password:****************
 * </pre>
 *
 * @author Arvin
 */
public class RiseJdbcDefParser implements JdbcDefParser {

    private static final Logger logger = LoggerFactory.getLogger(RiseJdbcDefParser.class);

    /**
     * 属性解析器
     */
    private static final List<RiseJdbcDefFieldResolver> RESOLVER_LIST = lookupJdbcDefFieldResolvers();

    /**
     * 搜索解析器
     *
     * @return 返回解析器列表
     */
    private static List<RiseJdbcDefFieldResolver> lookupJdbcDefFieldResolvers() {

        return ReflectUtil.scanAndInstanceByDefaultConstructor(
                RiseJdbcDefFieldResolver.class, RiseJdbcDefFieldResolver.class.getPackage().getName());

    }

    @Override
    public List<RiseJdbcDef> parser(Map<String, String> configMap, Environment environment) {

        // 解析升龙数据源别名
        Map<String, String> aliasMap = RiseUtil.parseRiseDataSourceAliasMap(configMap);

        List<RiseJdbcDef> resultList = parseJdbcDefList(configMap, aliasMap);

        // 处理升龙的环境变量
        resultList = resolveRiseEnvVar(configMap, environment, resultList);

        return resultList;
    }

    /**
     * <pre>
     * 处理环境变量的值
     * 1. schema -- default_db
     * 2. username -- user
     * 3. password -- password
     * 4. host -- host
     * 5. port -- port
     * 6. jdbcUrl
     * </pre>
     *
     * @param defList 定义列表
     * @return 返回定义列表
     */
    private List<RiseJdbcDef> resolveRiseEnvVar(Map<String, String> configMap, Environment environment, List<RiseJdbcDef> defList) {

        List<RiseJdbcDef> resultList = new ArrayList<>();

        for (RiseJdbcDef jdbcDef : defList) {

            String dsName = jdbcDef.getName();

            String schema = jdbcDef.getSchema();
            if (StringUtils.isBlank(schema)) {
                jdbcDef.setSchema(lookupRiseDsEnvVar(configMap, environment, dsName, "default_db", "rise_db"));
            }

            jdbcDef.setUsername(lookupRiseDsEnvVar(configMap, environment, dsName, "user", ""));
            jdbcDef.setPassword(lookupRiseDsEnvVar(configMap, environment, dsName, "password", ""));

            String host = lookupRiseDsEnvVar(configMap, environment, dsName, "host", null);
            AssertUtil.assertNotBlank(host, "找不到环境变量: [" + dsName + "_host]对应的值");

            String port = lookupRiseDsEnvVar(configMap, environment, dsName, "port", null);
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
                String slaveHost = lookupRiseDsEnvVar(configMap, environment, dsName, "slave_host", null);
                if (StringUtils.isNotBlank(slaveHost)) {
                    RiseJdbcDef slaveJdbcDef = jdbcDef.cloneInstance();
                    slaveJdbcDef.setPrimary(false);
                    slaveJdbcDef.setSupportTx(false);
                    slaveJdbcDef.setProxyTx(false);
                    slaveJdbcDef.setId("slave" + CommonUtil.firstLetterToUpperCase(jdbcDef.getId()));
                    slaveJdbcDef.setJdbcUrl(String.format(jdbcUrlTpl, slaveHost, port, jdbcDef.getSchema(), jdbcDef.getJdbcUrlParam()));

                    resultList.add(slaveJdbcDef);
                }
            }

            resultList.add(jdbcDef);

        }

        return resultList;
    }

    private String lookupRiseDsEnvVar(Map<String, String> configMap, Environment environment, String dsName, String field, String defVal) {

        String key = dsName + "_" + field;

        String value = System.getenv(key);

        if (StringUtils.isBlank(value)) {
            value = System.getProperty(key);
        }

        if (StringUtils.isBlank(value)) {
            value = environment.getProperty(key);
        }

        if (StringUtils.isBlank(value)) {
            value = configMap.get(key);
        }

        return StringUtils.isBlank(value) ? defVal : value;
    }

    /**
     * 解析JdbcDef定义
     *
     * @param jdbcConfigMap 升龙Jdbc配置定义
     * @param aliasMap      数据源实例名称别名
     * @return 返回 JdbcDef 定义列表
     */
    private static List<RiseJdbcDef> parseJdbcDefList(Map<String, String> jdbcConfigMap, final Map<String, String> aliasMap) {

        // 查找所有已 jdbc.rise 开头的配置
        Map<String, RiseJdbcDef> jdbcDefMap = new HashMap<>();

        if (null == jdbcConfigMap || jdbcConfigMap.isEmpty()) {
            return new ArrayList<>();
        }

        final String regex = "(?i)jdbc\\.rise\\.([^\\.]+)\\.([^\\.]+)\\.(.*)$";

        for (Map.Entry<String, String> entry : jdbcConfigMap.entrySet()) {

            String key = entry.getKey();

            // 不是可识别的参数
            if (StringUtils.isBlank(key) || !key.startsWith("jdbc.rise.")) {
                continue;
            }

            String value = entry.getValue();
            if (StringUtils.isBlank(value)) {
                value = "";
            }

            String dbType = key.replaceFirst(regex, "$1");
            String id = key.replaceFirst(regex, "$2");
            String config = key.replaceFirst(regex, "$3");

            RiseJdbcDef jdbcDef = jdbcDefMap.get(id);
            if (null == jdbcDef) {
                jdbcDef = new RiseJdbcDef();
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
            resolveJdbcDefProperty(aliasMap, jdbcDef, key, config, value);
        }

        return new ArrayList<>(jdbcDefMap.values());
    }

    private static RiseJdbcDef resolveJdbcDefProperty(Map<String, String> aliasMap, RiseJdbcDef jdbcDef, String key, String subKey, String value) {
        boolean hadResolved = false;
        if (RESOLVER_LIST != null) {
            for (RiseJdbcDefFieldResolver resolver : RESOLVER_LIST) {
                if (resolver.resolve(aliasMap, jdbcDef, key, subKey, value)) {
                    hadResolved = true;
                    break;
                }
            }
        }

        if (!hadResolved) {
            logger.warn("无效的 Rise JDBC属性[" + key + "]=[" + value + "]");
        }
        return jdbcDef;
    }
}
