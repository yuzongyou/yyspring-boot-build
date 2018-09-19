package com.duowan.common.thrift.client.util;

import com.duowan.common.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 重试工具类
 *
 * @author Arvin
 */
public class RetryUtil {

    /**
     * 执行器
     *
     * @param <T> 返回值类型
     */
    public interface Executor<T> {

        /**
         * 执行具体的业务逻辑
         *
         * @param tryTime 当前尝试调用次数
         * @return 返回执行结果
         * @throws Exception 业务的任何异常
         */
        T execute(int tryTime) throws Exception;
    }

    private static final Logger logger = LoggerFactory.getLogger(RetryUtil.class);

    /**
     * 重试执行
     *
     * @param retryTimes     重试次数
     * @param intervalMillis 每次重试时间间隔
     * @param throwException 重试失败后是否抛出异常，不抛出异常的话，重试失败将返回 null
     * @param executor       具体业务执行器
     * @param <T>            结果类型
     * @return 返回结果
     */
    public static <T> T execute(int retryTimes, int intervalMillis, boolean throwException, Executor<T> executor) {
        int totalTryTimes = retryTimes < 0 ? 1 : retryTimes + 1;
        int tryTimes = 0;

        while (tryTimes < totalTryTimes) {
            ++tryTimes;
            try {
                return executor.execute(tryTimes);
            } catch (Exception e) {
                if (tryTimes >= totalTryTimes) {
                    // 重试结束
                    if (throwException) {
                        throw new RuntimeException(e);
                    }
                    return null;
                }
                // 还会继续重试的情况下，检测时间间隔
                if (intervalMillis > 0) {
                    CommonUtil.sleep(intervalMillis);
                }
                if (tryTimes > 1) {
                    logger.warn("准备第[{}]次重试执行，interval=[{}],totalTryTimes=[{}],error=[{}]", (tryTimes - 1), intervalMillis, e.getMessage());
                }
            }
        }
        return null;
    }
}
