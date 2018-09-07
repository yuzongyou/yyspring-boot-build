package com.duowan.yyspring.boot.annotations;

import com.duowan.yyspring.boot.DefaultEnvReader;
import com.duowan.yyspring.boot.DefaultProjectNoReader;
import com.duowan.yyspring.boot.EnvReader;
import com.duowan.yyspring.boot.ProjectNoReader;

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
     * 项目代号读取器
     *
     * @return 返回应用代号读取类
     */
    Class<? extends ProjectNoReader> projectNoReader() default DefaultProjectNoReader.class;

    /**
     * 运行环境读取器
     *
     * @return 返回环境读取类
     */
    Class<? extends EnvReader> envReader() default DefaultEnvReader.class;

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
