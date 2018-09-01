package com.duowan.yyspring.boot.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/31 8:48
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface YYSpringBootApplication {

    /**
     * 应用名称，默认按顺序从JVM和环境变量读取变量 DWPROJECTNO, PROJECTNO, APPNO, DWAPPNO
     *
     * @return 返回应用名称
     */
    String projectNo() default "";

    /**
     * 获取当前运行环境的 KEY 值
     *
     * @return 返回获取环境的key值
     */
    String[] envKeys() default {"DWENV", "ENV"};

    /**
     * 资源文件搜索目录，允许自定义路径，自定义的路径会在默认的路径之后， 允许使用 projectNo 变量和系统变量；
     * 例如：
     * /data/app/${projectNo}
     * classpath:/config/${AREA}/
     * classpath:/config/${AREA:wuxi}
     * <p>
     * ${变量:默认值}
     *
     * @return 返回附加的资源文件搜索目录
     */
    String[] resourceLookupDirs() default {};

    /**
     * 是否尝试使用默认的 BeanNameGenerator, 默认是尝试
     *
     * @return 是否尝试
     */
    boolean tryEnabledDefaultBeanNameGenerator() default true;
}
