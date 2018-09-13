package com.duoean.common.admincenter.context;

import com.duowan.common.admincenter.context.PrivilegeContext;
import com.duowan.common.admincenter.model.Privilege;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 9:53
 */
public class PrivilegeContextTest {

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
    public void detectBelongPrivilege() throws Exception {

        PrivilegeContext context = new PrivilegeContext(getPrivilageXmlAsText(), true);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getRequestURI()).thenReturn("/contact/inner/sysUser/list.do");

        Privilege privilege = context.detectPrivilege(request);

        assertNotNull(privilege);

        Mockito.when(request.getRequestURI()).thenReturn("/contact/inner/sysUser/query/get.do");
        privilege = context.detectPrivilege(request);

        assertNotNull(privilege);


        Mockito.when(request.getRequestURI()).thenReturn("/contact/xxx.do");

        privilege = context.detectPrivilege(request);

        assertNull(privilege);


    }

}