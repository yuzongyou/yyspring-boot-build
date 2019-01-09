package com.duowan.mybatis.dao.mybatis.plugins;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/9 16:39
 */
public class SampleInterceptor implements Interceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleInterceptor.class);

    private final String id;

    public SampleInterceptor(String id) {
        this.id = id;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        LOGGER.info("Invocation(" + id + "): " + invocation);
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        LOGGER.info("Plugin(" + id + ") : " + target);
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        LOGGER.info("SetProperties(" + id + "): " + properties);
    }
}
