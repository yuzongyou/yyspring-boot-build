package com.duowan.yyspringcloud.msauth.web.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识这个类或方法是需要进行安全拦截的
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/17 19:44
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.METHOD, ElementType.TYPE})
public @interface SecurityApi {

    /**
     * 是否进行安全认证
     *
     * @return 是 or 否
     */
    boolean value() default true;

}
