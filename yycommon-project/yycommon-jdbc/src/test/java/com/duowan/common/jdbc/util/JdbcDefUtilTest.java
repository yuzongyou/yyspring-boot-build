package com.duowan.common.jdbc.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Arvin
 */
public class JdbcDefUtilTest {

    @Test
    public void testRemoveJdbcExtendConfigs() throws Exception {
        Map<String, String> configMap = new HashMap<>();
        configMap.put("jdbc.extend.test.enabled.ids", "xxxx");
        configMap.put("jdbc.extend.test.primary.id", "yyyy");
        configMap.put("jdbc.extend.test.primary", "aaaaa");

        JdbcDefUtil.removeJdbcExtendConfigs(configMap, "enabled.ids", "primary.id");

        assertFalse(configMap.containsKey("jdbc.extend.test.enabled.ids"));
        assertFalse(configMap.containsKey("jdbc.extend.test.primary.id"));
        assertTrue(configMap.containsKey("jdbc.extend.test.primary"));


    }
}