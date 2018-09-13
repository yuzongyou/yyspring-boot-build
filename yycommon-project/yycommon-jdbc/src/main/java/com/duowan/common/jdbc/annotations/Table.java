package com.duowan.common.jdbc.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 数据库表
 *
 * @author Arvin
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {

    /**
     * 数据库表名称， 如果没有写的话就按照下划线来处理
     *
     * @return 返回表名称
     */
    String tablename() default "";

    /**
     * 是否以下划线方式构造数据库表名称
     *
     * @return true or false
     */
    boolean underline() default true;

}
