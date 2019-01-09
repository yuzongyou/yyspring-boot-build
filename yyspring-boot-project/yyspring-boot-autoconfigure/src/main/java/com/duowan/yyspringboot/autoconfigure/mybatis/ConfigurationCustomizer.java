package com.duowan.yyspringboot.autoconfigure.mybatis;

import org.apache.ibatis.session.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/8 18:07
 */
public interface ConfigurationCustomizer {

    /**
     * Customize the given a {@link Configuration} object.
     * @param configuration the configuration object to customize
     */
    void customize(Configuration configuration);
}
