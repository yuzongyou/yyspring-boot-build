package com.duowan.yyspring.boot;

/**
 * @author Arvin
 */
public interface KeyFilter {

    /**
     * 是否过来指定的key
     *
     * @param key 要检查的key
     * @return 需要则返回 true， 不需要则返回 false
     */
    boolean filter(String key);
}
