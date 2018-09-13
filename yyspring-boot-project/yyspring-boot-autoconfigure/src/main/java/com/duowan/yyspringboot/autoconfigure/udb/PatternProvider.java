package com.duowan.yyspringboot.autoconfigure.udb;

import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 15:56
 */
public interface PatternProvider {

    /**
     * 获取要忽略的 Pattern
     * @return 返回要忽略的列表
     */
    Set<String> getExcludePatterns();

    /**
     * 要拦截的匹配路径
     * @return 返回要包含的列表
     */
    Set<String> getIncludePatterns();
}
