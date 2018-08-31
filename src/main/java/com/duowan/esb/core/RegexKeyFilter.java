package com.duowan.esb.core;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Arvin
 */
public class RegexKeyFilter implements KeyFilter {

    private final String REGEX;

    public RegexKeyFilter(String regex) {
        REGEX = regex;
    }

    @Override
    public boolean filter(String key) {
        if (StringUtils.isBlank(REGEX)) {
            return true;
        }
        return !StringUtils.isBlank(key) && key.matches(REGEX);
    }
}
