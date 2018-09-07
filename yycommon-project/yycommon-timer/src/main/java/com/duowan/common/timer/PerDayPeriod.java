package com.duowan.common.timer;

import com.duowan.common.utils.TimeUtil;

import java.util.Calendar;

/**
 * 每日周期
 *
 * @author Arvin
 */
public class PerDayPeriod extends AbstractPeriod {

    private final int hour24;
    private final int minute;
    private final int second;

    /**
     * 设置时分秒， 24小时制
     *
     * @param hour24 小时
     * @param minute 分钟
     * @param second 秒钟
     */
    public PerDayPeriod(int hour24, int minute, int second) {
        TimeUtil.checkHourMinuteSecond(hour24, minute, second);
        this.hour24 = hour24;
        this.minute = minute;
        this.second = second;
    }

    /**
     * 设置时分秒， 24小时制
     *
     * @param hour24 小时
     * @param minute 分钟
     */
    public PerDayPeriod(int hour24, int minute) {
        this(hour24, minute, 0);
    }

    /**
     * 设置时分秒， 24小时制
     *
     * @param hour24 小时
     */
    public PerDayPeriod(int hour24) {
        this(hour24, 0, 0);
    }

    @Override
    protected long getMilliseconds() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, this.hour24);
        cal.set(Calendar.MINUTE, this.minute);
        cal.set(Calendar.SECOND, this.second);
        long diff = cal.getTimeInMillis() - System.currentTimeMillis();
        // 一定要小于等于0
        if (diff <= 0) {
            // 今天的时间已过，明天再执行
            diff += 24 * 60 * 60 * 1000L;
        }
        return diff;
    }
}
