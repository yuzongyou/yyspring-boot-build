package com.duowan.common.innerip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/8 22:23
 */
public class OfficialIpParser {

    /**
     * IP 行正则
     */
    private static final String REGEX = "^(.*?)([0-9]{4}/[0-9]+/[0-9]+)?\\s+([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)\\s*$";

    /**
     * 环球网校_北京威地大厦 61.148.205.165
     * 上海办公室 2018/4/20 116.236.170.6
     * 珠海唐家大楼2018/1/29 59.38.34.130
     * 佛山VPN 14.215.104.211
     * 广州羊城创意产业园3-08多玩业务部移动IP 2017/03/10 183.237.185.79
     * 广州番禺万达广场B1 联通1.5G IP 2017/4/24 58.248.229.128
     * <p>
     * <p>
     * 没有非常一致的格式，主要是由三列组成
     * 最后一列：   具体的IP
     * 倒数第二列:  如果是日期格式 yyyy/M/d 那么解析成添加时间
     * 剩下的就是IP地址的说明
     *
     * @param line 办公网IP 行
     * @return 如果解析成功返回对象不成功返回null
     */
    public OfficialIp parse(String line) {
        try {

            if (null == line || "".equals(line.trim())) {
                return null;
            }

            String ip = trim(line.replaceFirst(REGEX, "$3"));
            String desc = trim(line.replaceFirst(REGEX, "$1"));
            String time = trim(line.replaceFirst(REGEX, "$2"));

            Date addDate = toDate(time);

            return new OfficialIp(ip, desc, addDate);
        } catch (Exception e) {
            return null;
        }
    }

    private static String trim(String value) {
        return null == value ? null : value.trim();
    }

    /**
     * 返回日期
     *
     * @param time 时间字符串, 中间使用 / 分割， 格式是 xx/xx/xx
     * @return 返回良好格式字符串
     */
    private static Date toDate(String time) {
        String[] array = time.split("/");

        if (array.length != 3) {
            return null;
        }

        String year = array[0];
        String month = array[1];
        String day = array[2];

        if (month.length() == 1) {
            month = "0" + day;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        try {
            return dateFormat.parse(year + "/" + month + "/" + day);
        } catch (ParseException e) {
            return null;
        }

    }
}
