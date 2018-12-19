package com.duowan.common.innerip;

import com.duowan.common.utils.HttpUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xiajiqiu
 * @version 1.0
 * @since 2018/9/9 15:53
 */
public class OfficialIpParserTest {

    @Test
    public void parse() {

        String responseText = HttpUtil.doGet("http://webapi.sysop.duowan.com:62175/office/ip_desc.txt");

        String[] lines = responseText.split("[\r\n]+");

        OfficialIpParser parser = new OfficialIpParser();

        List<OfficialIp> officialIpList = new ArrayList<>();

        int index = 0;
        for (String line : lines) {
            //System.out.println("line[" + ++index + "] : " + line);

            ++index;
            OfficialIp officialIp = parser.parse(line);
            if (null != officialIp) {
                officialIpList.add(officialIp);
            }

        }

        System.out.println(officialIpList.size() + " == " + index);
    }
}