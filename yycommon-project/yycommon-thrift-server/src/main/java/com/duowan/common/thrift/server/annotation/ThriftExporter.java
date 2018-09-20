package com.duowan.common.thrift.server.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Thrift 服务发布注解， 该注解必须应用在 ThriftExporter 实现类上面
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/9/20 14:35
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface ThriftExporter {

    /**
     * 分组ID，这个用于抽象一个服务分组的概念， 其他 @ThriftService 注解标识的类，需要指定 group 才能发布， 不允许为空
     *
     * @return 返回分组ID
     */
    String group();

    /**
     * 服务端口， 即将服务发布到什么端口上面，默认是 12000
     *
     * @return 返回端口
     */
    int port() default 12000;

    /**
     * 是否以 TMultiplexedProcessor 形式发布服务，默认是true，如果你只是发布一个服务的话，可以将这个改成 false，
     * <p>
     * 如果有多个 Service 对象，这个值为true，设置成false也无效
     *
     * @return 是或否
     */
    boolean multiplexed() default true;
}
