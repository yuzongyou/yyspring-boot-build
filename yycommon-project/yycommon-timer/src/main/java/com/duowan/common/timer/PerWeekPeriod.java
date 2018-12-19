package com.duowan.common.timer;

import com.duowan.common.timer.exception.TimerException;
import com.duowan.common.utils.TimeUtil;
import com.duowan.common.utils.WeekDay;

import java.util.Calendar;

/**
 * 每周指定时分秒执行
 *
 * @author Arvin
 */
public class PerWeekPeriod extends AbstractPeriod {

    /**
     * 一周七天的时间间隔
     */
    protected static final long WEEK_INTERVAL_MILLIS = 7 * 24 * 60 * 60 * 1000L;

    private final WeekDay weekDay;
    /**
     * 小时
     */
    private final int hour24;

    /**
     * 分钟
     */
    private final int minute;

    /**
     * 秒
     */
    private final int second;

    public static class Builder {

        private WeekDay weekDay;
        private int hour = 23;
        private int minute = 59;
        private int second = 59;

        /**
         * 设置星期几执行
         *
         * @param weekDay 周几
         * @return 返回 Builder
         */
        public Builder weekDay(WeekDay weekDay) {
            if (null == weekDay) {
                throw new TimerException("必须设定星期几执行！");
            }
            this.weekDay = weekDay;
            return this;
        }

        /**
         * 24 小时格式制
         *
         * @param hour 小时， [0,23]
         * @return 返回 Builder 对象
         */
        public Builder hour(int hour) {
            TimeUtil.check24Hour(hour);
            this.hour = hour;
            return this;
        }

        /**
         * 分钟， 0-59
         *
         * @param minute 分钟
         * @return 返回 Builder 对象
         */
        public Builder minute(int minute) {
            TimeUtil.checkMinute(minute);
            this.minute = minute;
            return this;
        }

        /**
         * 秒钟， 0-59
         *
         * @param second 秒钟
         * @return 返回 Builder 对象
         */
        public Builder second(int second) {
            TimeUtil.checkSecond(second);
            this.second = second;
            return this;
        }

        public PerWeekPeriod build() {
            return new PerWeekPeriod(weekDay, hour, minute, second);
        }
    }

    public static Builder builder(WeekDay weekDay) {
        return new Builder().weekDay(weekDay);
    }

    /**
     * 指定每个月第一天的开始执行时分秒
     *
     * @param weekDay 周几
     * @param hour24  小时
     * @param minute  分钟
     * @param second  秒钟
     */
    private PerWeekPeriod(WeekDay weekDay, int hour24, int minute, int second) {
        this.weekDay = weekDay;
        this.hour24 = hour24;
        this.minute = minute;
        this.second = second;
    }

    @Override
    protected long getMilliseconds() {

        long curWeekExecutePoint = calculateCurrentWeekExecutePoint();

        long diff = curWeekExecutePoint - System.currentTimeMillis();

        if (diff > 0) {
            // 当周执行时间点还没有到，直接返回要Sleep的时间毫秒数
            return diff;
        }

        // 当周执行时间点已经过了，需要获取下周的执行时间点
        return curWeekExecutePoint + WEEK_INTERVAL_MILLIS;
    }

    /**
     * 计算本周的执行时间点
     *
     * @return 返回毫秒数
     */
    private long calculateCurrentWeekExecutePoint() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, weekDay.getDay());
        cal.set(Calendar.HOUR_OF_DAY, this.hour24);
        cal.set(Calendar.MINUTE, this.minute);
        cal.set(Calendar.SECOND, this.second);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

}
