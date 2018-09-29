package com.duowan.udb.security;

import com.duowan.udb.partner.UserinfoForPartner;
import org.junit.Test;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 12:54
 */
public class UdbPartnerClientTest {

    @Test
    public void testDecryptPartnerInfo() {

        String partnerCkValue = "ECBA7FF9226A1B6D92A5270BA82CFDABB0861FF65DDA1CC4974DB02E9027AB83566CEF838C7DA74FE8156265FE3292E703EEC355204C5FA3624ED675D926810F99FD6B69BDD44F507F16494F6729B683B529FD3533146E6C914F9EF17AC9481D8EE9FADDA4800C4DAC1C31F097810086036985D0B29F5D0FD6EE5F9D7164D2FEEB6CD4DD4C962D7C5AC73080585B49E3F6BB9D01BA3AED88B3AC67C9659B8A243304DCD8319C6FA58C0FD8A82ACEC7167BA4D54F7736F90E0A67748F95C56F7A";
        String yyuid = "1549626765";
        String source = "9B67AA0254BF410F7AA8A38293B007BACED3996EC2E387900681FDC5296E2E9A7CC2DCD292F3754913E956C3A144265E067D6F60BDAB6DB32A2C5DC4D48B23A2";

        UserinfoForPartner userinfoForPartner = new UserinfoForPartner(source, yyuid, partnerCkValue);

        System.out.println(userinfoForPartner.validate());

        System.out.println(userinfoForPartner.getErrMsg());
        System.out.println(userinfoForPartner.getSource());

    }
}
