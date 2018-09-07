package com.duowan.yyspringboot.autoconfigure.virtualdns;

import com.duowan.common.utils.JsonUtil;
import com.duowan.common.virtualdns.VirtualDnsUtil;
import com.duowan.yyspring.boot.AppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/1 19:52
 */
@Configuration
@ConditionalOnClass({com.duowan.common.virtualdns.VirtualDnsUtil.class, com.duowan.common.dns.util.InetAddressUtil.class})
@EnableConfigurationProperties(VirtualDnsProperties.class)
public class VirtualDnsAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(VirtualDnsAutoConfiguration.class);

    public VirtualDnsAutoConfiguration(VirtualDnsProperties properties) {
        initVirtualDns(properties);
    }

    private void initVirtualDns(VirtualDnsProperties properties) {
        List<Resource> resourceList = new ArrayList<>();
        List<Resource> customResourceList = AppContext.tryGetResources(properties.getHostsPaths());

        if (!customResourceList.isEmpty()) {
            resourceList.addAll(customResourceList);
        }

        List<Resource> defaultResourceList = AppContext.lookupConfigResourceList("hosts");
        if (!defaultResourceList.isEmpty()) {
            resourceList.addAll(defaultResourceList);
        }

        resourceList = filterNotExistsResources(resourceList);

        if (null == resourceList || resourceList.isEmpty()) {
            logger.info("没有搜索到自定的DNS HOSTS 文件！");
            return;
        }

        List<String> ipDomainLineList = new ArrayList<>();
        for (int i = resourceList.size() - 1; i >= 0; --i) {
            Resource resource = resourceList.get(i);
            try {
                logger.info("加载自定义Host文件： " + resource.getURI());

                List<String> subIpDomainLineList = readResourceAsLines(resource);

                if (subIpDomainLineList != null && !subIpDomainLineList.isEmpty()) {
                    ipDomainLineList.addAll(subIpDomainLineList);
                }
            } catch (IOException e) {
                throw new RuntimeException("加载自定义DNS配置出错： " + e.getMessage(), e);
            }
        }

        Map<String, List<String>> domainIpsMap = VirtualDnsUtil.buildDnsMapByIpDomainLines(ipDomainLineList, true);
        if (domainIpsMap == null || domainIpsMap.isEmpty()) {
            logger.info("所有的Host文件都没有配置有效的Host！");
            return;
        }

        VirtualDnsUtil.enabled();
        VirtualDnsUtil.set(ipDomainLineList);
        logger.info("成功加载了自定义的DNS，加载的配置包含: " + JsonUtil.toPrettyJson(domainIpsMap));
    }

    private List<Resource> filterNotExistsResources(List<Resource> resourceList) {
        List<Resource> resultList = new ArrayList<>();
        for (Resource resource : resourceList) {
            if (null != resource && resource.exists()) {
                resultList.add(resource);
            }
        }
        return resultList;
    }

    private List<String> readResourceAsLines(Resource resource) {
        List<String> ipDomainList = new ArrayList<>();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                ipDomainList.add(line.trim());
            }
            return ipDomainList;
        } catch (Exception ignored) {
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }

        return ipDomainList;

    }

}
