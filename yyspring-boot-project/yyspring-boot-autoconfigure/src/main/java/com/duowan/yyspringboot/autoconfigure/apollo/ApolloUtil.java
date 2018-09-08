package com.duowan.yyspringboot.autoconfigure.apollo;

import org.apache.commons.lang3.StringUtils;

/**
 * @author dw_xiajiqiu1
 */
public class ApolloUtil {

    public static final String KEY_ORIGIN_APPID = "app.id";

    public static final String APOLLO_KEY_PREFIX = "yyspring.apollo.";

    public static boolean isApolloKey(String key) {
        return StringUtils.isNotBlank(key) && key.startsWith(APOLLO_KEY_PREFIX);
    }

    public static String wrapAsApolloKey(String key) {
        if (key.startsWith(APOLLO_KEY_PREFIX)) {
            return key;
        }
        return APOLLO_KEY_PREFIX + key;
    }
}
