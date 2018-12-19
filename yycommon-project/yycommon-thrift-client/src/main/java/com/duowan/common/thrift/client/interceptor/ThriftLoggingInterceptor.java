package com.duowan.common.thrift.client.interceptor;

import com.duowan.common.thrift.client.util.ThriftUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/18 17:51
 */
public class ThriftLoggingInterceptor implements ThriftInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThriftLoggingInterceptor.class);

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object before(ThriftInvokeContext invokeContext) {
        String argString = ThriftUtil.argsToString(invokeContext.getArgs());
        LOGGER.info("{} method={}, args={}, Transport={}, client={}",
                invokeContext.getTraceId(), invokeContext.getMethod().getName(), argString, invokeContext.getPooledTransport(), invokeContext.getClient());
        return null;
    }

    @Override
    public void afterReturning(Object returnValue, ThriftInvokeContext invokeContext) {

        String argString = ThriftUtil.argsToString(invokeContext.getArgs());

        LOGGER.info("{} method={}, args={}, Transport={}, client={}, return={}",
                invokeContext.getTraceId(), invokeContext.getMethod().getName(), argString, invokeContext.getPooledTransport(), invokeContext.getClient(), returnValue);
    }

    @Override
    public void afterThrowing(Exception e, ThriftInvokeContext invokeContext) {
        String remark = null;
        if (null != invokeContext.getPooledTransport()) {
            remark = invokeContext.getPooledTransport().getRemark();
        }
        String logInfo = invokeContext.getTraceId() + " remark=[" + remark + "], node =[" + invokeContext.getServerNode() + "], error = " + e.getMessage();
        LOGGER.warn(logInfo, e);
    }

}
