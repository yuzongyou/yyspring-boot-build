package com.duowan.common.timer;

import com.duowan.common.utils.TimeUtil;

import java.util.Calendar;

/**
 * 每月月初执行， 可以定义月初的什么时间执行，如每个月第一天几点几分几秒执行
 *
 * @author Arvin
 */
public class PerMonthBeginPeriod extends AbstractPeriod {

    /** 每月第一天 */
    protected static final int FIRST_DAY_OF_MONTH = 1;

    /** 小时 */
    private final int hour;

    /** 分钟 */
    private final int minute;

    /** 秒 */
    private final int second;

    public static class Builder {

        private int hour24 = 0;
        private int minute = 0;
        private int second = 0;

        /**
         * 24 小时格式制
         *
         * @param hour24 小时， [0,23]
         * @return 返回 Builder 对象
         */
        public Builder hour(int hour24) {
            TimeUtil.check24Hour(hour24);
            this.hour24 = hour24;
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

        public PerMonthBeginPeriod build() {
            return new PerMonthBeginPeriod(hour24, minute, second);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 指定每个月第一天的开始执行时分秒
     *
     * @param hour   小时
     * @param minute 分钟
     * @param second 秒钟
     */
    private PerMonthBeginPeriod(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    @Override
    protected long getMilliseconds() {

        long curMonthExecutePoint = calculateCurrentMonthExecutePoint();

        long diff = curMonthExecutePoint - System.currentTimeMillis();

        if (diff > 0) {
            // 当月执行时间点还没有到，直接返回要Sleep的时间毫秒数
            return diff;
        }

        // 当月执行时间点已经过了，需要获取下个月的执行时间点
        long nextMonthExecutePoint = calculateNextMonthExecutePoint();

        return nextMonthExecutePoint - System.currentTimeMillis();
    }

    /**
     * 计算下个月的执行时间点
     *
     * @return 返回毫秒数
     */
    private long calculateNextMonthExecutePoint() {

        Calendar cal = Calendar.getInstance();

        // 下个月
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        // 次月第一天
        cal.set(Calendar.DAY_OF_MONTH, FIRST_DAY_OF_MONTH);
        cal.set(Calendar.HOUR_OF_DAY, this.hour);
        cal.set(Calendar.MINUTE, this.minute);
        cal.set(Calendar.SECOND, this.second);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }

    /**
     * 计算当月执行时间点
     *
     * @return 返回毫秒数
     */
    private long calculateCurrentMonthExecutePoint() {

        Calendar cal = Calendar.getInstance();

        // 当月第一天
        cal.set(Calendar.DAY_OF_MONTH, FIRST_DAY_OF_MONTH);
        cal.set(Calendar.HOUR_OF_DAY, this.hour);
        cal.set(Calendar.MINUTE, this.minute);
        cal.set(Calendar.SECOND, this.second);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }
}
