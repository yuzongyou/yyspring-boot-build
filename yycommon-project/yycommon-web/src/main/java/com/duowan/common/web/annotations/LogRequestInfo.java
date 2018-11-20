package com.duowan.common.web.annotations;

import java.lang.annotation.*;

/**
 * 是否打印请求日志
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/11/6 20:49
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogRequestInfo {

    /**
     * 要输出的参数列表，使用英文逗号分割，默认是所有
     * @return 返回记录日志的参数
     */
    String includeParams() default "*";

    /**
     * 要忽略输出的参数列表，使用英文逗号分割，默认没有要忽略
     * @return 要忽略记录日志的参数
     */
    String excludeParams() default "";

    /**
     * 要输出的Cookie列表，使用英文逗号分割，默认是所有
     * @return 返回记录日志的Cookie
     */
    String includeCookies() default "";

    /**
     * 要忽略输出的Cookie列表，使用英文逗号分割，默认没有要忽略
     * @return 要忽略记录日志的Cookie
     */
    String excludeCookies() default "";

    /**
     * 要输出的Header列表，使用英文逗号分割，默认是所有
     * @return 返回记录日志的Header
     */
    String includeHeaders() default "";

    /**
     * 要忽略输出的Header列表，使用英文逗号分割，默认没有要忽略
     * @return 要忽略记录日志的 Header
     */
    String excludeHeaders() default "";
}
