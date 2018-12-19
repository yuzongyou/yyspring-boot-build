package com.duowan.common.ipowner.seeker;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/9 16:52
 */
public class IpDistrictSeekerTest {
    @Test
    public void testFind() throws Exception {

        URL url = getClass().getClassLoader().getResource("districtIp.datx");

        assertNotNull(url);

        IpDistrictSeeker seeker = new IpDistrictSeeker(url.getFile());

        // result: [中国] [广东] [广州] [番禺区] [440113] [0.5] [113.38397] [22.93599]
        // ["中国","广东","广州","番禺区","440113","9.4","113.38397","22.93599"]
        // array[0]:    国家/地区
        // array[1]:    省份/直辖市
        // array[2]:    地级市/省直辖县级行政区
        // array[3]:    区县
        // array[4]:    中国行政区划代码
        // array[5]:    覆盖方位，IP使用区域半径，单位：千米
        // array[6]:    区县中心点经度
        // array[6]:    区县中心点纬度
        String[] result = seeker.find("58.248.229.154");

        printArray(result);

    }

    public static void printArray(String[] array) {
        for (String item : array) {
            System.out.print("[" + item + "] ");
        }
        System.out.println();
    }
}