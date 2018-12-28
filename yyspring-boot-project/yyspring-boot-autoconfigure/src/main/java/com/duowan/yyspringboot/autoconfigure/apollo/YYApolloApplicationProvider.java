package com.duowan.yyspringboot.autoconfigure.apollo;

import com.ctrip.framework.apollo.core.MetaDomainConsts;
import com.ctrip.framework.apollo.core.enums.Env;
import com.ctrip.framework.foundation.internals.Utils;
import com.ctrip.framework.foundation.spi.provider.ApplicationProvider;
import com.ctrip.framework.foundation.spi.provider.Provider;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.ConvertUtil;
import com.duowan.common.utils.StringUtil;
import com.duowan.yyspring.boot.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 自定义实现 Apollo Application Provider
 *
 * @author dw_xiajiqiu1
 */
public class YYApolloApplicationProvider implements ApplicationProvider {

    private static final Logger logger = LoggerFactory.getLogger(YYApolloApplicationProvider.class);

    /** 识别项目代号作为 APPID */
    private static String APPID;

    private static Map<String, String> APOLLO_PROPERTIES = new HashMap<>();

    static {
        String apolloAppIdKey = ApolloUtil.wrapAsApolloKey(ApolloUtil.KEY_ORIGIN_APPID);
        // 优先读取 "apollo.app.id"
        APPID = ConvertUtil.toString(AppContext.getAppProperty(apolloAppIdKey, AppContext.getProjectNo()));

        Set<String> apolloKeys = AppContext.lookupKeys(AppContext.getAppKeySet(), ApolloUtil::isApolloKey);

        if (null != apolloKeys && !apolloKeys.isEmpty()) {
            for (String key : apolloKeys) {
                APOLLO_PROPERTIES.put(key, AppContext.getAppProperty(key, null));
            }
        }

        // 初始化 MetaDomainConsts
        initMetaDomainConsts();
    }

    /**
     * 允许在 esb 环境的配置文件中配置 配置中心的路径
     */
    private static void initMetaDomainConsts() {
        try {
            Class<?> clazz = MetaDomainConsts.class;

            Field field = clazz.getDeclaredField("domains");
            field.setAccessible(true);

            Object obj = field.get(clazz);

            Map<Env, Object> domainMap = (Map<Env, Object>) obj;

            // 使用 Esb 上下文环境配置进行覆盖
            overrideByEsbApplicationConfig(domainMap);

        } catch (Exception e) {
            throw new RuntimeException("Esb环境下初始化 Apollo MetaDomainConsts 失败： " + e.getMessage(), e);
        }

    }

    private static void overrideByEsbApplicationConfig(Map<Env, Object> domainMap) {

        String currentEnv = AppContext.getEnv();
        // 当前环境的 meta
        Env env = Env.fromString(currentEnv);

        String value = AppContext.getAppProperty(ApolloUtil.wrapAsApolloKey("meta"), null);
        if (StringUtil.isNotBlank(value)) {
            domainMap.put(env, value);
        }

        for (Env env1 : Env.values()) {
            if (!env.equals(env1)) {
                String key = ApolloUtil.wrapAsApolloKey(env.name().toLowerCase() + ".meta");
                String val = AppContext.getAppProperty(key, null);
                if (StringUtil.isNotBlank(val)) {
                    domainMap.put(env1, val);
                }
            }
        }

        logger.info("Apollo MetaDomainConsts: " + CommonUtil.mapToPrettyJson(domainMap));
    }

    @Override
    public String getAppId() {
        return APPID;
    }

    @Override
    public boolean isAppIdSet() {
        return !Utils.isBlank(APPID);
    }

    @Override
    public void initialize(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public Class<? extends Provider> getType() {
        return ApplicationProvider.class;
    }

    @Override
    public String getProperty(String name, String defaultValue) {
        if (ApolloUtil.KEY_ORIGIN_APPID.equalsIgnoreCase(name)) {
            return ConvertUtil.toString(APPID, defaultValue);
        }
        // 适配 key， 没有 apollo 前缀的就加上apollo前缀
        String appKey = ApolloUtil.wrapAsApolloKey(name);
        return ConvertUtil.toString(APOLLO_PROPERTIES.get(appKey), defaultValue);
    }

    @Override
    public void initialize() {

    }

    @Override
    public String toString() {
        return "appId [" + getAppId() + "] properties: " + APOLLO_PROPERTIES + " (YYApolloApplicationProvider)";
    }
}
