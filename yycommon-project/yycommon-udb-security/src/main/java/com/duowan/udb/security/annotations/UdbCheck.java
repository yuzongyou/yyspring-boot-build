package com.duowan.udb.security.annotations;

import com.duowan.udb.security.CheckMode;

import java.lang.annotation.*;

/**
 * UDB 检查
 *
 * @author dw_xiajiqiu1
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UdbCheck {

    /**
     * 验证模式
     *
     * @return 返回验证模式
     */
    CheckMode value() default CheckMode.DEFAULT;
}
