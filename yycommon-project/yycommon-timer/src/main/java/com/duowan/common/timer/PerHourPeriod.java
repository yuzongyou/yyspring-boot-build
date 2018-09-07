package com.duowan.common.timer;

import com.duowan.common.utils.TimeUtil;

import java.util.Calendar;

/**
 * 每小时什么时候执行
 *
 * @author Arvin
 */
public class PerHourPeriod extends AbstractPeriod {

    /** 第几分钟 */
    private final int minute;

    /** 第几秒 */
    private final int second;


    public PerHourPeriod(int minute, int second) {
        TimeUtil.checkMinute(minute);
        TimeUtil.checkSecond(second);
        this.minute = minute;
        this.second = second;
    }

    public PerHourPeriod(int minute) {
        this(minute, 0);
    }

    @Override
    protected long getMilliseconds() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, this.minute);
        cal.set(Calendar.SECOND, this.second);
        long diff = cal.getTimeInMillis() - System.currentTimeMillis();
        // 一定要小于等于0
        if (diff <= 0) {
            // 这一小时已经过了，等待下一小时执行
            diff += 60 * 60 * 1000L;
        }
        return diff;
    }
}
