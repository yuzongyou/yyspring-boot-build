package com.duowan.common.thrift.client.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Thrift 接口自动注入
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 11:17
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ThriftResource {

    @AliasFor("router")
    String value() default "";

    /**
     * 制定路由，如果只有一个话就可以不需要指定
     *
     * @return 返回路由ID
     */
    @AliasFor("value")
    String router() default "";

    /**
     * Bean 的名称，如果有多个的情况下，需要指定名称，默认情况下会按照 serviceClass + router + clientType 来计算BeanName
     *
     * @return 返回Bean的名称
     */
    String name() default "";
}
