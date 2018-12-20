package com.duowan.common.jdbc.util;

import com.duowan.common.jdbc.Jdbc;
import com.duowan.common.jdbc.model.JdbcDefinition;
import com.duowan.common.jdbc.provider.dbtype.DBProvider;
import com.duowan.common.jdbc.provider.pooltype.PoolProvider;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.JsonUtil;
import com.duowan.common.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;

/**
 * Jdbc 注册器
 *
 * @author Arvin
 */
public class JdbcRegisterUtil {

    private JdbcRegisterUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcRegisterUtil.class);

    /**
     * JdbcDefinition.id -- JdbcDefinition
     */
    private static Map<String, JdbcDefinition> jdbcDefMap = new HashMap<>();

    /**
     * 主 定义
     */
    private static JdbcDefinition primaryJdbcDef = null;

    /**
     * 检查是否有重复的
     */
    private static void checkJdbcDefList(List<JdbcDefinition> jdbcDefinitionList) {

        if (null == jdbcDefinitionList || jdbcDefinitionList.isEmpty()) {
            return;
        }

        for (JdbcDefinition jdbcDefinition : jdbcDefinitionList) {
            AssertUtil.assertFalse(jdbcDefMap.containsKey(jdbcDefinition.getId()), "JdbcDefinition[id=" + jdbcDefinition.getId() + "]重复定义！");

            if (jdbcDefinition.isPrimary()) {
                if (null == primaryJdbcDef) {
                    primaryJdbcDef = jdbcDefinition;
                } else {
                    AssertUtil.assertTrue(primaryJdbcDef.getId().equals(jdbcDefinition.getId()),
                            "不能定义多个 primary JdbcDefinition 目前定义了[" + primaryJdbcDef.getId() + "," + jdbcDefinition.getId() + "]");
                }

            }

            jdbcDefMap.put(jdbcDefinition.getId(), jdbcDefinition);
        }
    }

    /**
     * 注册  Bean
     *
     * @param registerContext        注册上下文
     * @return 返回注册的BeanDefinition MAP
     */
    public static Map<String, BeanDefinition> registerJdbcBeanDefinitions(JdbcRegisterContext registerContext) {

        // 过滤所有排除的 Jdbc 定义列表
        List<JdbcDefinition> jdbcDefinitionList = JdbcDefinitionUtil.filterExcludeDefList(registerContext.getExcludeIds(), registerContext.getJdbcDefinitionList());

        // 提取所有启用的Jdbc定义列表
        jdbcDefinitionList = JdbcDefinitionUtil.extractEnabledJdbcDefList(registerContext.getExcludeIds(), jdbcDefinitionList);

        // 设置属性
        jdbcDefinitionList = JdbcDefinitionUtil.autoFillProperties(jdbcDefinitionList, registerContext.getEnvironment());

        // 应用 主 定义
        jdbcDefinitionList = JdbcDefinitionUtil.applyPrimaryJdbcDefList(registerContext.getPrimaryId(), jdbcDefinitionList);

        // 检查定义列表，看看是不是有重复、是否有多个 primary 定义的之类
        checkJdbcDefList(jdbcDefinitionList);

        Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

        // 设置默认值
        jdbcDefinitionList = JdbcDefinitionUtil.fillDefaultConfig(jdbcDefinitionList);

        if (jdbcDefinitionList == null || jdbcDefinitionList.isEmpty()) {
            LOGGER.info("定义了多个Jdbc，但是未启用！");
            return beanDefinitionMap;
        }

        List<DBProvider> dbProviderList = appendDefaultDbProviderAndFilterDuplicateInstance(registerContext.getDbProviderList());
        List<PoolProvider> poolProviderList = appendDefaultPoolProviderAndFilterDuplicateInstance(registerContext.getPoolProviderList());

        BeanDefinitionRegistry registry = registerContext.getRegistry();

        for (JdbcDefinition jdbcDefinition : jdbcDefinitionList) {
            String prettyJson = JsonUtil.toPrettyJson(jdbcDefinition);
            LOGGER.info("准备注册JdbcDef[primary={}]: {}", jdbcDefinition.isPrimary(), prettyJson);

            DBProvider dbProvider = JdbcDefinitionUtil.lookupDBProvider(dbProviderList, jdbcDefinition);
            PoolProvider poolProvider = JdbcDefinitionUtil.lookupPoolProvider(poolProviderList, jdbcDefinition);

            // 应用连接池和驱动默认配置
            JdbcDefinitionUtil.fillDefaultPoolAndDriverConfig(jdbcDefinition, dbProvider, poolProvider);

            // 注册 DataSource
            String dsBeanName = registerDataSourceBeanDefinition(beanDefinitionMap, registry, jdbcDefinition, poolProvider);

            // 注册 JdbcTemplate
            String jdbcTemplateBeanName = registerJdbcTemplateBeanDefinition(beanDefinitionMap, registry, jdbcDefinition, dsBeanName);

            String txTemplateBeanMame = null;
            // 注册事务管理器和事务模版
            if (jdbcDefinition.isSupportTx()) {
                // 注册事务管理器
                String txBeanName = registerTransactionManagerBeanDefinition(beanDefinitionMap, registry, jdbcDefinition, dsBeanName);

                // 注册编程事务模版
                txTemplateBeanMame = registerTransactionTemplate(beanDefinitionMap, registry, jdbcDefinition, txBeanName);

                if (jdbcDefinition.isProxyTx()) {
                    // 注册声明式事务拦截器
                    registerTransactionInterceptor(beanDefinitionMap, registry, jdbcDefinition, txBeanName);
                }
            }

            // 初始化JDBC
            if (jdbcDefinition.isInitJdbc()) {
                registerJdbcBeanDefinition(beanDefinitionMap, registry, registerContext.getEnvironment(), jdbcDefinition, dbProvider, jdbcTemplateBeanName, txTemplateBeanMame);
            }
        }

        return beanDefinitionMap;
    }

    private static List<PoolProvider> appendDefaultPoolProviderAndFilterDuplicateInstance(List<PoolProvider> providerList) {
        if (providerList == null) {
            providerList = new ArrayList<>();
        }
        CommonUtil.appendList(providerList, ReflectUtil.scanAndInstanceByDefaultConstructor(PoolProvider.class, PoolProvider.class.getPackage().getName()));

        return CommonUtil.filterDuplicateInstance(providerList);
    }

    private static List<DBProvider> appendDefaultDbProviderAndFilterDuplicateInstance(List<DBProvider> providerList) {

        if (providerList == null) {
            providerList = new ArrayList<>();
        }
        CommonUtil.appendList(providerList, ReflectUtil.scanAndInstanceByDefaultConstructor(DBProvider.class, DBProvider.class.getPackage().getName()));

        return CommonUtil.filterDuplicateInstance(providerList);
    }

    private static String registerJdbcBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, Environment environment, JdbcDefinition jdbcDefinition, DBProvider dbProvider, String jdbcTemplateBeanName, String txTemplateBeanMame) {

        String beanName = jdbcDefinition.getId() + "Jdbc";
        Class<?> clazz = dbProvider.lookupJdbcImplClass(jdbcDefinition, environment);
        AssertUtil.assertNotNull(clazz, "找不到[" + jdbcDefinition.getDbType() + "]类型的Jdbc接口实现！");
        AssertUtil.assertTrue(Jdbc.class.isAssignableFrom(clazz), "配置的类[" + clazz.getName() + "]没有实现[" + Jdbc.class.getName() + "]接口！");

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();

        beanDefinition.setBeanClass(clazz);
        beanDefinition.setLazyInit(true);
        beanDefinition.getPropertyValues().addPropertyValue("jdbcTemplate", new RuntimeBeanReference(jdbcTemplateBeanName));

        if (jdbcDefinition.isSupportTx() && StringUtils.isNotBlank(txTemplateBeanMame)) {
            beanDefinition.getPropertyValues().addPropertyValue("transactionTemplate", new RuntimeBeanReference(txTemplateBeanMame));
        }

        beanDefinition.setPrimary(jdbcDefinition.isPrimary());
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    private static final int TX_METHOD_TIMEOUT = 5;

    /**
     * 声明式事务拦截器
     */
    private static String registerTransactionInterceptor(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDefinition jdbcDefinition, String txBeanName) {

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        // 只读事务，不做更新操作
        RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
        readOnlyTx.setReadOnly(true);
        readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);

        // 当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务
        RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
        requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
        requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        requiredTx.setTimeout(TX_METHOD_TIMEOUT);

        Map<String, TransactionAttribute> txMap = new HashMap<>();
        txMap.put("add*", requiredTx);
        txMap.put("save*", requiredTx);
        txMap.put("insert*", requiredTx);
        txMap.put("update*", requiredTx);
        txMap.put("delete*", requiredTx);
        txMap.put("remove*", requiredTx);
        txMap.put("drop*", requiredTx);
        txMap.put("audit*", requiredTx);
        txMap.put("create*", requiredTx);
        txMap.put("apply*", requiredTx);

        txMap.put("get*", readOnlyTx);
        txMap.put("find*", readOnlyTx);
        txMap.put("query*", readOnlyTx);
        txMap.put("select*", readOnlyTx);
        source.setNameMap(txMap);

        String txInterceptorBeanName = jdbcDefinition.getId() + "TxInterceptor";

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(TransactionInterceptor.class);
        beanDefinition.setLazyInit(true);
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, new RuntimeBeanReference(txBeanName));
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, source);

        beanDefinition.setPrimary(jdbcDefinition.isPrimary());

        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        registry.registerBeanDefinition(txInterceptorBeanName, beanDefinition);

        beanDefinitionMap.put(txInterceptorBeanName, beanDefinition);

        // 注册代理
        registerBeanNameAutoProxyCreator(beanDefinitionMap, registry, jdbcDefinition, txInterceptorBeanName);

        return txInterceptorBeanName;
    }

    private static String registerBeanNameAutoProxyCreator(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDefinition jdbcDefinition, String txInterceptorBeanName) {

        String beanName = jdbcDefinition.getId() + "BeanNameAutoProxyCreator";
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(BeanNameAutoProxyCreator.class);
        beanDefinition.setLazyInit(true);

        beanDefinition.getPropertyValues().add("interceptorNames", new String[]{txInterceptorBeanName});
        beanDefinition.getPropertyValues().add("beanNames", new String[]{"*Service", "*ServiceImpl"});
        beanDefinition.getPropertyValues().add("proxyTargetClass", true);
        beanDefinition.setPrimary(jdbcDefinition.isPrimary());
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    /**
     * 注册 JDBC Transaction Template
     */
    private static String registerTransactionTemplate(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDefinition jdbcDefinition, String txManagerBeanName) {
        String beanName = jdbcDefinition.getId() + "TxTemplate";
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(TransactionTemplate.class);
        beanDefinition.setLazyInit(true);
        beanDefinition.getConstructorArgumentValues()
                .addIndexedArgumentValue(0, new RuntimeBeanReference(txManagerBeanName));

        beanDefinition.setPrimary(jdbcDefinition.isPrimary());
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    /**
     * 注册事务管理器
     */
    private static String registerTransactionManagerBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDefinition jdbcDefinition, String dsBeanName) {

        String beanName = jdbcDefinition.getId() + "TxManager";

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DataSourceTransactionManager.class);
        beanDefinition.setLazyInit(true);
        beanDefinition.getConstructorArgumentValues()
                .addIndexedArgumentValue(0, new RuntimeBeanReference(dsBeanName));

        beanDefinition.setPrimary(jdbcDefinition.isPrimary());
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    /**
     * 注册 DataSource
     *
     * @param registry       BeanDefinition 注册器
     * @param jdbcDefinition jdbc 定义
     * @param dsBeanName     dataSource BeanName
     * @return 返回 JdbcTemplateBeanName
     */
    private static String registerJdbcTemplateBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDefinition jdbcDefinition, String dsBeanName) {

        String beanName = jdbcDefinition.getId() + "JdbcTemplate";
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(JdbcTemplate.class);
        beanDefinition.setLazyInit(true);
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, new RuntimeBeanReference(dsBeanName));
        beanDefinition.setPrimary(jdbcDefinition.isPrimary());
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    /**
     * 注册 DataSource
     *
     * @param registry       BeanDefinition 注册器
     * @param jdbcDefinition jdbc 定义
     * @param poolProvider   连接池提供者
     * @return 返回 DataSourceBeanName
     */
    private static String registerDataSourceBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDefinition jdbcDefinition, PoolProvider poolProvider) {

        String beanName = jdbcDefinition.getId() + "DataSource";

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setPrimary(jdbcDefinition.isPrimary());
        beanDefinition.setBeanClassName(jdbcDefinition.getDsclazz());
        beanDefinition.setLazyInit(true);

        applyPoolConfig(jdbcDefinition, poolProvider, beanDefinition);

        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    private static void applyPoolConfig(JdbcDefinition jdbcDefinition, PoolProvider poolProvider, GenericBeanDefinition beanDefinition) {
        Map<String, String> poolConfig = jdbcDefinition.getPoolConfig();
        if (null == poolConfig) {
            poolConfig = new HashMap<>();
        }

        poolConfig.put(poolProvider.getDriverFieldName(), jdbcDefinition.getDriverClass());
        poolConfig.put(poolProvider.getUsernameFieldName(), jdbcDefinition.getUsername());
        poolConfig.put(poolProvider.getPasswordFieldName(), jdbcDefinition.getPassword());
        poolConfig.put(poolProvider.getJdbcUrlFieldName(), jdbcDefinition.getJdbcUrl());

        // 应用不同连接池的默认配置
        poolConfig = poolProvider.applyDefaultPoolConfig(poolConfig);

        poolConfig = CommonUtil.filterUnRecordedField(poolConfig, jdbcDefinition.getDsclazz());

        MutablePropertyValues properties = new MutablePropertyValues(poolConfig);
        beanDefinition.getPropertyValues().addPropertyValues(properties);
    }

}
