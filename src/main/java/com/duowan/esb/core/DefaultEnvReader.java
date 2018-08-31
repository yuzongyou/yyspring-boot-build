package com.duowan.esb.core;

import com.duowan.common.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 支持环境变量中修改默认的逻辑
 * 1. 直接 JVM 参数设置启动环境 -DENV=xxx
 * 2. 设置读取环境变量使用的KEY， -Denvkey=xxx
 * <p>
 * 3. 设置搜索配置文件的路径: -DresLookupPath=/data/config,/data/conf
 *
 * @author Arvin
 */
public class DefaultEnvReader implements EnvReader {

    /**
     * 可以在读取 环境变量，系统变量或者JVM变量中的 envkey 作为环境变量的值
     */
    private static final String ENV_KEY = "envkey";
    /**
     * 资源文件搜索路径
     */
    private static final String RES_LOOKUP_PATH_KEY = "resLookupPath";

    private static final String DEFAULT_ENV = "dev";

    protected String getVar(String key, String defaultValue) {
        String env = System.getenv(key);

        if (StringUtils.isBlank(env)) {
            return System.getProperty(key, defaultValue);
        }

        return env;
    }

    @Override
    public String readEnv() {

        // 如果设置了 ENV_KEY 则读取ENV_KEY设置的环境
        List<String> envKeys = Arrays.asList(
                getVar(ENV_KEY, "ENV"),
                "ENV",
                "DWENV"
        );

        for (String envKey : envKeys) {
            String env = getVar(envKey, null);
            if (StringUtils.isNotBlank(env)) {
                return env;
            }
        }

        return DEFAULT_ENV;
    }

    @Override
    public List<String> getResourceLookupPaths() {

        List<String> lookupPaths = new ArrayList<>();

        String resLookupPath = getVar(RES_LOOKUP_PATH_KEY, null);
        if (StringUtils.isNotBlank(resLookupPath)) {
            List<String> paths = CommonUtil.splitAsStringList(resLookupPath, ",");
            if (paths != null && !paths.isEmpty()) {
                lookupPaths.addAll(paths);
            }
        }

        if ("dev".equalsIgnoreCase(readEnv())) {
            return lookupPaths;
        }

        String projectNo = getVar("PROJECTNO", getVar("DWPROJECTNO", ""));

        if (StringUtils.isNotBlank(projectNo)) {
            lookupPaths.add("/data/app/" + projectNo + "/config/");
        }

        return lookupPaths;
    }
}
