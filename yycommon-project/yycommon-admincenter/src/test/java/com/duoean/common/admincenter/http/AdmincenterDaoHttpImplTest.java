package com.duoean.common.admincenter.http;

import com.duowan.common.admincenter.dao.http.AdmincenterDaoHttpImpl;
import com.duowan.common.utils.JsonUtil;
import com.duowan.common.virtualdns.VirtualDnsUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 11:04
 */
public class AdmincenterDaoHttpImplTest {

    private String productId = "mbga_admin";
    private String productKey = "97MoCcsozg3kG9mow83DoDv26ikLYM";

    @Before
    public void ready() {
        VirtualDnsUtil.add("admincenter.game.yy.com", "118.191.11.29");
    }

    @Test
    public void getPrivilegeIds() throws Exception {

        AdmincenterDaoHttpImpl admincenterDao = new AdmincenterDaoHttpImpl(productId, productKey, null);

        Set<String> privilegeIds = admincenterDao.getPrivilegeIds("dw_xiajiqiu1");

        System.out.println(JsonUtil.toPrettyJson(privilegeIds));

    }

}