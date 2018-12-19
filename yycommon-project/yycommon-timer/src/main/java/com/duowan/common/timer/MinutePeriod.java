package com.duowan.common.timer;

/**
 * 分钟周期
 *
 * @author Arvin
 */
public class MinutePeriod extends AbstractPeriod {

    private final long milliSeconds;

    public MinutePeriod(int minute) {
        this.milliSeconds = minute * 60 * 1000L;
    }

    @Override
    protected long getMilliseconds() {
        return milliSeconds;
    }
}
