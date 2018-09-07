package com.duowan.yyspring.boot;

import org.springframework.core.env.StandardEnvironment;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/1 19:09
 */
public class DefaultEnvReader implements EnvReader {
    @Override
    public String readRuntimeEnv(StandardEnvironment environment, Class<?> sourceClass) {
        return AppContext.lookupFirstNotBlankValue(environment, new String[]{"DWENV", "ENV"}, AppContext.ENV_DEV);
    }
}
