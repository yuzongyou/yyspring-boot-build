package com.duowan.yyspringboot.autoconfigure.mybatis;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.support.BeanNameGenerator;

import java.lang.annotation.Annotation;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/8 15:52
 */
public class MapperScanConfiguration {

    /**
     * Base packages to scan for MyBatis interfaces. Note that only interfaces
     * with at least one method will be registered; concrete classes will be
     * ignored.
     */
    private String[] basePackages;

    /**
     * Type-safe alternative to {@link #basePackages} for specifying the packages
     * to scan for annotated components. The package of each class specified will be scanned.
     * <p>Consider creating a special no-op marker class or interface in each package
     * that serves no purpose other than being referenced by this attribute.
     */
    private Class<?>[] basePackageClasses;

    /**
     * The {@link BeanNameGenerator} class to be used for naming detected components
     * within the Spring container.
     */
    private Class<? extends BeanNameGenerator> nameGenerator = BeanNameGenerator.class;

    /**
     * This property specifies the annotation that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have
     * the specified annotation.
     * <p>
     * Note this can be combined with markerInterface.
     */
    private Class<? extends Annotation> annotationClass = Annotation.class;

    /**
     * This property specifies the parent that the scanner will search for.
     * <p>
     * The scanner will register all interfaces in the base package that also have
     * the specified interface class as a parent.
     * <p>
     * Note this can be combined with annotationClass.
     */
    private Class<?> markerInterface = Class.class;

    /**
     * Specifies which {@code SqlSessionTemplate} to use in the case that there is
     * more than one in the spring context. Usually this is only needed when you
     * have more than one datasource.
     */
    private String sqlSessionTemplateRef;

    /**
     * Specifies which {@code SqlSessionFactory} to use in the case that there is
     * more than one in the spring context. Usually this is only needed when you
     * have more than one datasource.
     */
    private String sqlSessionFactoryRef;


    /**
     * Specifies a custom MapperFactoryBean to return a mybatis proxy as spring bean.
     */
    private Class<? extends MapperFactoryBean> factoryBean = MapperFactoryBean.class;

    public String[] getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
    }

    public Class<?>[] getBasePackageClasses() {
        return basePackageClasses;
    }

    public void setBasePackageClasses(Class<?>[] basePackageClasses) {
        this.basePackageClasses = basePackageClasses;
    }

    public Class<? extends BeanNameGenerator> getNameGenerator() {
        return nameGenerator;
    }

    public void setNameGenerator(Class<? extends BeanNameGenerator> nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public Class<?> getMarkerInterface() {
        return markerInterface;
    }

    public void setMarkerInterface(Class<?> markerInterface) {
        this.markerInterface = markerInterface;
    }

    public String getSqlSessionTemplateRef() {
        return sqlSessionTemplateRef;
    }

    public void setSqlSessionTemplateRef(String sqlSessionTemplateRef) {
        this.sqlSessionTemplateRef = sqlSessionTemplateRef;
    }

    public String getSqlSessionFactoryRef() {
        return sqlSessionFactoryRef;
    }

    public void setSqlSessionFactoryRef(String sqlSessionFactoryRef) {
        this.sqlSessionFactoryRef = sqlSessionFactoryRef;
    }

    public Class<? extends MapperFactoryBean> getFactoryBean() {
        return factoryBean;
    }

    public void setFactoryBean(Class<? extends MapperFactoryBean> factoryBean) {
        this.factoryBean = factoryBean;
    }
}
