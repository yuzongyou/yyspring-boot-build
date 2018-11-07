package com.duowan.common.jdbc.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 数据库列
 *
 * @author Arvin
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Column {

    /**
     * (Optional) 对应数据库表的名称，默认是 Java类模型的属性下划线方式
     * @return 列名称
     */
    String columnName() default "";

    /**
     * 是否是主键|ID
     * @return true or false
     */
    boolean primaryKey() default false;

    /**
     * 是否自增
     * @return true or false
     */
    boolean autoIncrement() default false;

    /**
     * 是否使用 uuid
     * @return true or false
     */
    boolean useUuid() default false;

    /**
     * 是否是唯一能表示数据的属性
     * @return true or false
     */
    boolean uniqueKey() default false;

    /**
     * 插入的时候是否忽略
     * @return true or false
     */
    boolean insertIgnore() default false;

    /**
     * 插入的时候是否忽略 null 值
     * @return true or false
     */
    boolean insertIgnoreNull() default true;

    /**
     * 更新的时候是否忽略，默认不忽略
     * @return true or false
     */
    boolean updateIgnore() default false;

    /**
     * 更新的时候是否忽略 null 值
     * @return true or false
     */
    boolean updateIgnoreNull() default true;

}
