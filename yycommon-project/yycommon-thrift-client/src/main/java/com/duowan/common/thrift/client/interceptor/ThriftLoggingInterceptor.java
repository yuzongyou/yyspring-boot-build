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

    private static final Logger logger = LoggerFactory.getLogger(ThriftLoggingInterceptor.class);

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object before(ThriftInvokeContext invokeContext) throws Exception {
        logger.info(invokeContext.getTraceId() +
                " method=" + invokeContext.getMethod().getName() +
                ", args=" + ThriftUtil.argsToString(invokeContext.getArgs()) +
                ", Transport=" + invokeContext.getPooledTransport() +
                ", client=" + invokeContext.getClient());
        return null;
    }

    @Override
    public void afterReturning(Object returnValue, ThriftInvokeContext invokeContext) throws Exception {
        logger.info(invokeContext.getTraceId() +
                " method=" + invokeContext.getMethod().getName() +
                ", args=" + ThriftUtil.argsToString(invokeContext.getArgs()) +
                ", Transport=" + invokeContext.getPooledTransport() +
                ", client=" + invokeContext.getClient() +
                ", return=" + returnValue);
    }

    @Override
    public void afterThrowing(Exception e, ThriftInvokeContext invokeContext) throws Exception {
        String remark = null;
        if (null != invokeContext.getPooledTransport()) {
            remark = invokeContext.getPooledTransport().getRemark();
        }
        logger.warn(invokeContext.getTraceId() + " remark=[" + remark + "], node =[" + invokeContext.getServerNode() + "], error = " + e.getMessage(), e);
    }

}
