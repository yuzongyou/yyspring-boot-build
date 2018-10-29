package com.duowan.yyspringcloud.apollo;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigService;
import com.duowan.common.utils.JsonUtil;
import org.junit.Test;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/18 8:57
 */
public class ApolloTest {

    @Test
    public void testGetCommonNamespace() {
        Config config = ConfigService.getConfig("bizSupportGroup.ms-rpc-auth");
        System.out.println(JsonUtil.toJson(config.getPropertyNames()));

        for (String key : config.getPropertyNames()) {
            System.out.println(key + " = " + config.getProperty(key, ""));
        }
    }
}
