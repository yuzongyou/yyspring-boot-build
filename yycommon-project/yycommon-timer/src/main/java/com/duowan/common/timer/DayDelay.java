package com.duowan.common.timer;

/**
 * 多少天之后执行
 *
 * @author Arvin
 */
public class DayDelay implements Delay {

    /** 延迟毫秒数 */
    private final long milliseconds;

    public DayDelay(int days) {
        this.milliseconds = days * 24 * 60 * 60 * 1000L;
    }

    @Override
    public long getDelayMillis() {
        return this.milliseconds;
    }
}
