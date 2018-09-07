package com.duowan.common.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Arvin
 */
public abstract class AbstractPeriod implements Period {

    protected final Logger logger = LoggerFactory.getLogger("TIMERLOG." + getClass());

    /**
     * 获取毫秒数
     *
     * @return 返回毫秒数
     */
    protected abstract long getMilliseconds();

    /**
     * sleep 指定的毫秒数
     *
     * @param millis
     */
    protected void sleepMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.error("周期Sleep出错： " + e.getMessage(), e);
        }
    }

    @Override
    public boolean sleep() {
        long millis = getMilliseconds();
        if (millis <= 0) {
            millis = 1000;
        }
        sleepMillis(millis);
        return true;
    }
}
