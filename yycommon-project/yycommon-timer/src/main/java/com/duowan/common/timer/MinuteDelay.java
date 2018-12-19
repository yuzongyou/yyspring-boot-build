package com.duowan.common.timer;

/**
 * 延迟分钟
 *
 * @author Arvin
 */
public class MinuteDelay implements Delay {

    /** 要延迟的毫秒数 */
    private final long milliseconds;

    public MinuteDelay(int minute) {
        this.milliseconds = minute * 60 * 1000L;
    }

    @Override
    public long getDelayMillis() {
        return this.milliseconds;
    }
}
