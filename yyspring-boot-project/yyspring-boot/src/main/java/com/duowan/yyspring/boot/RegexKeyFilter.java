package com.duowan.yyspring.boot;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Arvin
 */
public class RegexKeyFilter implements KeyFilter {

    private final String regex;

    public RegexKeyFilter(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean filter(String key) {
        if (StringUtils.isBlank(regex)) {
            return true;
        }
        return !StringUtils.isBlank(key) && key.matches(regex);
    }
}
