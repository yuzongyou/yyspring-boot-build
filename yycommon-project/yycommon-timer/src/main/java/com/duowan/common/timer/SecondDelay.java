package com.duowan.common.timer;

/**
 * 秒延迟
 *
 * @author Arvin
 */
public class SecondDelay implements Delay {

    /** 延迟毫秒数 */
    private final long milliseconds;

    public SecondDelay(int seconds) {
        this.milliseconds = seconds * 1000L;
    }

    @Override
    public long getDelayMillis() {
        return this.milliseconds;
    }
}
