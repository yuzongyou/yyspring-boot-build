package com.duowan.esb.core;

import com.duowan.common.utils.ClassUtil;
import com.duowan.common.utils.PathUtil;
import com.duowan.common.utils.ReflectUtil;
import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspring.boot.SpringApplicationBuilderConfigurer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.core.io.UrlResource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Arvin
 */
public abstract class AbstractWebApplicationStarter extends SpringBootServletInitializer implements WebMvcConfigurer {

    protected static Logger logger;

    static {
        AppContext.prepareAppEnv();
    }

    /**
     * 外置容器才会执行这里的代码
     *
     * @param servletContext web环境上下文
     */
    @Override
    protected WebApplicationContext createRootApplicationContext(ServletContext servletContext) {
        WebApplicationContext applicationContext = super.createRootApplicationContext(servletContext);
        AppContext.setAcx(applicationContext);

        initLogger();

        return applicationContext;
    }

    private synchronized static void initLogger() {
        if (logger == null) {

            logger = LoggerFactory.getLogger(AbstractWebApplicationStarter.class);

            logInitMessageNow(logger);
        }
    }

    private static List<String> initMessageList = new ArrayList<>();

    protected static void logInitMessageNow(Logger logger) {
        for (String message : initMessageList) {
            logger.info(message);
        }
    }

    /**
     * 初始化系统环境, 只要WebApplication继承本类就可以，因为同一个ClassLoader， 所以初始化方法会优先执行
     */
    protected static void initWebEnv(Class<?> appClass, String[] args, boolean runByBuiltInContainer) throws Exception {
    }

    /**
     * 启动应用
     *
     * @param appClass 应用主入口class
     * @param args     命令行参数
     */
    protected static void startApp(Class<?> appClass, String[] args) throws Exception {
        startApp(appClass, args, null);
    }

    /**
     * 启动应用
     *
     * @param appClass   应用主入口class
     * @param args       命令行参数
     * @param configurer 应用启动配置
     */
    protected static void startApp(Class<?> appClass, String[] args, SpringApplicationBuilderConfigurer configurer) throws Exception {

        SpringApplicationBuilder builder = new SpringApplicationBuilder(appClass)
                .web(true);

        if (configurer != null) {
            configurer.configure(builder);
        }

        prepareWebEnv(appClass, args, builder, true);

        SpringApplication application = builder.build();

        // 如果用户没有自定义 BeanNameGenerator 则默认给一个
        Object beanNameGenerator = ReflectUtil.getFieldValue(application, "beanNameGenerator");
        if (null == beanNameGenerator) {
            application.setBeanNameGenerator(new EsbAnnotationBeanNameGenerator());
        }

        AppContext.setAcx(application.run(args));

        initLogger();
    }

    protected static void prepareWebEnv(Class<?> appClass, String[] args, SpringApplicationBuilder builder, boolean runByBuiltInContainer) throws Exception {

        AppContext.setStartClass(appClass);

        // 初始化应用环境
        initWebEnv(appClass, args, runByBuiltInContainer);

        // 如果是开发环境的话，重置静态资源路径
        resetStaticResourceLocationsIfDevMode();

        if (isThymeleafImported()) {

            // 如果是开发环境的话，重置 thymeleaf 路径
            resetThymeleafTemplatePrefixIfDevMode();

            // Thymeleaf 缓存， 开发环境如果没有设置怎么默认就是 false
            resetThymeleafCacheIfDevMode();

            // Thymeleaf 模版模式， 3.0 默认设置为 HTML
            resetThymeleafModeIfDevMode();
        }
    }

