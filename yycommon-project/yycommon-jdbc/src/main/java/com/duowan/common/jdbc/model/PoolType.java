package com.duowan.common.jdbc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库连接池类型
 *
 * @author Arvin
 */
public enum PoolType {

    /**
     * 阿里 Druid
     */
    DRUID,

    HIKARI,

    DBCP2,

    C3P0,

    TOMCAT
    ;

    private static final Map<String, PoolType> MAP = new HashMap<>();

    static {
        for (PoolType type : PoolType.values()) {
            MAP.put(type.name().toLowerCase(), type);
        }
    }

    /**
     * 解析数据库连接池类型，不区分大小写
     *
     * @param poolType 数据库类型
     * @return 如果有匹配的就返回，没有就返回null
     */
    public static PoolType parse(String poolType) {
        if (null == poolType) {
            return null;
        }
        return MAP.get(poolType.trim().toLowerCase());
    }
}
