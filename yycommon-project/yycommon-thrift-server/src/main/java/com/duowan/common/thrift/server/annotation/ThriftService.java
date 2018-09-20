package com.duowan.common.thrift.server.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 用于标识一个 Thrift 服务实例并发布，实现 Thrift 相关接口
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 14:16
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface ThriftService {

    /**
     * 服务路由地址，默认不需要填写，会计算这个类的名称作为路由路径，同一个端口发布的服务不允许有重复的路由ID
     *
     * @return 返回路由路径
     */
    String router() default "";

}
