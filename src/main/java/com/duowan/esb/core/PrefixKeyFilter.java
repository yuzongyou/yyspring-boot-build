package com.duowan.esb.core;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Arvin
 */
public class PrefixKeyFilter implements KeyFilter {

    private final String PREFIX;

    public PrefixKeyFilter(String prefix) {
        PREFIX = prefix;
    }

    @Override
    public boolean filter(String key) {
        if (StringUtils.isBlank(PREFIX)) {
            return true;
        }
        return !StringUtils.isBlank(key) && key.startsWith(PREFIX);
    }
}
