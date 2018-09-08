package com.duowan.common.timer;

import java.util.Calendar;

/**
 * 达到固定时间后执行
 *
 * @author Arvin
 */
public class FixTimeDelay implements Delay {

    /** 延迟毫秒数 */
    private final long milliseconds;

    public static class Builder {

        private int year;
        private int month;
        private int day;
        private int hour;
        private int minute;
        private int second;

        public Builder() {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);
            day = cal.get(Calendar.DAY_OF_MONTH);
            hour = cal.get(Calendar.HOUR_OF_DAY);
            minute = cal.get(Calendar.MINUTE);
            second = cal.get(Calendar.SECOND);
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        /**
         * 月份
         *
         * @param month 1月就是1，2月就是2，以此类推
         * @return 返回Builder本身
         */
        public Builder month(int month) {
            this.month = month - 1;
            return this;
        }

        public Builder day(int day) {
            this.day = day;
            return this;
        }

        /**
         * 24 小时 制
         *
         * @param hour 小时
         * @return 返回Builder本身
         */
        public Builder hour(int hour) {
            this.hour = hour;
            return this;
        }

        public Builder minute(int minute) {
            this.minute = minute;
            return this;
        }

        public Builder second(int second) {
            this.second = second;
            return this;
        }

        public FixTimeDelay build() {
            return new FixTimeDelay(year, month, day, hour, minute, second);
        }
    }


    public static Builder builder() {
        return new Builder();
    }

    private FixTimeDelay(int year, int month, int day, int hour, int minute, int second) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);

        this.milliseconds = cal.getTimeInMillis() - System.currentTimeMillis();
    }

    @Override
    public long getDelayMillis() {
        return this.milliseconds;
    }
}
