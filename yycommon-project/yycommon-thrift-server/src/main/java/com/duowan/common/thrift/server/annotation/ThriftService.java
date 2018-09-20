package com.duowan.common.thrift.server.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 用于标识一个 Thrift 服务实例，实现 Thrift 相关接口
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
     * 分组ID，这个用于抽象一个服务分组的概念， 其他 @ThriftService 注解标识的类，需要指定 group 才能发布， 不允许为空
     *
     * @return 返回分组ID
     */
    String group();

    /**
     * 服务路由地址，默认不需要填写，会计算这个类的名称，首字母小写作为 路由路径，同一个端口发布的服务不允许有重复的路由ID
     * <p>
     * 当然，如果你发布形式是单个服务发布的（不使用 TMultiplexedProcessor 进行发布），那么这个参数无效
     *
     * @return 返回路由路径
     */
    String router() default "";

}
