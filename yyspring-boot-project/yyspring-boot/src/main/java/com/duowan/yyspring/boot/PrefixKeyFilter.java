package com.duowan.yyspring.boot;

import org.apache.commons.lang3.StringUtils;

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
        if (StringUtils.isBlank(prefix)) {
            return true;
        }
        return !StringUtils.isBlank(key) && key.startsWith(prefix);
    }
}
