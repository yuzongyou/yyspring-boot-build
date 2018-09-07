package com.duowan.common.timer;

/**
 * 毫秒时间延迟
 *
 * @author Arvin
 */
public class MillisecondDelay implements Delay {

    /** 毫秒 */
    private final long milliseconds;

    public MillisecondDelay(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    @Override
    public long getDelayMillis() {
        return milliseconds;
    }
}
