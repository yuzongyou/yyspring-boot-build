package com.duowan.common.admincenter.annotations;

import java.lang.annotation.*;

/**
 * 忽略 权限 检查
 *
 * @author dw_xiajiqiu1
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoPrivilege {

}
