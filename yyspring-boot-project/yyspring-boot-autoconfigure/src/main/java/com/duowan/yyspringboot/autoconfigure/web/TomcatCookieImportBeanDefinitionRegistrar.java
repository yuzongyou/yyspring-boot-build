package com.duowan.yyspringboot.autoconfigure.web;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/8 11:19
 */
public class TomcatCookieImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private boolean needRegister = false;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {

        if (needRegister) {
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(LegacyCookieProcessorCustomizer.class);
            registry.registerBeanDefinition("tomcatLegacyCookieProcessorCustomizer", beanDefinition);
        }
    }

    @Override
    public void setEnvironment(Environment environment) {
        Boolean dotEnabled = environment.getProperty("yyspring.mvc.cookie.dot-enabled", Boolean.class);
        ClassLoader classLoader = getClass().getClassLoader();
        if (null != dotEnabled && dotEnabled) {
            this.needRegister = ClassUtils.isPresent("org.springframework.boot.web.server.WebServerFactoryCustomizer", classLoader)
                && ClassUtils.isPresent("org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory", classLoader)
                && ClassUtils.isPresent("org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer", classLoader)
                && ClassUtils.isPresent("org.apache.tomcat.util.http.LegacyCookieProcessor", classLoader)
                && ClassUtils.isPresent("org.apache.tomcat.util.http.CookieProcessor", classLoader)
            ;
        }

    }
}
