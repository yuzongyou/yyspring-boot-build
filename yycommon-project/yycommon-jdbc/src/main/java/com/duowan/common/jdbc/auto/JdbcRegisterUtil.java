package com.duowan.common.jdbc.auto;

import com.duowan.common.jdbc.Jdbc;
import com.duowan.common.jdbc.model.JdbcDef;
import com.duowan.common.jdbc.parser.JdbcDefParser;
import com.duowan.common.jdbc.provider.dbtype.DBProvider;
import com.duowan.common.jdbc.provider.pooltype.PoolProvider;
import com.duowan.common.jdbc.util.JdbcDefHelper;
import com.duowan.common.jdbc.util.JdbcDefUtil;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.JsonUtil;
import com.duowan.common.utils.ReflectUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
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
public abstract class JdbcRegisterUtil {

    private static final Logger logger = LoggerFactory.getLogger(JdbcRegisterUtil.class);

    /**
     * 解析并注册JDBC
     *
     * @param configMap   数据源配置MAP
     * @param enabledIds  启用的ID列表
     * @param primaryId   设置为主的JdbcDef.id 定义
     * @param environment Spring 环境
     * @param registry    注册入口
     * @return 返回注册的Bean列表
     */
    public synchronized static Map<String, BeanDefinition> registerJdbcBeanDefinitions(Map<String, String> configMap, Set<String> enabledIds, String primaryId, Environment environment, BeanDefinitionRegistry registry) {

        // 获取 JDBC Def 解析器
        List<JdbcDefParser> parserList = JdbcDefUtil.lookupJdbcDefParsers(configMap, environment);

        // 解析标准所有的 JdbcDef
        List<JdbcDef> jdbcDefList = JdbcDefUtil.parseJdbcDefList(configMap, parserList, environment);

        List<DBProvider> dbProviderList = JdbcDefUtil.lookupJdbcDefDbProviders(configMap, environment);
        List<PoolProvider> poolProviderList = JdbcDefUtil.lookupJdbcDefPoolProviders(configMap, environment);

        return doJdbcBeanDefinitionsRegister(dbProviderList, poolProviderList, primaryId, enabledIds, jdbcDefList, registry, environment);
    }

    /**
     * JdbcDef.id --> JdbcDef
     */
    private static Map<String, JdbcDef> JDBC_DEF_MAP = new HashMap<>();

    /**
     * 主 定义
     */
    private static volatile JdbcDef PRIMARY_JDBC_DEF = null;

    /**
     * 检查是否有重复的
     */
    private static List<JdbcDef> checkJdbcDefList(List<JdbcDef> jdbcDefList) {

        if (null == jdbcDefList || jdbcDefList.isEmpty()) {
            return jdbcDefList;
        }

        for (JdbcDef jdbcDef : jdbcDefList) {
            AssertUtil.assertFalse(JDBC_DEF_MAP.containsKey(jdbcDef.getId()), "JdbcDef[id=" + jdbcDef.getId() + "]重复定义！");

            if (jdbcDef.isPrimary()) {
                if (null == PRIMARY_JDBC_DEF) {
                    PRIMARY_JDBC_DEF = jdbcDef;
                } else {
                    AssertUtil.assertTrue(PRIMARY_JDBC_DEF.getId().equals(jdbcDef.getId()),
                            "不能定义多个 primary JdbcDef 目前定义了[" + PRIMARY_JDBC_DEF.getId() + "," + jdbcDef.getId() + "]");
                }

            }

            JDBC_DEF_MAP.put(jdbcDef.getId(), jdbcDef);
        }

        return jdbcDefList;
    }

