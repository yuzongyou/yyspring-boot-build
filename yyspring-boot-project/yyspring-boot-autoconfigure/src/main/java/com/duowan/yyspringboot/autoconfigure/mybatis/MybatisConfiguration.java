package com.duowan.yyspringboot.autoconfigure.mybatis;

import com.duowan.common.utils.StringUtil;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/8 15:40
 */
public class MybatisConfiguration {

    /**
     * 数据源标识
     **/
    private String dataSourceId;

    /**
     * Location of MyBatis xml config file.
     */
    private String configLocation;

    /**
     * Locations of MyBatis mapper files.
     */
    private String[] mapperLocations;

    /**
     * Packages to search type aliases. (Package delimiters are ",; \t\n")
     */
    private String typeAliasesPackage;

    /**
     * Packages to search for type handlers. (Package delimiters are ",; \t\n")
     */
    private String typeHandlersPackage;

    /**
     * Indicates whether perform presence check of the MyBatis xml config file.
     */
    private Boolean checkConfigLocation;

    /**
     * Execution mode for {@link org.mybatis.spring.SqlSessionTemplate}.
     */
    private ExecutorType executorType;

    /**
     * Externalized properties for MyBatis configuration.
     */
    private Properties configurationProperties;

    /**
     * the mybatis interceptor bean refs
     **/
    private String[] interceptorRefs;

    /** The database id provider ref **/
    private String databaseIdProviderRef;

    /**
     * A Configuration object for customize default settings. If {@link #configLocation}
     * is specified, this property is not used.
     */
    @NestedConfigurationProperty
    private Configuration configuration;

    @NestedConfigurationProperty
    private MapperScanConfiguration mapperScan;

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    /**
     * @since 1.1.0
     */
    public String getConfigLocation() {
        return this.configLocation;
    }

    /**
     * @since 1.1.0
     */
    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }

    public String[] getMapperLocations() {
        return this.mapperLocations;
    }

    public void setMapperLocations(String[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public String getTypeHandlersPackage() {
        return this.typeHandlersPackage;
    }

    public void setTypeHandlersPackage(String typeHandlersPackage) {
        this.typeHandlersPackage = typeHandlersPackage;
    }

    public String getTypeAliasesPackage() {
        return this.typeAliasesPackage;
    }

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public boolean isCheckConfigLocation() {
        return this.checkConfigLocation == null ? false : this.checkConfigLocation;
    }

    public void setCheckConfigLocation(Boolean checkConfigLocation) {
        this.checkConfigLocation = checkConfigLocation;
    }

    public ExecutorType getExecutorType() {
        return this.executorType;
    }

    public void setExecutorType(ExecutorType executorType) {
        this.executorType = executorType;
    }

    /**
     * @since 1.2.0
     */
    public Properties getConfigurationProperties() {
        return configurationProperties;
    }

    /**
     * @since 1.2.0
     */
    public void setConfigurationProperties(Properties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    public String[] getInterceptorRefs() {
        return interceptorRefs;
    }

    public void setInterceptorRefs(String[] interceptorRefs) {
        this.interceptorRefs = interceptorRefs;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public MapperScanConfiguration getMapperScan() {
        return mapperScan;
    }

    public void setMapperScan(MapperScanConfiguration mapperScan) {
        this.mapperScan = mapperScan;
    }

    public String getDatabaseIdProviderRef() {
        return databaseIdProviderRef;
    }

    public void setDatabaseIdProviderRef(String databaseIdProviderRef) {
        this.databaseIdProviderRef = databaseIdProviderRef;
    }

    public Resource[] resolveMapperLocations() {
        ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
        List<Resource> resources = new ArrayList<Resource>();
        if (this.mapperLocations != null) {
            for (String mapperLocation : this.mapperLocations) {
                try {
                    Resource[] mappers = resourceResolver.getResources(mapperLocation);
                    resources.addAll(Arrays.asList(mappers));
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        return resources.toArray(new Resource[resources.size()]);
    }

    /**
     * 合并， 如果自身不为空的则以自身为主，否则以 basic 的为主
     *
     * @param basic 基础配置
     * @return 返回合并后的
     */
    public MybatisConfiguration merge(MybatisConfiguration basic) {

        if (basic == null) {
            return this;
        }

        if (null == this.checkConfigLocation) {
            this.setCheckConfigLocation(basic.isCheckConfigLocation());
        }

        if (StringUtil.isBlank(this.configLocation)) {
            this.setConfigLocation(basic.getConfigLocation());
        }

        if (StringUtil.isAllBlank(this.mapperLocations)) {
            this.setMapperLocations(basic.getMapperLocations());
        }

        if (StringUtil.isBlank(this.typeAliasesPackage)) {
            this.setTypeAliasesPackage(basic.getTypeAliasesPackage());
        }

        if (StringUtil.isBlank(this.typeHandlersPackage)) {
            this.setTypeHandlersPackage(basic.getTypeHandlersPackage());
        }

        if (null == this.executorType) {
            this.setExecutorType(basic.getExecutorType());
        }

        if (StringUtil.isAllBlank(this.interceptorRefs)) {
            this.setInterceptorRefs(basic.getInterceptorRefs());
        }


        return this;
    }
}
