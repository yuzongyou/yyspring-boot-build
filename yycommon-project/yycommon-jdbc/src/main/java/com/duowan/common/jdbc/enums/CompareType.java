package com.duowan.common.jdbc.enums;

/**
 * 查询语句查询类型定义
 *
 * @author Arvin
 */
public enum CompareType {

    /** 等值比较 */
    EQUAL,

    /** 非等值比较 */
    NOT_EQUAL,

    /** like比较 */
    LIKE,

    /** IS NULL 比较 */
    IS_NULL,

    /** IS NOT NULL 比较 */
    IS_NOT_NULL,

    /** Between 比较 */
    BETWEEN,

    /**less than(小于)*/
    LT,

    /**greater than(大于)*/
    GT,

    /**less than and equal(小于等于)*/
    LTANDEQ,

    /**greater than and equal(大于等于)*/
    GTANDEQ

}
