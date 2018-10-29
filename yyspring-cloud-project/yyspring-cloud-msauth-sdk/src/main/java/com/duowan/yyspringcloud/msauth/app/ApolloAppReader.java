package com.duowan.yyspringcloud.msauth.app;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.duowan.yyspringcloud.msauth.Constants;
import com.duowan.yyspringcloud.msauth.exception.EmptyApolloNamespaceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 格式：
 * # 默认的配置
 * default.secret=xxx
 * <p>
 * # 前缀是secret.然后带上 appid，最后跟secretKey
 * secret.[appid]=xxx
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 9:41
 */
public class ApolloAppReader implements AppReader, Ordered {

    /**
     * 配置列表
     **/
    private List<Config> configList;

    public ApolloAppReader(List<String> namespaces) {
        /**
         * public namespaces
         **/
        this.configList = initConfigList(filterAndCheckNamespaces(namespaces));
    }

    public ApolloAppReader(String... namespaces) {
        /**
         * public namespaces
         **/
        this.configList = initConfigList(filterAndCheckNamespaces(Arrays.asList(namespaces)));
    }

    private List<Config> initConfigList(List<String> namespaces) {
        List<Config> configList = new ArrayList<>(namespaces.size());
        namespaces.forEach(namespace -> configList.add(ConfigService.getConfig(namespace)));
        return configList;

    }

    private List<String> filterAndCheckNamespaces(List<String> namespaces) {

        if (namespaces == null || namespaces.isEmpty()) {
            throw new EmptyApolloNamespaceException();
        }

        List<String> result = new ArrayList<>();
        for (String namespace : namespaces) {
            if (StringUtils.isNotBlank(namespace) && !result.contains(namespace)) {
                result.add(namespace);
            }
        }
        if (result.isEmpty()) {
            throw new EmptyApolloNamespaceException();
        }

        return result;
    }


    @Override
    public App read(String appId) {

        for (Config config : configList) {
            App app = lookupAppByConfig(appId, config);
            if (null != app) {
                return app;
            }
        }
        return null;
    }

    private App lookupAppByConfig(String appId, Config config) {

        String secretKey = Constants.SECRET_KEY_PREFIX + appId;
        String secret = config.getProperty(secretKey, null);
        if (StringUtils.isBlank(secret)) {
            secret = config.getProperty(Constants.DEFAULT_SECRET_KEY, null);
        }
        if (StringUtils.isBlank(secret)) {
            return null;
        }

        return new App(appId, secret);
    }

    @Override
    public int getOrder() {
        return Constants.ORDER_APOLLO_APP_READER;
    }
}
