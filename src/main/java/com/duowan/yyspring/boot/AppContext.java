package com.duowan.yyspring.boot;

import com.duowan.common.exception.CodeException;
import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.JsonUtil;
import com.duowan.common.utils.PathUtil;
import com.duowan.esb.core.DefaultEnvReader;
import com.duowan.esb.core.EnvReader;
import com.duowan.esb.core.KeyFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.ApplicationHome;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * App 应用环境上下文
 *
 * @author Arvin
 */
public final class AppContext {

    /**
     * 日志
     */
    protected static Logger logger = LoggerFactory.getLogger(AppContext.class);

    public static final String DEFAULT_SERVER_PORT = "8081";

    public static final String ENV_DEV = "dev";
    public static final String ENV_TEST = "test";
    public static final String ENV_PROD = "prod";

    /**
     * 环境
     */
    private static volatile String env;

    /** 项目代号 */
    private static volatile String projectNo;

    /**
     * Spring 容器上下文
     */
    private static ApplicationContext acx;

    /**
     * Spring 环境上下文
     */
    private static Environment environment;

    /**
     * 自定义资源文件搜索路径列表
     */
    private static List<String> customResourceLookupPaths;

    private static List<String> lazyLogMessages = new ArrayList<>();

    public static ApplicationContext getAcx() {
        return acx;
    }

    public static Environment getEnvironment() {
        return environment;
    }

    public static void lazyLogMessage(String message) {
        lazyLogMessages.add(message);
    }

    public static void prepareAppEnv() {

        if (StringUtils.isNotBlank(AppContext.env)) {
            return;
        }

        // 优先读取 classpath 路径下指定的 EnvReader
        EnvReader envReader = lookupEnvReaderFromEsbFactories();
        if (null == envReader) {
            envReader = new DefaultEnvReader();
        }
        boolean hadGetEnv = false;

        try {

            lazyLogMessage("使用EnvReader: " + envReader.getClass());
            String env = envReader.readEnv();
            if (StringUtils.isNotBlank(env)) {
                customResourceLookupPaths = envReader.getResourceLookupPaths();
                AppContext.setEnv(env);
                hadGetEnv = true;
            }

        } catch (Exception ignored) {
            ignored.printStackTrace();
        }

        if (!hadGetEnv) {
            throw new RuntimeException("无法读取当前应用环境，请实现 EnvReader 接口，并编写[/META-INF/services/com.duowan.esb.env.EnvReader] 内容为实现类的全新类名, 或者在classpath:/META-INF/esb.factories 中配置com.duowan.esb.env.EnvReader=实现类");
        }
    }

    /**
     * 从 classpath:/META-INF/esb.factories 中配置com.duowan.esb.env.EnvReader=实现类
     *
     * @return 返回EnvReader
     */
    private static EnvReader lookupEnvReaderFromEsbFactories() {

        Resource resource = getClasspathResource("/META-INF/esb.factories");

        if (null != resource && resource.exists()) {
            Properties properties = new Properties();
            try {
                properties.load(resource.getInputStream());

                String readerClass = properties.getProperty(EnvReader.class.getName());

                return BeanUtils.instantiateClass(Class.forName(readerClass), EnvReader.class);

            } catch (Exception ignored) {
            }
        }

        return null;
    }

