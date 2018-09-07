package com.duowan.yyspring.boot;

import org.springframework.core.env.StandardEnvironment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/1 19:16
 */
public class DefaultProjectNoReader implements ProjectNoReader {
    @Override
    public String readProjectNo(StandardEnvironment environment, Class<?> sourceClass) {
        return AppContext.lookupFirstNotBlankValue(environment,
                new String[]{"DWPROJECTNO", "PROJECTNO", "APPNO", "DWAPPNO"},
                null);
    }
}
