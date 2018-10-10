package com.duowan.common.web.annotations;

import com.duowan.common.web.ParamLookupScope;

import java.lang.annotation.*;

/**
 * 自动注入参数作用域获取注解
 *
 * @author Arvin
 * @since 2018/10/9 18:46
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Param {

    /**
     * 读取的作用域，会按照指定的作用域进行参数读取
     *
     * @return 返回作用域列表
     */
    ParamLookupScope[] value() default {ParamLookupScope.AUTO};
}