    /**
     * 注册  Bean
     *
     * @param dbProviderList   DB 提供者列表
     * @param poolProviderList 连接池提供者实例列表
     * @param primaryId        主JdbcID
     * @param enabledIds       要启用的ID列表，支持通配符 *
     * @param jdbcDefList      JDBC 定义列表
     * @param registry         Bean 注册入口
     * @param environment      环境
     * @return 返回注册的BeanDefinition MAP
     */
    public static Map<String, BeanDefinition> doJdbcBeanDefinitionsRegister(List<DBProvider> dbProviderList,
                                                                            List<PoolProvider> poolProviderList,
                                                                            String primaryId,
                                                                            Set<String> enabledIds,
                                                                            List<JdbcDef> jdbcDefList,
                                                                            BeanDefinitionRegistry registry,
                                                                            Environment environment) {

        // 设置属性
        jdbcDefList = JdbcDefHelper.autoFillProperties(jdbcDefList, environment);

        // 提取所有启用的Jdbc定义列表
        jdbcDefList = JdbcDefUtil.extractEnabledJdbcDefList(enabledIds, jdbcDefList);

        // 应用 主 定义
        jdbcDefList = JdbcDefUtil.applyPrimaryJdbcDefList(primaryId, jdbcDefList);

        // 检查定义列表，看看是不是有重复、是否有多个 primary 定义的之类
        jdbcDefList = checkJdbcDefList(jdbcDefList);

        Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

        // 设置默认值
        jdbcDefList = JdbcDefUtil.fillDefaultConfig(jdbcDefList);

        if (jdbcDefList == null || jdbcDefList.isEmpty()) {
            return beanDefinitionMap;
        }

        dbProviderList = appendDefaultDbProviderAndFilterDuplicateInstance(dbProviderList);
        poolProviderList = appendDefaultPoolProviderAndFilterDuplicateInstance(poolProviderList);

        for (JdbcDef jdbcDef : jdbcDefList) {
            logger.info("准备注册JdbcDef[primary=" + jdbcDef.isPrimary() + "]: " + JsonUtil.toPrettyJson(jdbcDef));

            DBProvider dbProvider = JdbcDefUtil.lookupDBProvider(dbProviderList, jdbcDef);
            PoolProvider poolProvider = JdbcDefUtil.lookupPoolProvider(poolProviderList, jdbcDef);

            // 应用连接池和驱动默认配置
            JdbcDefUtil.fillDefaultPoolAndDriverConfig(jdbcDef, dbProvider, poolProvider);

            // 注册 DataSource
            String dsBeanName = registerDataSourceBeanDefinition(beanDefinitionMap, registry, jdbcDef, dbProvider, poolProvider);

            // 注册 JdbcTemplate
            String jdbcTemplateBeanName = registerJdbcTemplateBeanDefinition(beanDefinitionMap, registry, jdbcDef, dbProvider, poolProvider, dsBeanName);

            String txTemplateBeanMame = null;
            // 注册事务管理器和事务模版
            if (jdbcDef.isSupportTx()) {
                // 注册事务管理器
                String txBeanName = registerTransactionManagerBeanDefinition(beanDefinitionMap, registry, jdbcDef, dsBeanName);

                // 注册编程事务模版
                txTemplateBeanMame = registerTransactionTemplate(beanDefinitionMap, registry, jdbcDef, txBeanName);

                if (jdbcDef.isProxyTx()) {
                    // 注册声明式事务拦截器
                    registerTransactionInterceptor(beanDefinitionMap, registry, jdbcDef, txBeanName);
                }
            }

            // 初始化JDBC
            if (jdbcDef.isInitJdbc()) {
                registerJdbcBeanDefinition(beanDefinitionMap, registry, environment, jdbcDef, dbProvider, jdbcTemplateBeanName, txTemplateBeanMame);
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

    /**
     * 注册JDBC实现
     */
    private static String registerJdbcBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, Environment environment, JdbcDef jdbcDef, DBProvider dbProvider, String jdbcTemplateBeanName, String txTemplateBeanMame) {

        String beanName = jdbcDef.getId() + "Jdbc";
        Class<?> clazz = dbProvider.lookupJdbcImplClass(jdbcDef, environment);
        AssertUtil.assertNotNull(clazz, "找不到[" + jdbcDef.getDbType() + "]类型的Jdbc接口实现！");
        AssertUtil.assertTrue(Jdbc.class.isAssignableFrom(clazz), "配置的类[" + clazz.getName() + "]没有实现[" + Jdbc.class.getName() + "]接口！");

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();

        beanDefinition.setBeanClass(clazz);
        beanDefinition.setLazyInit(true);
        beanDefinition.getPropertyValues().addPropertyValue("jdbcTemplate", new RuntimeBeanReference(jdbcTemplateBeanName));

        if (jdbcDef.isSupportTx() && StringUtils.isNotBlank(txTemplateBeanMame)) {
            beanDefinition.getPropertyValues().addPropertyValue("transactionTemplate", new RuntimeBeanReference(txTemplateBeanMame));
        }

        beanDefinition.setPrimary(jdbcDef.isPrimary());
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    private static final int TX_METHOD_TIMEOUT = 5;

    /**
     * 声明式事务拦截器
     */
    private static String registerTransactionInterceptor(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDef jdbcDef, String txBeanName) {

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

        String txInterceptorBeanName = jdbcDef.getId() + "TxInterceptor";

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(TransactionInterceptor.class);
        beanDefinition.setLazyInit(true);
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, new RuntimeBeanReference(txBeanName));
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(1, source);

        beanDefinition.setPrimary(jdbcDef.isPrimary());

        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        registry.registerBeanDefinition(txInterceptorBeanName, beanDefinition);

        beanDefinitionMap.put(txInterceptorBeanName, beanDefinition);

        // 注册代理
        registerBeanNameAutoProxyCreator(beanDefinitionMap, registry, jdbcDef, txInterceptorBeanName);

        return txInterceptorBeanName;
    }

    private static String registerBeanNameAutoProxyCreator(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDef jdbcDef, String txInterceptorBeanName) {

        String beanName = jdbcDef.getId() + "BeanNameAutoProxyCreator";
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(BeanNameAutoProxyCreator.class);
        beanDefinition.setLazyInit(true);

        beanDefinition.getPropertyValues().add("interceptorNames", new String[]{txInterceptorBeanName});
        beanDefinition.getPropertyValues().add("beanNames", new String[]{"*Service", "*ServiceImpl"});
        beanDefinition.getPropertyValues().add("proxyTargetClass", true);
        beanDefinition.setPrimary(jdbcDef.isPrimary());
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    /**
     * 注册 JDBC Transaction Template
     */
    private static String registerTransactionTemplate(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDef jdbcDef, String txManagerBeanName) {
        String beanName = jdbcDef.getId() + "TxTemplate";
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(TransactionTemplate.class);
        beanDefinition.setLazyInit(true);
        beanDefinition.getConstructorArgumentValues()
                .addIndexedArgumentValue(0, new RuntimeBeanReference(txManagerBeanName));

        beanDefinition.setPrimary(jdbcDef.isPrimary());
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    /**
     * 注册事务管理器
     */
    private static String registerTransactionManagerBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDef jdbcDef, String dsBeanName) {

        String beanName = jdbcDef.getId() + "TxManager";

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DataSourceTransactionManager.class);
        beanDefinition.setLazyInit(true);
        beanDefinition.getConstructorArgumentValues()
                .addIndexedArgumentValue(0, new RuntimeBeanReference(dsBeanName));

        beanDefinition.setPrimary(jdbcDef.isPrimary());
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    /**
     * 注册 DataSource
     *
     * @param registry     BeanDefinition 注册器
     * @param jdbcDef      jdbc 定义
     * @param dbProvider   dbProvider
     * @param poolProvider 连接池提供者
     * @param dsBeanName   dataSource BeanName
     * @return 返回 JdbcTemplateBeanName
     */
    private static String registerJdbcTemplateBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDef jdbcDef, DBProvider dbProvider, PoolProvider poolProvider, String dsBeanName) {

        String beanName = jdbcDef.getId() + "JdbcTemplate";
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(JdbcTemplate.class);
        beanDefinition.setLazyInit(true);
        beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, new RuntimeBeanReference(dsBeanName));
        beanDefinition.setPrimary(jdbcDef.isPrimary());
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    /**
     * 注册 DataSource
     *
     * @param registry     BeanDefinition 注册器
     * @param jdbcDef      jdbc 定义
     * @param dbProvider   dbProvider
     * @param poolProvider 连接池提供者
     * @return 返回 DataSourceBeanName
     */
    private static String registerDataSourceBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap, BeanDefinitionRegistry registry, JdbcDef jdbcDef, DBProvider dbProvider, PoolProvider poolProvider) {

        String beanName = jdbcDef.getId() + "DataSource";

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setPrimary(jdbcDef.isPrimary());
        beanDefinition.setBeanClassName(jdbcDef.getDsclazz());
        beanDefinition.setLazyInit(true);

        applyPoolConfig(jdbcDef, poolProvider, beanDefinition);

        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        registry.registerBeanDefinition(beanName, beanDefinition);

        beanDefinitionMap.put(beanName, beanDefinition);

        return beanName;
    }

    private static void applyPoolConfig(JdbcDef jdbcDef, PoolProvider poolProvider, GenericBeanDefinition beanDefinition) {
        Map<String, String> poolConfig = jdbcDef.getPoolConfig();
        if (null == poolConfig) {
            poolConfig = new HashMap<>();
        }

        poolConfig.put(poolProvider.getDriverFieldName(), jdbcDef.getDriverClass());
        poolConfig.put(poolProvider.getUsernameFieldName(), jdbcDef.getUsername());
        poolConfig.put(poolProvider.getPasswordFieldName(), jdbcDef.getPassword());
        poolConfig.put(poolProvider.getJdbcUrlFieldName(), jdbcDef.getJdbcUrl());

        // 应用不同连接池的默认配置
        poolConfig = poolProvider.applyDefaultPoolConfig(poolConfig);

        poolConfig = CommonUtil.filterUnRecordedField(poolConfig, jdbcDef.getDsclazz());

        MutablePropertyValues properties = new MutablePropertyValues(poolConfig);
        beanDefinition.getPropertyValues().addPropertyValues(properties);
    }

}
