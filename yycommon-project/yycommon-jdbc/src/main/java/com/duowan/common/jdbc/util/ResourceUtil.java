package com.duowan.common.jdbc.util;

import com.duowan.common.exception.CodeException;
import com.duowan.common.utils.ConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Arvin
 */
public abstract class ResourceUtil {

    public static Resource getClasspathResource(String classpathResource, ClassLoader classLoader) {

        classLoader = classLoader == null ? Thread.currentThread().getContextClassLoader() : classLoader;

        String path = classpathResource.replaceFirst("(?i)^classpath\\*?:", "");
        Resource resource = new ClassPathResource(path, classLoader);
        if (!resource.exists()) {
            return null;
        }
        return resource;
    }

    public static Resource getResourceFromAbsPath(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                Resource resource = new InputStreamResource(new FileInputStream(file));
                if (resource.exists()) {
                    return resource;
                }
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    /**
     * 搜索资源文件， 自动判断从 classpath 还是按照绝对路径搜索
     *
     * @param resourcePath 资源文件路径
     * @param classLoader  加载器
     * @return 如果存在就返回Resource，否则返回null
     */
    public static Resource lookupResource(String resourcePath, ClassLoader classLoader) {

        if (StringUtils.isBlank(resourcePath)) {
            return null;
        }

        resourcePath = resourcePath.replaceFirst("(?i)^classpath\\*?:", "").trim();

        Resource resource = getClasspathResource(resourcePath, classLoader);

        if (null == resource) {
            resource = getResourceFromAbsPath(resourcePath);
        }

        return resource;
    }

    /**
     * 将一个或多个资源文件反转来读取为 key - value MAP
     *
     * @param resourceList 资源文件列表
     * @return 始终返回非null
     */
    public static Map<String, String> readResourcesAsMapByReverse(List<Resource> resourceList) {

        Map<String, String> propMap = new HashMap<>();

        if (null == resourceList || resourceList.isEmpty()) {
            return propMap;
        }

        int size = resourceList.size();
        for (int i = size - 1; i > -1; --i) {
            Resource resource = resourceList.get(i);
            Map<String, String> subMap = readResourceAsMap(resource);
            if (null != subMap && !subMap.isEmpty()) {
                propMap.putAll(subMap);
            }
        }

        return propMap;
    }

    /**
     * 将一个或多个资源文件读取为 key - value MAP
     *
     * @param resourceList 资源文件列表
     * @return 始终返回非null
     */
    public static Map<String, String> readResourcesAsMap(List<Resource> resourceList) {

        Map<String, String> propMap = new HashMap<>();

        if (null == resourceList || resourceList.isEmpty()) {
            return propMap;
        }

        for (Resource resource : resourceList) {
            Map<String, String> subMap = readResourceAsMap(resource);
            if (null != subMap && !subMap.isEmpty()) {
                propMap.putAll(subMap);
            }
        }

        return propMap;
    }

    public static Map<String, String> readResourceAsMap(Resource resource) {
        Map<String, String> propMap = new HashMap<>();
        if (null != resource && resource.exists()) {
            try {

                Properties properties = new Properties();
                properties.load(resource.getInputStream());

                if (!properties.isEmpty()) {

                    for (Map.Entry<Object, Object> entry : properties.entrySet()) {

                        String key = ConvertUtil.toString(entry.getKey());
                        String value = ConvertUtil.toString(entry.getValue(), "");

                        if (StringUtils.isNotBlank(key)) {
                            propMap.put(key, value);
                        }

                    }

                }
            } catch (Exception e) {
                throw new CodeException("读取资源文件错误：" + e.getMessage(), e);
            }
        }
        return propMap;
    }
}
