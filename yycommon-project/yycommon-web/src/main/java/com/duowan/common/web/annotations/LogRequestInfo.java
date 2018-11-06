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
     * 打印参数信息
     *
     * @return 是否打印
     */
    boolean param() default true;

    /**
     * 打印header参数信息
     *
     * @return 是否打印
     */
    boolean header() default false;

    /**
     * 打印Cookie参数信息
     *
     * @return 是否打印
     */
    boolean cookie() default false;


}
