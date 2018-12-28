package com.duowan.yyspring.boot;

import com.duowan.common.utils.StringUtil;

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
        if (StringUtil.isBlank(regex)) {
            return true;
        }
        return !StringUtil.isBlank(key) && key.matches(regex);
    }
}
