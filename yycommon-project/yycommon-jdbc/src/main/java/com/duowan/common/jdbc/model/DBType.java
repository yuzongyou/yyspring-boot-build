package com.duowan.common.jdbc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库类型
 *
 * @author Arvin
 */
public enum DBType {

    /**
     * Mysql
     */
    MYSQL;

    private static final Map<String, DBType> MAP = new HashMap<>();

    static {
        for (DBType dbType : DBType.values()) {
            MAP.put(dbType.name().toLowerCase(), dbType);
        }
    }

    /**
     * 解析数据库类型，不区分大小写
     *
     * @param dbType 数据库类型
     * @return 如果有匹配的就返回，没有就返回null
     */
    public static DBType parse(String dbType) {
        if (null == dbType) {
            return null;
        }
        return MAP.get(dbType.trim().toLowerCase());
    }

}
