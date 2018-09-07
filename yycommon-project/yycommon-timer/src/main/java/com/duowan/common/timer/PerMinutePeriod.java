package com.duowan.common.timer;

import com.duowan.common.utils.TimeUtil;

import java.util.Calendar;

/**
 * 每分钟指定的秒数执行
 *
 * @author Arvin
 */
public class PerMinutePeriod extends AbstractPeriod {

    /**
     * 每分钟第几秒的时候执行
     */
    private final int second;

    public PerMinutePeriod(int second) {
        TimeUtil.checkSecond(second);
        this.second = second;
    }

    @Override
    protected long getMilliseconds() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, this.second);
        long diff = cal.getTimeInMillis() - System.currentTimeMillis();
        // 一定要小于等于0
        if (diff <= 0) {
            // 这一分钟已过等待下一分钟
            diff += 60 * 1000L;
        }
        return diff;
    }
}
