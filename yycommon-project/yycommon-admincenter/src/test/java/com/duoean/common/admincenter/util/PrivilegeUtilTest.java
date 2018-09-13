package com.duoean.common.admincenter.util;

import com.duowan.common.admincenter.model.Privilege;
import com.duowan.common.admincenter.util.PrivilegeUtil;
import com.duowan.common.utils.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/27 14:25
 */
public class PrivilegeUtilTest {

    /**
     * 获取 privilege.xml
     *
     * @return
     */
    public String getPrivilageXmlAsText() throws IOException {
        ClassPathResource resource = new ClassPathResource("/privilege-test.xml");
        return IOUtils.toString(new InputStreamReader(resource.getInputStream(), "UTF-8"));
    }

    @Test
    public void testParseByXml() throws Exception {
        String xmlText = getPrivilageXmlAsText();

        List<Privilege> privilegeList = PrivilegeUtil.parseByXmlUnRefParent(xmlText);

        System.out.println(JsonUtil.toPrettyJson(privilegeList));
    }


}