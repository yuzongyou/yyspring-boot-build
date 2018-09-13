package com.duowan.common.jdbc.annotations;

import com.duowan.common.jdbc.enums.CompareType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 指定查询比较类型
 *
 * @author Arvin
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface QueryCompare {

    /**
     * 设置查询比较类型
     *
     * @return 返回比较类型
     */
    CompareType value();

}
