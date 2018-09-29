package com.duowan.yyspringboot.autoconfigure.udbsecurity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * udb 登录校验必须忽略的
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 15:58
 */
public class UdbLoginRequirePatternProvider implements PatternProvider {
    @Override
    public Set<String> getExcludePatterns() {
        return new HashSet<>(Arrays.asList(
                "/udb/getSdkAuthReq4LayerClose.do",
                "/udb/callback.do",
                "/udb/logout.do"
        ));
    }

    @Override
    public Set<String> getIncludePatterns() {
        return null;
    }
}
