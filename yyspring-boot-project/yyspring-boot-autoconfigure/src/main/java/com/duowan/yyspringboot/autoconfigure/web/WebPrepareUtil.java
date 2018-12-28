package com.duowan.yyspringboot.autoconfigure.web;

import com.duowan.common.utils.ClassUtil;
import com.duowan.common.utils.PathUtil;
import com.duowan.common.utils.StringUtil;
import com.duowan.yyspring.boot.AppContext;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/1 16:40
 */
public class WebPrepareUtil {

    private WebPrepareUtil() {
        throw new IllegalStateException("Utility Class");
    }

    private static List<String> initInfo = new ArrayList<>();

    private static final String FILE_URL_PREFIX = "file:///";
    private static final String FILE_URL_PREFIX_REGEX = "(?i)^file:///.*";
    private static final String LOG_STATIC_RES_PREFIX = "开发环境，设置静态资源路径： ";

    public static String appendSystemUrlFilePrefix(String path) {
        if (path.matches(FILE_URL_PREFIX_REGEX)) {
            return path;
        }
        path = path.replaceFirst("^[/\\\\]", "");
        return FILE_URL_PREFIX + path;
    }

    public static List<String> getInitInfo() {
        return initInfo;
    }

    /**
     * 如果是开发环境的话，重置静态资源路径
     *
     * @param appEnvironment 环境熟悉
     * @param moduleDir      模块所在目录
     */
    @SuppressWarnings("unchecked")
    public static void prepareStaticResourceLocations(StandardEnvironment appEnvironment, String moduleDir) {
        String resourceLocationPrefix = PathUtil.normalizePath(moduleDir + "/src/main/resources/");

        resourceLocationPrefix = appendSystemUrlFilePrefix(resourceLocationPrefix);

        String locationKeyPrefix = "spring.resources.static-locations";

        Binder binder = Binder.get(appEnvironment);
        Map<String, Object> map = null;
        BindResult<Map> result = binder.bind(locationKeyPrefix, Map.class);
        if (result.isBound()) {
            map = result.get();
        }

        if (map == null || map.isEmpty()) {
            resetStaticResourceLocationsAsDefault(resourceLocationPrefix, locationKeyPrefix);
        } else {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String value = String.valueOf(entry.getValue());
                String locationKey = locationKeyPrefix + entry.getKey();
                if (StringUtil.isBlank(value)) {
                    continue;
                }
                String newValue = resetLocationForDevEnv(resourceLocationPrefix, locationKeyPrefix, locationKey, value);
                // 重置值
                System.setProperty(locationKey, newValue);
                initInfo.add(LOG_STATIC_RES_PREFIX + locationKey + "=" + newValue + ", 原始配置： " + value);
            }
        }
    }

    private static String resetLocationForDevEnv(String locationPrefix, String locationKeyPrefix, String locationKey, String value) {
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

    private static String resetMultiPathLocationsForDevEnv(String locationPrefix, String value) {
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

    private static String resetSingleLocationForDevEnv(String locationPrefix, String location) {
        String replaceLocation = location;
        if (isClasspath(location)) {
            replaceLocation = appendLocationPrefixForClasspath(locationPrefix, location);
        } else {
            replaceLocation = appendSystemUrlFilePrefix(replaceLocation);
        }
        return replaceLocation;
    }

    protected static String appendLocationPrefixForClasspath(String locationPrefix, String location) {
        return locationPrefix + PathUtil.normalizePath(location.replaceAll("(?i)classpath:[/\\\\]*", ""));
    }

    public static boolean isClasspath(String path) {
        return null != path && path.trim().matches("(?i)^classpath:.*");
    }

    public static String trimClasspath(String path) {
        return isClasspath(path) ? path.replaceFirst("(?i)^classpath:[/\\\\]*", "") : null;
    }

    private static void resetStaticResourceLocationsAsDefault(String locationPrefix, String locationKeyPrefix) {
        initInfo.add(LOG_STATIC_RES_PREFIX + locationKeyPrefix + "[0] = /");
        initInfo.add(LOG_STATIC_RES_PREFIX + locationKeyPrefix + "[1] = " + locationPrefix + "META-INF/resources/");
        initInfo.add(LOG_STATIC_RES_PREFIX + locationKeyPrefix + "[2] = " + locationPrefix + "resources/");
        initInfo.add(LOG_STATIC_RES_PREFIX + locationKeyPrefix + "[3] = " + locationPrefix + "static/");
        initInfo.add(LOG_STATIC_RES_PREFIX + locationKeyPrefix + "[4] = " + locationPrefix + "public/");

        System.setProperty(locationKeyPrefix + "[0]", "/");
        System.setProperty(locationKeyPrefix + "[1]", locationPrefix + "META-INF/resources/");
        System.setProperty(locationKeyPrefix + "[2]", locationPrefix + "resources/");
        System.setProperty(locationKeyPrefix + "[3]", locationPrefix + "static/");
        System.setProperty(locationKeyPrefix + "[4]", locationPrefix + "public/");
    }

    public static void prepareThymeleafDevConfig(String moduleDir) {
        if (isThymeleafImported()) {

            // 如果是开发环境的话，重置 thymeleaf 路径
            resetThymeleafTemplatePrefix(moduleDir);

            // Thymeleaf 缓存， 开发环境如果没有设置怎么默认就是 false
            resetThymeleafCache();

            // Thymeleaf 模版模式， 3.0 默认设置为 HTML
            resetThymeleafMode();
        }
    }

    /**
     * Thymeleaf 模版处理的文件内容格式，HTML， XML， TEXT， JAVASCRIPT， CSS， RAW，thymeleaf3.0 版本使用HTML即可
     * 使用本模版，默认 HTML5 是过时的， 更改为默认的 HTML
     */
    private static void resetThymeleafMode() {
        String thymeleafModeKey = "spring.thymeleaf.mode";
        String deprecatedModeValue = "HTML5";

        String thymeleafModeValue = AppContext.getAppProperty(thymeleafModeKey, null);

        if (StringUtil.isBlank(thymeleafModeValue) || deprecatedModeValue.equals(thymeleafModeValue.trim())) {
            // 开发环境下，如果没有配置的或者配置的过时了就设置为 HTML
            initInfo.add("默认[" + thymeleafModeKey + "]配置=" + thymeleafModeValue + ", 更正为： HTML");
            System.setProperty(thymeleafModeKey, "HTML");
        }
    }

    /**
     * Thymeleaf 缓存， 开发环境如果没有设置怎么默认就是 false
     */
    private static void resetThymeleafCache() {
        String thymeleafCacheKey = "spring.thymeleaf.cache";

        String thymeleafCacheValue = AppContext.getAppProperty(thymeleafCacheKey, null);

        if (StringUtil.isBlank(thymeleafCacheValue)) {
            // 开发环境下，如果没有配置的话，默认关闭缓存
            System.setProperty(thymeleafCacheKey, "false");
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
     * 如果是开发环境的话，重置 thymeleaf 路径
     */
    private static void resetThymeleafTemplatePrefix(String moduleDir) {
        try {

            String resourceDir = PathUtil.normalizePath(moduleDir + "/src/main/resources/");
            String testResourceDir = PathUtil.normalizePath(moduleDir + "/src/test/resources/");
            String thymeleafPrefixKey = "spring.thymeleaf.prefix";

            String thymeleafTemplatePrefix = AppContext.getAppProperty(thymeleafPrefixKey, null);
            String thymeleafTemplatePathPrefix = getThymeleafTemplatePrefix(thymeleafTemplatePrefix, resourceDir, true);

            if (StringUtil.isBlank(thymeleafTemplatePathPrefix)) {
                thymeleafTemplatePathPrefix = getThymeleafTemplatePrefix(thymeleafTemplatePrefix, testResourceDir, true);
            }

            if (StringUtil.isBlank(thymeleafTemplatePathPrefix)) {
                thymeleafTemplatePathPrefix = getThymeleafTemplatePrefix(thymeleafTemplatePrefix, resourceDir, false);
            }

            if (null != thymeleafTemplatePathPrefix) {
                System.setProperty(thymeleafPrefixKey, thymeleafTemplatePathPrefix);

                initInfo.add("开发环境，设置Thymeleaf模版路径： " + thymeleafPrefixKey + "=" + thymeleafTemplatePathPrefix + ", 原始配置： " + thymeleafTemplatePrefix);
            }
        } catch (MalformedURLException e) {
            initInfo.add("resetThymeleafTemplatePrefix error: " + e.getMessage());
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
}
