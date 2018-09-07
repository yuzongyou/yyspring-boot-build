package com.duowan.common.timer;

/**
 * 毫秒周期
 *
 * @author Arvin
 */
public class MillisecondPeriod extends AbstractPeriod {

    private final long milliSeconds;

    /**
     * 构造周期，单位毫秒
     *
     * @param milliSeconds 毫秒数
     */
    public MillisecondPeriod(long milliSeconds) {
        this.milliSeconds = milliSeconds;
    }

    @Override
    protected long getMilliseconds() {
        return this.milliSeconds;
    }

}