    private static EnvReader lookupEnvReaderFromSPI() {
        ServiceLoader<EnvReader> readers = ServiceLoader.load(EnvReader.class);
        Iterator<EnvReader> iterator = readers.iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public static void setEnv(String env) {
        if (StringUtils.isBlank(AppContext.env) || !AppContext.env.equals(env)) {
            AppContext.env = env;

            // 设置日志
            lookupLog4j2Configuration();

            // 初始化搜索配置文件资源的路径
            initLookupConfigResourcePaths();

            // 初始化 app 配置
            initAppActiveProperties();

            // 初始化项目代号
            initProjectNo();
        }

    }

    private static void initProjectNo() {

        List<String> projectNoKeys = Arrays.asList("DWPROJECTNO", "PROJECTNO");

        for (String key : projectNoKeys) {
            String projectNo = getAppProperty(key, null);
            if (StringUtils.isBlank(projectNo)) {
                projectNo = System.getProperty(key);
            }
            if (StringUtils.isBlank(projectNo)) {
                projectNo = System.getenv(key);
            }
            if (StringUtils.isNotBlank(projectNo)) {
                AppContext.projectNo = projectNo;
                return;
            }
        }
    }

    private static String logFilePath;

    /**
     * <pre>
     * 搜索日志配置文件，这个约定放在classpath路径下， 识别类型包含(同时也是应用顺序， 序号小的将优先应用)：
     * 1. log4j2.xml
     * 2. log4j2-spring.xml
     * 3. log4j2-[当前环境].xml
     * 4. log4j2-spring-[当前环境].xml
     * </pre>
     */
    private static void lookupLog4j2Configuration() {

        String loggingConfigKey = "logging.config";

        String[] lookupConfigurationPaths = new String[]{
                "classpath:log4j2-" + env + ".xml",
                "classpath:log4j2-spring-" + env + ".xml",
                "classpath:log4j2.xml",
                "classpath:log4j2-spring.xml"
        };

        for (String configurationPath : lookupConfigurationPaths) {
            Resource resource = getClasspathResource(configurationPath);
            if (null != resource && resource.exists()) {
                System.setProperty(loggingConfigKey, configurationPath);
                logFilePath = configurationPath;
                break;
            }
        }
    }

    public synchronized static void setAcx(ApplicationContext acx) {
        if (AppContext.acx == null || !AppContext.acx.equals(acx)) {

            if (!lazyLogMessages.isEmpty()) {
                for (String message : lazyLogMessages) {
                    logger.info(message);
                }
            }

            logger.info("当前运行环境： " + env);
            logger.info("当前日志配置： " + logFilePath);

            AppContext.acx = acx;
            AppContext.environment = acx.getEnvironment();

            // 初始化
            initAppContext();
        }
    }

    /**
     * App 应用所有的key集合
     */
    private static volatile Set<String> appKeySet;

    private static volatile boolean hadInit = false;

    /**
     * 初始化上下文
     */
    private static void initAppContext() {

        if (!hadInit) {
            hadInit = true;

            // 将 app 自己的属性配置追加到 Environment 中
            appendAppPropertiesToEnvironment();

            // 初始化所有的 key 集合
            initAppAllKeySet();

        }
    }

    public static String getEnv() {
        return env;
    }

    public static String getProjectNo() {
        if (StringUtils.isBlank(projectNo)) {
            initProjectNo();
        }
        return projectNo;
    }

    /**
     * 给定的环境是否一致
     *
     * @param env 环境
     * @return 返回是否和给定的环境一致
     */
    public static boolean isEnvMatched(String env) {
        return StringUtils.equals(AppContext.env, env);
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

    /**
     * 获取指定 Appkey 对应的 配置值
     *
     * @param appKey       appKey
     * @param defaultValue 默认值
     * @return 返回对应key的值
     */
    public static String getAppProperty(String appKey, String defaultValue) {

        String value = null;
        if (null != environment) {
            value = environment.getProperty(appKey, defaultValue);
        }

        if (StringUtils.isBlank(value) && null != customAppProperties) {
            return customAppProperties.getProperty(appKey, defaultValue);
        }

        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        return environment.resolvePlaceholders(value);
    }

    /**
     * 获取指定 Appkey 对应的 配置值
     *
     * @param appKey       appKey
     * @param defaultValue 默认值
     * @return 返回对应key的值
     */
    public static String getCustomAppProperty(String appKey, String defaultValue) {

        if (null == customAppProperties) {
            return defaultValue;
        }

        String value = customAppProperties.getProperty(appKey, defaultValue);

        if (StringUtils.isBlank(value)) {
            return value;
        }

        return value;
    }

    /**
     * 获取 APP 所有的配置 key
     *
     * @return 返回 配置
     */
    public static Set<String> getAppKeySet() {
        return appKeySet;
    }

    public static Map<String, String> getAppConfigMap() {

        Set<String> keySet = getAppKeySet();

        Map<String, String> configMap = new HashMap<>();

        if (null == keySet || keySet.isEmpty()) {
            return configMap;
        }

        for (String key : keySet) {
            String value = environment.getProperty(key, String.class, null);
            if (StringUtils.isNotBlank(value)) {
                value = environment.resolvePlaceholders(value);
            }
            configMap.put(key, value);
        }

        return configMap;
    }

    private static void initAppAllKeySet() {
        AbstractEnvironment springEnv = (AbstractEnvironment) environment;
        MutablePropertySources propertySources = springEnv.getPropertySources();

        Set<String> allKeySet = new HashSet<>();

        for (PropertySource<?> propertySource : propertySources) {
            if (propertySource instanceof EnumerablePropertySource) {
                Collections.addAll(allKeySet, ((EnumerablePropertySource<?>) propertySource).getPropertyNames());
            }
        }

        AppContext.appKeySet = Collections.unmodifiableSet(allKeySet);
    }

    private static void appendAppPropertiesToEnvironment() {
        AbstractEnvironment springEnv = (AbstractEnvironment) environment;
        MutablePropertySources propertySources = springEnv.getPropertySources();
        propertySources.addFirst(new MapPropertySource("app_props", toMap(customAppProperties)));
    }

    private static Map<String, Object> toMap(Properties properties) {
        Map<String, Object> map = new HashMap<>();

        if (null == properties || properties.isEmpty()) {
            return map;
        }

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            map.put(String.valueOf(entry.getKey()), entry.getValue());
        }

        return map;
    }

    /**
     * 自定义的 App 配置
     */
    private static volatile Properties customAppProperties;

    /**
     * 自定义 App Key 集合
     */
    private static volatile Set<String> customAppKeySet;

    /**
     * 初始化 App 配置，同时追加到 Environment 对象中
     */
    private static void initAppActiveProperties() {

        List<Resource> resourceList = lookupConfigResourceList("application.properties");

        if (null != resourceList && !resourceList.isEmpty()) {

            customAppProperties = new Properties();

            for (Resource resource : resourceList) {

                Properties properties = new Properties();

                try {

                    logger.info("加载配置文件： " + resource.getURL());

                    properties.load(resource.getInputStream());

                    if (!properties.isEmpty()) {
                        customAppProperties.putAll(properties);
                    }

                } catch (Exception e) {
                    throw new CodeException("加载App资源错误：" + e.getMessage(), e);
                }

            }

            fixServerPort(customAppProperties);

            customAppKeySet = new HashSet<>();

            for (Object key : customAppProperties.keySet()) {
                customAppKeySet.add(String.valueOf(key));
            }

            customAppKeySet = Collections.unmodifiableSet(customAppKeySet);
        }

    }

    private static void fixServerPort(Properties properties) {
        String serverPortKey = "server.port";
        String serverPort = System.getProperty(serverPortKey);
        if (StringUtils.isNotBlank(serverPort)) {
            properties.put(serverPortKey, serverPort);
        }

        if (!properties.containsKey(serverPortKey)) {
            // 默认使用8081
            properties.setProperty(serverPortKey, DEFAULT_SERVER_PORT);
        }
    }

    /**
     * 搜索 配置文件
     *
     * @param configFilename 配置文件名称
     * @return 返回配置文件资源
     */
    public static Resource lookupConfigResource(String configFilename) {

        List<String> lookupPaths = extractLookupConfigResourcePaths(configFilename);

        if (null == lookupPaths || lookupPaths.isEmpty()) {
            return null;
        }

        for (String lookupPath : lookupPaths) {

            if (lookupPath.startsWith("classpath")) {
                Resource resource = getClasspathResource(lookupPath);
                if (null != resource && resource.exists()) {
                    return resource;
                }
            } else {
                Resource resource = getResourceFromAbsPath(lookupPath);
                if (null != resource && resource.exists()) {
                    return resource;
                }
            }

        }

        return null;
    }

    /**
     * 搜索 多个配置文件
     *
     * @param configFilename 配置文件名称
     * @return 返回配置文件资源
     */
    public static List<Resource> lookupConfigResourceList(String configFilename) {

        List<Resource> resourceList = new ArrayList<>();

        List<String> lookupPaths = extractLookupConfigResourcePaths(configFilename);

        if (null == lookupPaths || lookupPaths.isEmpty()) {
            return resourceList;
        }

        for (String lookupPath : lookupPaths) {

            if (lookupPath.startsWith("classpath")) {
                Resource resource = getClasspathResource(lookupPath);
                if (null != resource && resource.exists()) {
                    resourceList.add(resource);
                }
            } else {
                Resource resource = getResourceFromAbsPath(lookupPath);
                if (null != resource && resource.exists()) {
                    resourceList.add(resource);
                }
            }

        }

        return resourceList;
    }

    protected static Resource getClasspathResource(String classpathResource) {

        String path = classpathResource.replaceFirst("(?i)classpath\\*?:", "");
        Resource resource = new ClassPathResource(path);
        if (!resource.exists()) {
            return null;
        }
        return resource;
    }

    protected static Resource getResourceFromAbsPath(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                Resource resource = new FileSystemResource(file);
                if (resource.exists()) {
                    return resource;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private static List<String> CONFIG_LOOKUP_PATHS;

    /**
     * 初始化搜索配置文件资源的路径， 配置文件生效顺序：
     * 1. classpath:/config/{env}/application.properties
     * 2. classpath:/config/application-env.properties
     * 3. classpath:/{env}/application.properties
     * 4. classpath:/application-env.properties
     * 5. 自定义 EnvReader 中的配置基路径 /config/{env}/application.properties
     */
    private static void initLookupConfigResourcePaths() {

        List<String> lookupPaths = new ArrayList<>();

        List<String> paths = customResourceLookupPaths;
        if (paths != null && !paths.isEmpty()) {
            lookupPaths.addAll(paths);
        }

        if (isDev()) {
            // 从当前目录开始，读取第一个含有 config 的目录
            try {
                String userDir = String.valueOf(Thread.currentThread().getContextClassLoader().getResource(""));

                userDir = userDir.replaceFirst("(?i)^jar:", "");
                userDir = userDir.replaceFirst("(?i)^file:/", "");
                userDir = userDir.replaceFirst("(?i)[/\\\\][^./\\\\]+\\.jar!.*$", "");

                int index = userDir.lastIndexOf("/target");
                if (index > -1) {
                    userDir = userDir.substring(0, index);
                    userDir = userDir.replaceFirst("(?i)[/\\\\][^./\\\\]+$", "");
                }

                lookupPaths.add(userDir + "/config/");

            } catch (Exception ignored) {
            }
        }

        lookupPaths.add("classpath:/");
        lookupPaths.add("classpath:/config/");

        lazyLogMessage("配置文件搜索路径目录：" + JsonUtil.toJson(lookupPaths));

        CONFIG_LOOKUP_PATHS = lookupPaths;
    }

    /**
     * 提取完整的配置文件路徑
     *
     * @param configFilename 配置文件名称
     * @return 返回非 null 列表
     */
    private static List<String> extractLookupConfigResourcePaths(String configFilename) {

        if (StringUtils.isBlank(configFilename)) {
            return new ArrayList<>();
        }

        int index = configFilename.lastIndexOf('.');
        String name;
        String suffix;
        if (index > -1) {
            name = configFilename.substring(0, index);
            suffix = configFilename.substring(index);
        } else {
            name = configFilename;
            suffix = "";
        }

        List<String> pathList = new ArrayList<>();

        for (String basePath : CONFIG_LOOKUP_PATHS) {
            pathList.add(PathUtil.normalizePath(basePath + "/" + name + "-" + env + suffix));
            pathList.add(PathUtil.normalizePath(basePath + "/" + env + "/" + configFilename));
        }

        return pathList;
    }

    /**
     * 获取符合规则的key集合
     *
     * @param keyFilter key过滤器，满足指定条件的key才会接受
     * @return 返回非 null 集合
     */
    public static Set<String> lookupAllKeys(KeyFilter keyFilter) {
        return lookupKeys(appKeySet, keyFilter);
    }

    /**
     * 获取符合规则的key集合
     *
     * @param keyFilter key过滤器，满足指定条件的key才会接受
     * @return 返回非 null 集合
     */
    public static Set<String> lookupCustomKeys(KeyFilter keyFilter) {
        return lookupKeys(customAppKeySet, keyFilter);
    }

    private static Set<String> lookupKeys(Set<String> keySet, KeyFilter keyFilter) {
        if (keySet == null || keySet.isEmpty()) {
            return keySet;
        }

        if (null == keyFilter) {
            return keySet;
        }
        Set<String> resultKeySet = new HashSet<>();
        for (String key : keySet) {
            if (keyFilter.filter(key)) {
                resultKeySet.add(key);
            }
        }
        return resultKeySet;
    }


    /**
     * 获取 资源路径跟路径
     *
     * @return 返回绝对路径
     */
    public static String getAppRootResourceDir() {
        return appRootDir + "src/main/resources/";
    }

    /**
     * 获取 测试资源路径跟路径
     *
     * @return 返回绝对路径
     */
    public static String getAppRootTestResourceDir() {
        return appRootDir + "src/test/resources/";
    }

    /**
     * 启动类
     */
    private static Class<?> startClass;

    /**
     * 设置启动类
     *
     * @param startClass 启动类
     */
    public static void setStartClass(Class<?> startClass) {
        AssertUtil.assertTrue(null == AppContext.startClass || AppContext.startClass.equals(startClass), "已经设置了启动class");

        if (null == AppContext.startClass || !AppContext.startClass.equals(startClass)) {
            AppContext.startClass = startClass;

            initForStartClass();
        }

    }

    private static String projectRootDir;
    private static String appRootDir;

    public static String getAppRootDir() {
        return appRootDir;
    }

    public static String getProjectRootDir() {
        return projectRootDir;
    }

    private static void initForStartClass() {

        initAppRootDir();
    }

    /**
     * WAR 包 的LIB目录
     */
    private static final Pattern WAR_LIB_FOLDER_REGEX = Pattern.compile("(?i)(.*)[/\\\\]WEB-INF[/\\\\]lib$");
    private static final String WAR_CLASSES_FOLDER_REGEX_PREFIX = "(?i)(.*)[/\\\\]WEB-INF[/\\\\]classes[/\\\\]";

    /**
     * <pre>
     * 初始化项目根目录， parent pom.xml 所在目录
     * 1. 如果是使用外部容器启动， 那么 user.dir 就是容器命令所在目录，比如 tomcat 容器那么返回的就是tomcat启动脚本命令所在目录，如D:\apache-tomcat-7.0.57\bin
     * 2. 非 外部容器可以直接获取到项目根路径
     *
     * 因此不能使用 userdir 来计算项目路径， 可以采用 SpringBoot 中的 ApplicationHome 来计算
     *
     * ApplicationHome:
     * 1. 打包成 jar 模式，所有的资源都会打包成一个可执行的 jar， 因此获取到目录就是 该可执行的 jar 目录
     * 2. 打包成 war 模式，结果是标准的 war 包， 因此 返回的目录是 本类所在 jar 的目录， 通常是 WEB-INF/lib
     *   (一般使用本模版的话， env 模块会打包到 WEB-INF/lib)
     *
     * </pre>
     */
    private static void initAppRootDir() {

        ApplicationHome home = new ApplicationHome(startClass);

        String homeDir = home.getDir().getAbsolutePath();

        if (isDev()) {

            // 项目根目录
            projectRootDir = PathUtil.normalizePath(homeDir.replaceFirst("[/\\\\][^/\\\\]+[/\\\\]target[/\\\\].*$", "/") + "/");
            // 模块APP目录
            appRootDir = PathUtil.normalizePath(homeDir.replaceFirst("[/\\\\]target[/\\\\].*$", "/") + "/");

        } else {

            Matcher matcher = WAR_LIB_FOLDER_REGEX.matcher(homeDir);
            if (matcher.find()) {
                projectRootDir = PathUtil.normalizePath(matcher.replaceAll("$1/"));
                appRootDir = projectRootDir;
            } else {
                String sourceClassPackage = startClass.getPackage().getName();

                String packageToRegex = sourceClassPackage.replaceAll("\\.", "[/\\\\\\\\]");

                Pattern pattern = Pattern.compile(WAR_CLASSES_FOLDER_REGEX_PREFIX + packageToRegex);

                matcher = pattern.matcher(homeDir);

                if (matcher.find()) {
                    projectRootDir = PathUtil.normalizePath(matcher.replaceAll("$1/"));
                    appRootDir = projectRootDir;
                } else {
                    // 非 war， jar模式
                    projectRootDir = PathUtil.normalizePath(homeDir.replaceAll("classes$", "/"));
                    appRootDir = PathUtil.normalizePath(homeDir.replaceAll("classes$", "/"));
                }
            }
        }

        lazyLogMessage(getEnv() + "HomeDir: " + homeDir);
        lazyLogMessage(getEnv() + "ProjectRootDir: " + projectRootDir);
        lazyLogMessage(getEnv() + "DevAppRootDir: " + projectRootDir);
    }

    /**
     * 忽略自动配置类
     */
    private static List<String> autoconfigExcludeClassList = new ArrayList<>();

    /**
     * 忽略自动配置
     *
     * @param clazz 要忽略的自动配置类全路径
     */
    public synchronized static void excludeAutoConfigClass(String clazz) {

        if (StringUtils.isBlank(clazz)) {
            return;
        }

        int size = autoconfigExcludeClassList.size();
        int searchIndex = 0;
        if (autoconfigExcludeClassList.contains(clazz)) {
            for (int i = 0; i < size; ++i) {
                if (clazz.equals(autoconfigExcludeClassList.get(i))) {
                    searchIndex = i;
                }
            }
        } else {
            autoconfigExcludeClassList.add(clazz);
            searchIndex = autoconfigExcludeClassList.size() - 1;
        }

        System.setProperty("spring.autoconfigure.exclude[" + searchIndex + "]", clazz);

    }
}
