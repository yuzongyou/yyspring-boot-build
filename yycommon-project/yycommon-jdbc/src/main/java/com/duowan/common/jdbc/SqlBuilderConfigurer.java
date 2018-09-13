package com.duowan.common.jdbc;

/**
 * @author Arvin
 */
public interface SqlBuilderConfigurer<T> {

    /**
     * 使用之前进行自定义配置
     *
     * @param sqlBuilder sqlBuilder
     * @return 返回配置对象本身
     */
    T configure(T sqlBuilder);
}
