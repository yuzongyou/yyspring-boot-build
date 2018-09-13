package com.duowan.common.jdbc.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 升序排序
 *
 * @author Arvin
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface OrderAsc {

    /**
     * 排序顺序， 越小的将排在越前面
     *
     * @return 返回顺序
     */
    int order() default 0;
}
