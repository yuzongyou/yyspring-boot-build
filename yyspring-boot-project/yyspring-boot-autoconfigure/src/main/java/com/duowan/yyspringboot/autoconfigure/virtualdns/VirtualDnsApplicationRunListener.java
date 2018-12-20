package com.duowan.yyspringboot.autoconfigure.virtualdns;

import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.JsonUtil;
import com.duowan.common.virtualdns.VirtualDnsUtil;
import com.duowan.yyspring.boot.AppContext;
import com.duowan.yyspring.boot.SpringApplicationRunListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
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
 * @since 2018/9/29 10:33
 */
public class VirtualDnsApplicationRunListener extends SpringApplicationRunListenerAdapter {

    private boolean needAutoConfigurer;

    public VirtualDnsApplicationRunListener(SpringApplication application, String[] args) {
        super(application, args);
        this.needAutoConfigurer = isClassesImported(
                "com.duowan.common.virtualdns.VirtualDnsUtil",
                "com.duowan.common.dns.util.InetAddressUtil"
        );
    }

    @Override
    protected boolean needAutoConfigurer() {
        return needAutoConfigurer;
    }

    @Override
    public void doEnvironmentPrepared(ConfigurableEnvironment environment) {
        VirtualDnsProperties properties = bindProperties(VirtualDnsProperties.PROPERTIES_PREFIX, VirtualDnsProperties.class);

        initVirtualDns(properties == null ? new VirtualDnsProperties() : properties);
    }

    private static void initVirtualDns(VirtualDnsProperties properties) {
        List<Resource> resourceList = new ArrayList<>();
        List<Resource> customResourceList = AppContext.tryGetResources(properties.getHostsPaths());

        if (!customResourceList.isEmpty()) {
            resourceList.addAll(customResourceList);
        }

        List<Resource> defaultResourceList = new ArrayList<>();
        CommonUtil.appendList(defaultResourceList, AppContext.lookupConfigResourceList("hosts"));
        CommonUtil.appendList(defaultResourceList, AppContext.lookupConfigResourceList("hosts.properties"));

        if (!defaultResourceList.isEmpty()) {
            resourceList.addAll(defaultResourceList);
        }

        resourceList = filterNotExistsResources(resourceList);

        if (null == resourceList || resourceList.isEmpty()) {
            System.err.println("没有搜索到自定的DNS HOSTS 文件！");
            return;
        }

        List<String> ipDomainLineList = new ArrayList<>();
        for (int i = resourceList.size() - 1; i >= 0; --i) {
            Resource resource = resourceList.get(i);
            try {
                System.err.println("加载自定义Host文件： " + resource.getURI());

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
            System.err.println("所有的Host文件都没有配置有效的Host！");
            return;
        }

        VirtualDnsUtil.enabled();
        VirtualDnsUtil.set(ipDomainLineList);
        System.err.println("成功加载了自定义的DNS，加载的配置包含: " + JsonUtil.toPrettyJson(domainIpsMap));
    }

    private static List<Resource> filterNotExistsResources(List<Resource> resourceList) {
        List<Resource> resultList = new ArrayList<>();
        for (Resource resource : resourceList) {
            if (null != resource && resource.exists()) {
                resultList.add(resource);
            }
        }
        return resultList;
    }

    private static List<String> readResourceAsLines(Resource resource) {
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
