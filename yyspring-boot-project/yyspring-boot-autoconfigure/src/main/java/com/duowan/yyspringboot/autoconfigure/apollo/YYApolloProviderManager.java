package com.duowan.yyspringboot.autoconfigure.apollo;

import com.ctrip.framework.foundation.internals.DefaultProviderManager;
import com.ctrip.framework.foundation.internals.provider.DefaultNetworkProvider;
import com.ctrip.framework.foundation.internals.provider.DefaultServerProvider;
import com.ctrip.framework.foundation.spi.provider.Provider;

/**
 * 自定义 Provider Manager
 *
 * @author dw_xiajiqiu1
 */
public class YYApolloProviderManager extends DefaultProviderManager {

    public YYApolloProviderManager() {
        // 读取 Esb 规范的应用程序配置
        Provider applicationProvider = new YYApolloApplicationProvider();
        applicationProvider.initialize();
        register(applicationProvider);

        // Load network parameters
        Provider networkProvider = new DefaultNetworkProvider();
        networkProvider.initialize();
        register(networkProvider);

        // Load environment (fat, fws, uat, prod ...) and dc, from /opt/settings/server.properties, JVM property and/or OS
        // environment variables.
        Provider serverProvider = new DefaultServerProvider();
        serverProvider.initialize();
        register(serverProvider);
    }

}
