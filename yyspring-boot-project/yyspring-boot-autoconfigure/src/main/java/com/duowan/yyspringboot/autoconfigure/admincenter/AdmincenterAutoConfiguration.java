package com.duowan.yyspringboot.autoconfigure.admincenter;

import com.duowan.common.admincenter.context.PrivilegeContext;
import com.duowan.common.admincenter.dao.AdmincenterDao;
import com.duowan.common.admincenter.dao.http.AdmincenterDaoHttpImpl;
import com.duowan.common.admincenter.model.Privilege;
import com.duowan.common.admincenter.service.AdmincenterService;
import com.duowan.common.admincenter.service.impl.AdmincenterInterceptorService;
import com.duowan.common.utils.AssertUtil;
import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspringboot.autoconfigure.udbsecurity.UdbSecurityAutoConfiguration;
import com.duowan.yyspringboot.autoconfigure.udbsecurity.UdbProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.Servlet;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/8/28 14:17
 */
@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnClass({Servlet.class, DispatcherServlet.class, AdmincenterService.class})
@EnableConfigurationProperties({UdbProperties.class, AdmincenterProperties.class})
@AutoConfigureAfter(UdbSecurityAutoConfiguration.class)
public class AdmincenterAutoConfiguration {

    @Bean
    public AdmincenterDaoHttpImpl admincenterDaoHttpImpl(AdmincenterProperties admincenterProperties) {

        AssertUtil.assertNotBlank(admincenterProperties.getProductId(), "Admincenter's productId should not be null");
        AssertUtil.assertNotBlank(admincenterProperties.getProductKey(), "Admincenter's productKey should not be null");

        return new AdmincenterDaoHttpImpl(
                admincenterProperties.getProductId(),
                admincenterProperties.getProductKey(),
                admincenterProperties.getFetchPrivilegeUrl());
    }

    @Bean
    public PrivilegeContext privilegeContext(AdmincenterProperties admincenterProperties) throws Exception {
        String privilegeXmlText = PrivilegeXmlLoader.loadPrivilegeXml(admincenterProperties.getPrivilegeXmlPath());

        Boolean needCheckPrivilege = admincenterProperties.getNeedCheckPrivilege();
        if (null == needCheckPrivilege) {
            needCheckPrivilege = !AppContext.isDev();
        }

        return new PrivilegeContext(privilegeXmlText, needCheckPrivilege);
    }

    @Bean
    public AdmincenterInterceptorService admincenterInterceptorService(AdmincenterDao admincenterDao,
                                                                       PrivilegeContext privilegeContext) {
        return new AdmincenterInterceptorService(admincenterDao, privilegeContext);
    }

    @Bean
    public AdmincenterPrivilegeInterceptor admincenterPrivilegeInterceptor(AdmincenterService admincenterService, AdmincenterProperties admincenterProperties) {
        return new AdmincenterPrivilegeInterceptor(admincenterService, admincenterProperties.getForbiddenUrl());
    }

    @Bean
    public AdmincenterPatternProvider admincenterPatternProvider(AdmincenterProperties admincenterProperties, PrivilegeContext privilegeContext) {
        Set<String> excludePatterns = new HashSet<>(Arrays.asList(admincenterProperties.getExcludePathPatterns()));
        if (StringUtils.isNotBlank(admincenterProperties.getForbiddenUrl())) {
            excludePatterns.add(admincenterProperties.getForbiddenUrl());
        }
        if (StringUtils.isNotBlank(admincenterProperties.getLogoutUrl())) {
            excludePatterns.add(admincenterProperties.getLogoutUrl());
        }

        return new AdmincenterPatternProvider(excludePatterns, extractIncludePathPatterns(privilegeContext));
    }

    private Set<String> extractIncludePathPatterns(PrivilegeContext privilegeContext) {

        List<Privilege> allPrivileges = privilegeContext.getAllPrivileges();

        Set<String> patterns = new HashSet<>();
        if (null != allPrivileges && !allPrivileges.isEmpty()) {
            for (Privilege privilege : allPrivileges) {
                List<String> urlList = privilege.getUrlList();
                if (null != urlList && !urlList.isEmpty()) {
                    for (String url : urlList) {
                        if (StringUtils.isNotBlank(url)) {
                            patterns.add(url);
                        }
                    }
                }
            }
        }

        return patterns;

    }

    @Bean
    public PrivilegeController privilegeController(AdmincenterProperties admincenterProperties) {
        AssertUtil.assertNotBlank(admincenterProperties.getProductId(), "Admincenter's productId should not be null");
        String logoutUrl = admincenterProperties.getLogoutUrl();
        if (StringUtils.isBlank(logoutUrl)) {
            logoutUrl = "/udb/logout.do";
        }
        return new PrivilegeController(
                admincenterProperties.getProductId(),
                logoutUrl,
                admincenterProperties.getProductName());
    }
}
