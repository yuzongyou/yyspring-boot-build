package com.duowan.common.ipowner.seeker;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertNotNull;

/**
 * @author Arvin
 */
public class IpProvinceAndCitySeekerTest {

    @Test
    public void testFind() throws Exception {

        URL url = getClass().getClassLoader().getResource("provinceAndCityIp.datx");

        assertNotNull(url);

        IpProvinceAndCitySeeker seeker = new IpProvinceAndCitySeeker(url.getFile());

        // result: [中国] [广东] [广州] [] [联通] [23.125178] [113.280637] [Asia/Shanghai] [UTC+8] [440100] [86] [CN] [AP]
        // ["中国","广东","广州","","联通","23.125178","113.280637","Asia/Shanghai","UTC+8","440100","86","CN","AP"]
        // array[0]:    国家/地区
        // array[1]:    省份/直辖市
        // array[2]:    地级市/省直辖县级行政区
        // array[3]:    区县
        // array[4]:    网络运营商
        // array[5]:    经度
        // array[6]:    纬度
        // array[7]:    国际、火星坐标
        // array[8]:    时区、国际协调时间
        // array[9]:    行政区划代码
        // array[10]:   国家代码
        // array[11]:   国际代码
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