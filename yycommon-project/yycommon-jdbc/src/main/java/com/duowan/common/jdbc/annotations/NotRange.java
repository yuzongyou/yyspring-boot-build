package com.duowan.common.jdbc.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标识某个字段不使用范围查询
 *
 * @author Arvin
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface NotRange {

}
