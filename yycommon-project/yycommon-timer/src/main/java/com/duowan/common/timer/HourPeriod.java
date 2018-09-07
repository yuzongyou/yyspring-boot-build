package com.duowan.common.timer;

/**
 * 小时周期
 *
 * @author Arvin
 */
public class HourPeriod extends AbstractPeriod {

    private final long milliSeconds;

    public HourPeriod(int hour) {
        this.milliSeconds = hour * 60 * 60 * 1000;
    }

    @Override
    protected long getMilliseconds() {
        return milliSeconds;
    }
}
