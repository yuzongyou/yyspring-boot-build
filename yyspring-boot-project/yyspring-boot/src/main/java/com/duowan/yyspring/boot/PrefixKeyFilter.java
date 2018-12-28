package com.duowan.yyspring.boot;

import com.duowan.common.utils.StringUtil;

/**
 * @author Arvin
 */
public class PrefixKeyFilter implements KeyFilter {

    private final String prefix;

    public PrefixKeyFilter(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean filter(String key) {
        if (StringUtil.isBlank(prefix)) {
            return true;
        }
        return !StringUtil.isBlank(key) && key.startsWith(prefix);
    }
}
