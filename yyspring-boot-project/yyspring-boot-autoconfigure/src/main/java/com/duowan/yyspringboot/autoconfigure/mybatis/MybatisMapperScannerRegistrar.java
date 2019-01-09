package com.duowan.yyspringboot.autoconfigure.mybatis;

import com.duowan.common.utils.CollectionUtil;
import com.duowan.common.utils.StringUtil;
import com.duowan.yyspring.boot.AppContext;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.ClassPathMapperScanner;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/8 18:56
 */
public class MybatisMapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private static final PathMatchingResourcePatternResolver RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

    private static final String SQL_SESSION_FACTORY_BEAN_NAME_SUFFIX = "SqlSessionFactory";

    private static final String DEFAULT_SQL_SESSION_FACTORY_BEAN_NAME = "default" + SQL_SESSION_FACTORY_BEAN_NAME_SUFFIX;

    private ResourceLoader resourceLoader;

    private MybatisProperties properties;

    private String getSqlSessionFactoryBeanName(String prefix) {
        if (StringUtil.isBlank(prefix)) {
            return DEFAULT_SQL_SESSION_FACTORY_BEAN_NAME;
        }
        return prefix + SQL_SESSION_FACTORY_BEAN_NAME_SUFFIX;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.properties = AppContext.bindProperties(environment, MybatisProperties.MYBATIS_PREFIX, MybatisProperties.class);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (this.properties == null) {
            return;
        }

        List<MybatisConfiguration> configurationList = properties.getConfigurations();

        if (CollectionUtil.isEmpty(configurationList)) {
            return;
        }

        for (MybatisConfiguration configuration : configurationList) {

            String sqlSessionFactoryBeanName = registerMybatisSqlSessionFactoryBeanDefinition(configuration, registry);

            registerMybatisMapperBeanDefinitions(configuration, sqlSessionFactoryBeanName, registry);
        }

    }

    private String registerMybatisSqlSessionFactoryBeanDefinition(MybatisConfiguration mybatisConfiguration, BeanDefinitionRegistry registry) {

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(SqlSessionFactoryBean.class);
        MutablePropertyValues mutablePropertyValues = beanDefinition.getPropertyValues();
        String dataSourceRef = mybatisConfiguration.getDataSourceId() + "DataSource";

        mutablePropertyValues.addPropertyValue("dataSource", new RuntimeBeanReference(dataSourceRef));
        mutablePropertyValues.addPropertyValue("vfs", SpringBootVFS.class);

        if (StringUtils.hasText(mybatisConfiguration.getConfigLocation())) {
            mutablePropertyValues.addPropertyValue("configLocation", RESOURCE_RESOLVER.getResource(mybatisConfiguration.getConfigLocation()));
        }

        Configuration configuration = mybatisConfiguration.getConfiguration();
        if (configuration == null && !StringUtils.hasText(mybatisConfiguration.getConfigLocation())) {
            configuration = new Configuration();
        }

        if (null != configuration) {
            mutablePropertyValues.addPropertyValue("configuration", configuration);
        }
        if (mybatisConfiguration.getConfigurationProperties() != null) {
            mutablePropertyValues.addPropertyValue("configurationProperties", mybatisConfiguration.getConfigurationProperties());
        }

        String[] interceptorRefs = mybatisConfiguration.getInterceptorRefs();
        if (!StringUtil.isAllBlank(interceptorRefs)) {
            ManagedList<RuntimeBeanReference> interceptorList = new ManagedList<>();
            for (String interceptorRef : interceptorRefs) {
                if (StringUtil.isNotBlank(interceptorRef)) {
                    interceptorList.add(new RuntimeBeanReference(interceptorRef));
                }
            }
            mutablePropertyValues.addPropertyValue("plugins", interceptorList);
        }

        String databaseIdProviderRef = mybatisConfiguration.getDatabaseIdProviderRef();
        if (StringUtil.isNotBlank(databaseIdProviderRef)) {
            mutablePropertyValues.addPropertyValue("databaseIdProvider", databaseIdProviderRef);
        }
        if (StringUtils.hasLength(mybatisConfiguration.getTypeAliasesPackage())) {
            mutablePropertyValues.addPropertyValue("typeAliasesPackage", mybatisConfiguration.getTypeAliasesPackage());
        }
        if (StringUtils.hasLength(mybatisConfiguration.getTypeHandlersPackage())) {
            mutablePropertyValues.addPropertyValue("typeHandlersPackage", mybatisConfiguration.getTypeHandlersPackage());
        }
        if (!ObjectUtils.isEmpty(mybatisConfiguration.resolveMapperLocations())) {
            mutablePropertyValues.addPropertyValue("mapperLocations", mybatisConfiguration.resolveMapperLocations());
        }

        String beanName = getSqlSessionFactoryBeanName(mybatisConfiguration.getDataSourceId());
        registry.registerBeanDefinition(beanName, beanDefinition);

        return beanName;
    }

    private void registerMybatisMapperBeanDefinitions(MybatisConfiguration mybatisConfiguration, String sqlSessionFactoryBeanName, BeanDefinitionRegistry registry) {

        ClassPathMapperScanner scanner = new ClassPathMapperScanner(registry);

        // this check is needed in Spring 3.1
        if (resourceLoader != null) {
            scanner.setResourceLoader(resourceLoader);
        }

        MapperScanConfiguration configuration = mybatisConfiguration.getMapperScan();

        Class<? extends Annotation> annotationClass = configuration.getAnnotationClass();
        if (!Annotation.class.equals(annotationClass)) {
            scanner.setAnnotationClass(annotationClass);
        }

        Class<?> markerInterface = configuration.getMarkerInterface();
        if (!Class.class.equals(markerInterface)) {
            scanner.setMarkerInterface(markerInterface);
        }

        Class<? extends BeanNameGenerator> generatorClass = configuration.getNameGenerator();
        if (!BeanNameGenerator.class.equals(generatorClass)) {
            scanner.setBeanNameGenerator(BeanUtils.instantiateClass(generatorClass));
        }

        Class<? extends MapperFactoryBean> mapperFactoryBeanClass = configuration.getFactoryBean();
        if (!MapperFactoryBean.class.equals(mapperFactoryBeanClass)) {
            scanner.setMapperFactoryBean(BeanUtils.instantiateClass(mapperFactoryBeanClass));
        }

        String sqlSessionFactoryRef = configuration.getSqlSessionFactoryRef();
        if (StringUtil.isBlank(sqlSessionFactoryRef)) {
            sqlSessionFactoryRef = sqlSessionFactoryBeanName;
        }
        scanner.setSqlSessionFactoryBeanName(sqlSessionFactoryRef);

        List<String> basePackages = new ArrayList<String>();
        if (!StringUtil.isAllBlank(configuration.getBasePackages())) {
            for (String pkg : configuration.getBasePackages()) {
                if (StringUtils.hasText(pkg)) {
                    basePackages.add(pkg);
                }
            }
        }
        Class<?>[] classes = configuration.getBasePackageClasses();
        if (classes != null && classes.length > 0) {
            for (Class<?> clazz : classes) {
                basePackages.add(ClassUtils.getPackageName(clazz));
            }
        }
        scanner.registerFilters();
        scanner.doScan(StringUtils.toStringArray(basePackages));

    }
}
