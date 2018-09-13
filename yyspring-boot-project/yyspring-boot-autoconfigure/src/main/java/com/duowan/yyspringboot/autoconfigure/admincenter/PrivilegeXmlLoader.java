package com.duowan.yyspringboot.autoconfigure.admincenter;

import com.duowan.common.utils.AssertUtil;
import com.duowan.common.utils.HttpUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 16:16
 */
public class PrivilegeXmlLoader {


    public static String loadPrivilegeXml(String privilegeXmlPath) throws IOException {
        AssertUtil.assertNotBlank(privilegeXmlPath, "必须提供Admincenter的权限文件(privilege.xml)路径!");

        Resource resource = null;
        try {
            resource = getClasspathResource(privilegeXmlPath);
            if (null != resource) {
                return readInputStreamAsString(resource.getInputStream(), "UTF-8");
            }
        } catch (IOException ignored) {
        }

        try {
            resource = getResourceFromAbsPath(privilegeXmlPath);
            if (null != resource) {
                return readInputStreamAsString(resource.getInputStream(), "UTF-8");
            }
        } catch (IOException ignored) {
        }

        return HttpUtil.doGet(privilegeXmlPath, 10000, 10000);
    }

    public static Resource getClasspathResource(String classpathResource) {

        String path = classpathResource.replaceFirst("(?i)classpath\\*?:", "");
        Resource resource = new ClassPathResource(path);
        if (!resource.exists()) {
            return null;
        }
        return resource;
    }

    public static Resource getResourceFromAbsPath(String filePath) {
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

    private static String readInputStreamAsString(InputStream is, String encoding) throws IOException {
        if (null == is) {
            return null;
        }
        if (null == encoding || "".equals(encoding.trim())) {
            encoding = "UTF-8";
        }
        StringBuilder builder = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = is.read(b)) != -1; ) {
            builder.append(new String(b, 0, n, encoding));
        }
        return builder.toString();
    }
}