    private static boolean isThymeleafImported() {
        try {
            Set<Class<?>> classSet = ClassUtil.scan(false, new String[]{"org.thymeleaf"}, null);

            return null != classSet && !classSet.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * war 包启动方式，外部 web 容器启动
     *
     * @param application 应用
     * @return
     */
    @Override
    protected final SpringApplicationBuilder configure(SpringApplicationBuilder application) {

        try {

            prepareWebEnv(getClass(), new String[]{}, application, false);

        } catch (Exception e) {
            logInitMessage("初始化 Web 环境失败： " + e.getMessage());
            throw new RuntimeException(e);
        }

        application = customConfigure(application.sources(getClass()));

        return application;
    }

    /**
     * 自定义配置
     *
     * @param application app builder
     * @return
     */
    protected SpringApplicationBuilder customConfigure(SpringApplicationBuilder application) {
        return application;
    }

    public static boolean isClasspath(String path) {
        return null != path && path.trim().matches("(?i)^classpath:.*");
    }

    public static String trimClasspath(String path) {
        return isClasspath(path) ? path.replaceFirst("(?i)^classpath:[/\\\\]*", "") : null;
    }

    /**
     * Thymeleaf 缓存， 开发环境如果没有设置怎么默认就是 false
     */
    private static void resetThymeleafCacheIfDevMode() {
        if (AppContext.isDev()) {
            String thymeleafCacheKey = "spring.thymeleaf.cache";

            String thymeleafCacheValue = AppContext.getCustomAppProperty(thymeleafCacheKey, null);

            if (StringUtils.isBlank(thymeleafCacheValue)) {
                // 开发环境下，如果没有配置的话，默认关闭缓存
                System.setProperty(thymeleafCacheKey, "false");
            }

        }
    }

    /**
     * Thymeleaf 模版处理的文件内容格式，HTML， XML， TEXT， JAVASCRIPT， CSS， RAW，thymeleaf3.0 版本使用HTML即可
     * 使用本模版，默认 HTML5 是过时的， 更改为默认的 HTML
     */
    private static void resetThymeleafModeIfDevMode() {
        if (AppContext.isDev()) {
            String thymeleafModeKey = "spring.thymeleaf.mode";
            String deprecatedModeValue = "HTML5";

            String thymeleafModeValue = AppContext.getCustomAppProperty(thymeleafModeKey, null);

            if (StringUtils.isBlank(thymeleafModeValue) || deprecatedModeValue.equals(thymeleafModeValue.trim())) {
                // 开发环境下，如果没有配置的或者配置的过时了就设置为 HTML
                logInitMessage("默认[" + thymeleafModeKey + "]配置=" + thymeleafModeValue + ", 更正为： HTML");
                System.setProperty(thymeleafModeKey, "HTML");
            }

        }
    }

    /**
     * 如果是开发环境的话，重置 thymeleaf 路径
     */
    private static void resetThymeleafTemplatePrefixIfDevMode() throws MalformedURLException {
        if (AppContext.isDev()) {

            String thymeleafPrefixKey = "spring.thymeleaf.prefix";

            String thymeleafTemplatePrefix = AppContext.getCustomAppProperty(thymeleafPrefixKey, null);
            String thymeleafTemplatePathPrefix = getThymeleafTemplatePrefix(thymeleafTemplatePrefix, AppContext.getAppRootResourceDir(), true);

            if (StringUtils.isBlank(thymeleafTemplatePathPrefix)) {
                thymeleafTemplatePathPrefix = getThymeleafTemplatePrefix(thymeleafTemplatePrefix, AppContext.getAppRootTestResourceDir(), true);
            }

            if (StringUtils.isBlank(thymeleafTemplatePathPrefix)) {
                thymeleafTemplatePathPrefix = getThymeleafTemplatePrefix(thymeleafTemplatePrefix, AppContext.getAppRootResourceDir(), false);
            }

            if (null != thymeleafTemplatePathPrefix) {
                System.setProperty(thymeleafPrefixKey, thymeleafTemplatePathPrefix);

                logInitMessage("开发环境，设置Thymeleaf模版路径： " + thymeleafPrefixKey + "=" + thymeleafTemplatePathPrefix + ", 原始配置： " + thymeleafTemplatePrefix);
            }
        }
    }

    private static String getThymeleafTemplatePrefix(String thymeleafTemplatePrefix, String locationPrefix, boolean existsCheck) throws MalformedURLException {
        String pathPrefix = null;

        if (isClasspath(thymeleafTemplatePrefix)) {
            String noClasspathPath = trimClasspath(thymeleafTemplatePrefix);
            pathPrefix = locationPrefix + noClasspathPath;
        } else {
            pathPrefix = locationPrefix + "templates/";
        }

        pathPrefix = appendSystemUrlFilePrefix(PathUtil.normalizePath(pathPrefix));

        if (!existsCheck) {
            return pathPrefix;
        }

        if (isUrlResourceExists(pathPrefix)) {
            return pathPrefix;
        }
        return null;
    }

    private static boolean isUrlResourceExists(String urlResourcePath) throws MalformedURLException {
        return new UrlResource(urlResourcePath).exists();
    }

    private static final String FILE_URL_PREFIX = "file:///";
    private static final String FILE_URL_PREFIX_REGEX = "(?i)^file:///.*";

    public static String appendSystemUrlFilePrefix(String path) {
        if (path.matches(FILE_URL_PREFIX_REGEX)) {
            return path;
        }
        path = path.replaceFirst("^[/\\\\]", "");
        return FILE_URL_PREFIX + path;
    }

    /**
     * 如果是开发环境的话，重置静态资源路径
     */
    private static void resetStaticResourceLocationsIfDevMode() {
        if (AppContext.isDev()) {
            String resourceLocationPrefix = AppContext.getAppRootResourceDir();

            resourceLocationPrefix = appendSystemUrlFilePrefix(resourceLocationPrefix);

            String locationKeyPrefix = "spring.resources.static-locations";
            Set<String> locationKeys = AppContext.lookupCustomKeys(new PrefixKeyFilter(locationKeyPrefix));

            if (locationKeys == null || locationKeys.isEmpty()) {
                resetStaticResourceLocationsAsDefault(resourceLocationPrefix, locationKeyPrefix);
            } else {
                for (String locationKey : locationKeys) {
                    String value = AppContext.getCustomAppProperty(locationKey, null);
                    if (StringUtils.isBlank(value)) {
                        continue;
                    }
                    String newValue = resetLocationForDevEnv(resourceLocationPrefix, locationKeyPrefix, locationKey, value);
                    // 重置值
                    System.setProperty(locationKey, newValue);

                    logInitMessage("开发环境，设置静态资源路径： " + locationKey + "=" + newValue + ", 原始配置： " + value);
                }
            }
        }
    }

    private static String resetLocationForDevEnv(String locationPrefix, String locationKeyPrefix, String locationKey, String value) {
        if (AppContext.isDev()) {
            String newValue = value;
            if (locationKey.equals(locationKeyPrefix)) {
                newValue = resetMultiPathLocationsForDevEnv(locationPrefix, value);
            } else {
                if (isClasspath(value)) {
                    newValue = appendLocationPrefixForClasspath(locationPrefix, value);
                }
            }
            return newValue;
        }
        return value;
    }

    private static String resetMultiPathLocationsForDevEnv(String locationPrefix, String value) {
        if (AppContext.isDev()) {
            String newValue;
            String[] locationArr = value.split(",");
            StringBuilder locationBuilder = new StringBuilder();
            for (String location : locationArr) {
                String replaceLocation = resetSingleLocationForDevEnv(locationPrefix, location);
                locationBuilder.append(replaceLocation).append(",");
            }
            locationBuilder.setLength(locationBuilder.length() - 1);
            newValue = locationBuilder.toString();
            return newValue;
        }
        return value;
    }

    private static String resetSingleLocationForDevEnv(String locationPrefix, String location) {
        if (AppContext.isDev()) {
            String replaceLocation = location;
            if (isClasspath(location)) {
                replaceLocation = appendLocationPrefixForClasspath(locationPrefix, location);
            } else {
                replaceLocation = appendSystemUrlFilePrefix(replaceLocation);
            }
            return replaceLocation;
        }
        return location;
    }

    protected static String appendLocationPrefixForClasspath(String locationPrefix, String location) {
        return locationPrefix + PathUtil.normalizePath(location.replaceAll("(?i)classpath:[/\\\\]*", ""));
    }

    private static void logInitMessage(String message) {
        initMessageList.add(message);
    }

    private static void resetStaticResourceLocationsAsDefault(String locationPrefix, String locationKeyPrefix) {
        logInitMessage("开发环境，设置静态资源路径： " + locationKeyPrefix + "[0] = /");
        logInitMessage("开发环境，设置静态资源路径： " + locationKeyPrefix + "[1] = " + locationPrefix + "META-INF/resources/");
        logInitMessage("开发环境，设置静态资源路径： " + locationKeyPrefix + "[2] = " + locationPrefix + "resources/");
        logInitMessage("开发环境，设置静态资源路径： " + locationKeyPrefix + "[3] = " + locationPrefix + "static/");
        logInitMessage("开发环境，设置静态资源路径： " + locationKeyPrefix + "[4] = " + locationPrefix + "public/");

        System.setProperty(locationKeyPrefix + "[0]", "/");
        System.setProperty(locationKeyPrefix + "[1]", locationPrefix + "META-INF/resources/");
        System.setProperty(locationKeyPrefix + "[2]", locationPrefix + "resources/");
        System.setProperty(locationKeyPrefix + "[3]", locationPrefix + "static/");
        System.setProperty(locationKeyPrefix + "[4]", locationPrefix + "public/");
    }

    /**
     * WebMVC adapter
     **/

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {

    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {

    }

    @Override
    public void addFormatters(FormatterRegistry registry) {

    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {

    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {

    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {

    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {

    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
}
