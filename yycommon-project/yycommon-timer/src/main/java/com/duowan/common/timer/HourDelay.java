package com.duowan.common.timer;

/**
 * 多少小时之后
 *
 * @author Arvin
 */
public class HourDelay implements Delay {

    /** 延迟毫秒数 */
    private final long milliseconds;

    public HourDelay(int hour) {
        this.milliseconds = hour * 60 * 60 * 1000;
    }

    @Override
    public long getDelayMillis() {
        return this.milliseconds;
    }
}
