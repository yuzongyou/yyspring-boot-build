package com.duowan.yyspringboot.autoconfigure.apollo;

import com.duowan.common.utils.StringUtil;

/**
 * @author dw_xiajiqiu1
 */
public class ApolloUtil {

    public static final String KEY_ORIGIN_APPID = "app.id";

    public static final String APOLLO_KEY_PREFIX = "yyspring.apollo.";

    public static boolean isApolloKey(String key) {
        return StringUtil.isNotBlank(key) && key.startsWith(APOLLO_KEY_PREFIX);
    }

    public static String wrapAsApolloKey(String key) {
        if (key.startsWith(APOLLO_KEY_PREFIX)) {
            return key;
        }
        return APOLLO_KEY_PREFIX + key;
    }
}
