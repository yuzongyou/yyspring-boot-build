package com.duowan.udb.security.annotations;

import java.lang.annotation.*;

/**
 * 忽略UDB 检查
 *
 * @author dw_xiajiqiu1
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoredUdbCheck {

}
