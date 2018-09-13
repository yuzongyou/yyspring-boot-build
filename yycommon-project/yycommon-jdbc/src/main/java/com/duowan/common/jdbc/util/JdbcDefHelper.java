package com.duowan.common.jdbc.util;

import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.jdbc.model.RiseJdbcDef;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/13 14:09
 */
public class JdbcDefHelper {

    /**
     * 自动填充环境变量属性，升龙会将数据库属性设置到系统环境变量中
     *
     * @param defList     定义列表
     * @param environment 系统环境
     * @return 返回填充好属性后的定义列表
     */
    public static List<JdbcDef> autoFillProperties(List<JdbcDef> defList, Environment environment) {
        List<JdbcDef> resultList = new ArrayList<>();

        for (JdbcDef jdbcDef : defList) {

            if (jdbcDef instanceof RiseJdbcDef) {

                List<RiseJdbcDef> subList = autoFillProperties((RiseJdbcDef) jdbcDef, environment);

                if (null != subList && !subList.isEmpty()) {
                    resultList.addAll(subList);
                }
            } else {
                resultList.add(jdbcDef);
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
    public static List<RiseJdbcDef> autoFillProperties(RiseJdbcDef jdbcDef, Environment environment) {
        List<RiseJdbcDef> resultList = new ArrayList<>();
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

    private static RiseJdbcDef createSlaveRiseJdbcDef(RiseJdbcDef sourceJdbcDef, String port, String jdbcUrlTpl, String slaveHost) {
        RiseJdbcDef slaveJdbcDef = sourceJdbcDef.cloneInstance();
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
        value = environment.resolvePlaceholders(value);
        return value;
    }
}
