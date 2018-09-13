package com.duowan.common.jdbc.util;

import com.duowan.common.utils.ConvertUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 升龙工具类
 *
 * @author Arvin
 */
public abstract class RiseUtil {

    /**
     * 解析升龙数据源别名， 升龙数据源别名格式：
     * rise.ds.[升龙数据源名称]=[升龙数据源别名]
     *
     * @param configMap 配置MAP
     * @return 返回别名MAP，非null
     */
    public static Map<String, String> parseRiseDataSourceAliasMap(Map<String, String> configMap) {
        Map<String, String> aliasMap = new HashMap<>();

        if (null == configMap || configMap.isEmpty()) {
            return aliasMap;
        }
        final String regex = "(?i)rise\\.ds\\.(.*)$";
        for (Map.Entry<String, String> entry : configMap.entrySet()) {

            String key = entry.getKey();
            String value = ConvertUtil.toString(entry.getValue(), "").trim();

            String dsName = key.replaceFirst(regex, "$1");
            if (StringUtils.isAnyBlank(value, dsName)) {
                continue;
            }

            aliasMap.put(dsName, value);
        }

        return aliasMap;
    }
}
