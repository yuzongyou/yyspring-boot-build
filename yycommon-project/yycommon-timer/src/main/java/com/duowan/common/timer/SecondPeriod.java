package com.duowan.common.timer;

/**
 * 秒周期
 *
 * @author Arvin
 */
public class SecondPeriod extends AbstractPeriod {

    private final long milliseconds;

    /**
     * 秒周期
     *
     * @param seconds 单位秒
     */
    public SecondPeriod(int seconds) {
        this.milliseconds = seconds * 1000L;
    }

    @Override
    protected long getMilliseconds() {
        return milliseconds;
    }
}
