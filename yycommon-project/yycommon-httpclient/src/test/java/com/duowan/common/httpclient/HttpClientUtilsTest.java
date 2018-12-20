package com.duowan.common.httpclient;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/11/20 21:03
 */
public class HttpClientUtilsTest {

    @Test
    public void doGet() {
        String url = "https://usercenter.yy.com/user/UserWebService/getCacheUserByYyid?name=xxx&age=12";

        HttpTextResponse response = HttpClientUtils.get(url)
                .cookie("udb_oar", "7090C8BB74C9B854067206B0031BE50ADDFC5672F33B049971D04655A995BD25DCA6A0EB507D3AC9F2E3AEA6CF9AFB21ED12353E7895C4C9E32D9554AF22DC6C5E5E5045C78416F221BE157668E90C338CA7122F0BA4CA00443A958E535B7875DF7A30030164803FD9DCEBEDB7EFFC5EAC5702D9A118ABE451F030CEAA1D97C278D028B6B14FEFF1A53EC53A7825795644C81AD830C4199A8E196210487DA5701C04E00F32219E7F8D736AD4B07C5542A1307F595600EBBD8F99273360B452FBA5A7822F16AF88D2DB4C232380365D987E0EA6FC460D1E46BA9D06EABD1C6BDDB54B22836073928CA00DFCCA5516BF7744905D3AF0A5CF386DCDA50309C8EC1CCFC54FE25D541E847705F97E8C22322EB6025901CD3C737C2C49FDF29A47D38EA407F57A13A9C90F6D45800573D99ACB15E53079641605EEA8AFCC6E180657D1435BB696E304B788BBD9926A9A16D2F05D9CF4EBA24E9BC8AC442C495E267A1DA59C02BBC994451EFFDB0BEF68EE5901C596808B2930496ADACDAB876173CB9A")
                .cookie("username", "dw_xiajiqiu1")
                .param("name", "中文++ 撒旦")
                .header("xtoken", "asdweiunsybUabsdbskfhun")
                .responseText();

        String responseText = response.asText();

        System.out.println(responseText);
        System.out.println(JSON.toJSONString(response.headers(), true));

    }

    @Test
    public void doPost() {
        String url = "http://usercenter.game.yy.com/user/UserWebService/getCacheUserByYyid?name=xxx&age=12";

        String responseText = HttpClientUtils.post(url)
                .cookie("udb_oar", "28B335BF71385CA88ED39DF22DF7F735BDFE40B3F1B6097C972E419ABF1EB604E8816E38B4E93F7844B8DD2F2739C63DF380E83351C3F7095D5174D5147B833722ECCB7A9C00F78A30E4EE93940316CD05E65C231B085256E4B2AE71899D4418FD8399629E31FE59F5C93FDBD2A45E48EF501496722384D792B45F4D16A027EB63E24A1F47F9FE39936A52471424BE01F590B38DF2D358C95FAFACB6D22351F90966D03C862F20CAEF9EB0C7153D8AEFECFFFB07F304BD368B4265B4477842DE271D023A0E713321B10B278E4C9CA995B010158D973E06BDDE8A35B3657D0A9E3E3F6A1C8C504F891C5EEE166878A6783F143D4A6AD2ED480B65167D359D88285FB44A9B346A59D5BD0559871F465A31AD497AF48138FF97C2188396A6104D59FFAF963A2430163E5141FCA5D658B87724C5A845A3BB2F94213DAAEEE5003969")
                .cookie("username", "qq_7z2icq5ucu54")
                .responseText()
                .asText();

        System.out.println(responseText);

    }

    @Test
    public void doDelete() {
        String url = "http://usercenter.game.yy.com/user/UserWebService/getCacheUserByYyid?name=xxx&age=12";

        String responseText = HttpClientUtils.delete(url)
                .cookie("udb_oar", "28B335BF71385CA88ED39DF22DF7F735BDFE40B3F1B6097C972E419ABF1EB604E8816E38B4E93F7844B8DD2F2739C63DF380E83351C3F7095D5174D5147B833722ECCB7A9C00F78A30E4EE93940316CD05E65C231B085256E4B2AE71899D4418FD8399629E31FE59F5C93FDBD2A45E48EF501496722384D792B45F4D16A027EB63E24A1F47F9FE39936A52471424BE01F590B38DF2D358C95FAFACB6D22351F90966D03C862F20CAEF9EB0C7153D8AEFECFFFB07F304BD368B4265B4477842DE271D023A0E713321B10B278E4C9CA995B010158D973E06BDDE8A35B3657D0A9E3E3F6A1C8C504F891C5EEE166878A6783F143D4A6AD2ED480B65167D359D88285FB44A9B346A59D5BD0559871F465A31AD497AF48138FF97C2188396A6104D59FFAF963A2430163E5141FCA5D658B87724C5A845A3BB2F94213DAAEEE5003969")
                .cookie("username", "qq_7z2icq5ucu54")
                .responseText()
                .asText();

        System.out.println(responseText);

    }

    @Test
    public void doPut() {
        String url = "http://usercenter.game.yy.com/user/UserWebService/getCacheUserByYyid?name=xxx&age=12";

        String responseText = HttpClientUtils.put(url)
                .cookie("udb_oar", "28B335BF71385CA88ED39DF22DF7F735BDFE40B3F1B6097C972E419ABF1EB604E8816E38B4E93F7844B8DD2F2739C63DF380E83351C3F7095D5174D5147B833722ECCB7A9C00F78A30E4EE93940316CD05E65C231B085256E4B2AE71899D4418FD8399629E31FE59F5C93FDBD2A45E48EF501496722384D792B45F4D16A027EB63E24A1F47F9FE39936A52471424BE01F590B38DF2D358C95FAFACB6D22351F90966D03C862F20CAEF9EB0C7153D8AEFECFFFB07F304BD368B4265B4477842DE271D023A0E713321B10B278E4C9CA995B010158D973E06BDDE8A35B3657D0A9E3E3F6A1C8C504F891C5EEE166878A6783F143D4A6AD2ED480B65167D359D88285FB44A9B346A59D5BD0559871F465A31AD497AF48138FF97C2188396A6104D59FFAF963A2430163E5141FCA5D658B87724C5A845A3BB2F94213DAAEEE5003969")
                .cookie("username", "qq_7z2icq5ucu54")
                .responseText()
                .asText();

        System.out.println(responseText);

    }
}