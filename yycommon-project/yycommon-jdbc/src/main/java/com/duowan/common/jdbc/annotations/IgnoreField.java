package com.duowan.common.jdbc.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标识一个用于忽略的属性，即不会出现在任何 SQL 语句中
 *
 * @author Arvin
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface IgnoreField {

}
