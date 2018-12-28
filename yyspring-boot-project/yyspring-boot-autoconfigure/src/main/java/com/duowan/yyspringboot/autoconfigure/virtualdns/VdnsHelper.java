package com.duowan.yyspringboot.autoconfigure.virtualdns;

import com.duowan.common.dns.CompositeDnsResolver;
import com.duowan.common.dns.HostsFilesDnsResolver;
import com.duowan.common.dns.SystemHostsFileDnsResolver;
import com.duowan.common.utils.CommonUtil;
import com.duowan.common.utils.FileUtil;
import com.duowan.common.vdns.VirtualDnsUtil;
import com.duowan.yyspring.boot.AppContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/28 14:06
 */
public class VdnsHelper {

    private VdnsHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static void initVirtualDns(VirtualDnsProperties properties) {

        List<String> hostsFilesPaths = new ArrayList<>();
        CommonUtil.appendList(hostsFilesPaths, AppContext.extractLookupConfigResourcePaths("hosts"));
        CommonUtil.appendList(hostsFilesPaths, AppContext.extractLookupConfigResourcePaths("vdns.hosts"));
        String[] customHostsFiles = properties.getHostsPaths();
        if (customHostsFiles != null && customHostsFiles.length > 0) {
            CommonUtil.appendList(hostsFilesPaths, Arrays.asList(customHostsFiles));
        }

        // 过滤不存在的文件
        hostsFilesPaths = FileUtil.filterNotExistsFiles(hostsFilesPaths);

        HostsFilesDnsResolver hostsFilesDnsResolver = new HostsFilesDnsResolver(hostsFilesPaths, properties.isIgnoredUnknownFile(), properties.getReloadIntervalMillis());
        SystemHostsFileDnsResolver systemHostsFileDnsResolver = new SystemHostsFileDnsResolver();
        CompositeDnsResolver compositeDnsResolver = new CompositeDnsResolver(Arrays.asList(systemHostsFileDnsResolver, hostsFilesDnsResolver));

        // hook
        VirtualDnsUtil.hook(compositeDnsResolver);
    }
}
