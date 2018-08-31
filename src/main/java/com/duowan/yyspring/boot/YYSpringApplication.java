package com.duowan.yyspring.boot;

import com.duowan.common.exception.CodeException;
import com.duowan.common.utils.ReflectUtil;
import com.duowan.common.utils.exception.AssertFailException;
import com.duowan.yyspring.boot.annotations.YYSpringBootApplication;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/29 9:45
 */
public class YYSpringApplication {

    /**
     * 日志
     */
    protected static Logger logger = LoggerFactory.getLogger(AppContext.class);

    private SpringApplicationBuilder applicationBuilder;

    private boolean tryEnabledDefaultBeanNameGenerator = true;

    private Object[] sources;

    private StandardEnvironment appEnvironment = new StandardEnvironment();

    public YYSpringApplication(Object... sources) {
        this.sources = sources;
        this.initialize(this.sources);
        this.applicationBuilder = new SpringApplicationBuilder(sources);
    }

    public static YYSpringApplication build(Object... sources) {
        return new YYSpringApplication(sources);
    }

    public YYSpringApplication configure(SpringApplicationBuilderConfigurer configurer) {
        if (null != configurer) {
            configurer.configure(applicationBuilder);
        }
        return this;
    }

    /**
     * 是否尝试使用默认的 BeanNameGenerator
     *
     * @param tryEnabledDefaultBeanNameGenerator 是否要尝试使用
     * @return 返回YYSpringAppliciation
     */
    public YYSpringApplication tryEnabledDefaultBeanNameGenerator(boolean tryEnabledDefaultBeanNameGenerator) {
        this.tryEnabledDefaultBeanNameGenerator = tryEnabledDefaultBeanNameGenerator;
        return this;
    }

    /**
     * 运行应用程序
     *
     * @param args 启动参数
     * @return 返回ApplicationContext
     */
    public ConfigurableApplicationContext run(String... args) {

        if (this.tryEnabledDefaultBeanNameGenerator) {
            SpringApplication application = this.applicationBuilder.application();

            // 如果用户没有自定义 BeanNameGenerator 则默认给一个
            Object beanNameGenerator = ReflectUtil.getFieldValue(application, "beanNameGenerator");
            if (null == beanNameGenerator) {
                applicationBuilder.beanNameGenerator(new DefaultAnnotationBeanNameGenerator());
            }
        }

        return this.applicationBuilder.run(args);
    }

    public static final String DEFAULT_SERVER_PORT = "8081";

    public static final String ENV_DEV = "dev";
    public static final String ENV_TEST = "test";
    public static final String ENV_PROD = "prod";

    /**
     * 环境
     */
    private static volatile String env;

    /**
     * 项目代号
     */
    private static volatile String projectNo;

    /**
     * 当前运行模块的根路径
     **/
    private static volatile String moduleDir;

    /**
     * 资源文件搜索目录
     **/
    private static List<String> resourceLookupDirs = new ArrayList<>();

    public static String getEnv() {
        return env;
    }

    /**
     * 给定的环境是否一致
     *
     * @param givenEnv 环境
     * @return 返回是否和给定的环境一致
     */
    public static boolean isEnvMatched(String givenEnv) {
        return StringUtils.equals(givenEnv, env);
    }

    public static boolean isDev() {
        return ENV_DEV.equals(env);
    }

    public static boolean isTest() {
        return ENV_TEST.equals(env);
    }

    public static boolean isProd() {
        return ENV_PROD.equals(env);
    }

    public static String getProjectNo() {
        return projectNo;
    }

    /**
     * 初始化应用环境， 项目代号， 环境， 日志等
     *
     * @param sources 应用启动来源
     */
    private void initialize(Object[] sources) {
        Object source = validateSourcesThenReturnFirstNotNullSource(sources);

        Class<?> sourceClass = getObjectClass(source);

        YYSpringBootApplication applicationAnn = sourceClass.getAnnotation(YYSpringBootApplication.class);

        env = deduceRuntimeEnv(sourceClass, applicationAnn);
        moduleDir - deduceModuleDir(sourceClass, applicationAnn);
        projectNo = deduceProjectNo(sourceClass, applicationAnn);

        Map<String, Object> projectInfoMap = new HashMap<>();
        projectInfoMap.put("projectNo", projectNo);
        projectInfoMap.put("env", env);
        projectInfoMap.put("moduleDir", moduleDir);
        appEnvironment.getPropertySources().addLast(new MapPropertySource("projectInfo", projectInfoMap));

        resourceLookupDirs = deduceResourceLookupDirs(sourceClass, applicationAnn);


    }

    /**
     * 推断资源搜索目录，在给定的目录下按照如下规则进行资源文件加载：
     *  ${LOOKUP_DIR}/
     */
    private List<String> deduceResourceLookupDirs(Class<?> sourceClass, YYSpringBootApplication applicationAnn) {





    }

    private String deduceRuntimeEnv(Class<?> sourceClass, YYSpringBootApplication applicationAnn) {

        String[] lookupEnvKeys = null;
        if (null != applicationAnn) {
            lookupEnvKeys = applicationAnn.envKeys();
        }
        if (StringUtils.isAllBlank(lookupEnvKeys)) {
            lookupEnvKeys = new String[]{"DWENV", "ENV"};
        }

        return lookupFirstNotBlankValue(lookupEnvKeys, ENV_DEV);
    }

    private String deduceProjectNo(Class<?> sourceClass, YYSpringBootApplication applicationAnn) {
        String pno = null;
        if (null != applicationAnn) {
            pno = applicationAnn.projectNo();
        }
        if (StringUtils.isBlank(pno)) {
            String[] lookupProjectNoKeys = new String[]{"DWPROJECTNO", "PROJECTNO", "APPNO", "DWAPPNO"};
            pno = lookupFirstNotBlankValue(lookupProjectNoKeys, null);
            if (StringUtils.isNotBlank(pno)) {
                return pno;
            }
        }
        // 如果还是为空，则抛出异常，表示无法识别项目代号
        throw new CodeException(500, "无法识别项目代号！");
    }

    private String lookupFirstNotBlankValue(String[] keys, String defaultValue) {
        for (String projectNoKey : keys) {
            String value = appEnvironment.resolvePlaceholders(projectNoKey);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
        }
        return defaultValue;
    }

    private String getSystemVar(String key, String defaultValue) {
        String env = System.getenv(key);

        if (StringUtils.isBlank(env)) {
            return System.getProperty(key, defaultValue);
        }

        return env;
    }

    private Class<?> getObjectClass(Object object) {
        if (null == object) {
            return null;
        }
        if (object instanceof Class) {
            return (Class<?>) object;
        } else {
            return object.getClass();
        }
    }

    public static void main(String[] args) {
        YYSpringApplication.build(YYSpringApplication.class);
    }

    private Object validateSourcesThenReturnFirstNotNullSource(Object[] sources) {
        String message = "必须提供应用Source对象";
        if (null == sources || sources.length < 1) {
            throw new AssertFailException(message);
        }

        for (Object source : sources) {
            if (null != source) {
                return source;
            }
        }
        throw new AssertFailException(message);
    }
}
