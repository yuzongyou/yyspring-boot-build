package com.duowan.common.dns;

import com.duowan.common.dns.util.OsUtil;
import sun.net.util.IPAddressUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/7 9:39
 */
public class MapDnsResolver implements DnsResolver {

    /**
     * 域名 to InetAddress 数组 MAP
     */
    private Map<String, InetAddress[]> domainIpsMap = new HashMap<String, InetAddress[]>();


    @Override
    public InetAddress[] resolve(String host) throws UnknownHostException {
        InetAddress[] addresses = domainIpsMap.get(host);

        if (null == addresses || addresses.length < 1) {
            throw new UnknownHostException(host);
        }

        return addresses;
    }


    /**
     * 设置自定义的DNS，每一行是一个域名ip对应关系： domain ip
     * 设置的含义是： 会把之前的自定义的DNS都清理，然后以本次输入的为主
     * 一个域名可以对应多个IP
     *
     * @param ipDomainLines 域名-IP 列表
     * @return 返回 域名 to IP 列表
     */
    public Map<String, List<String>> set(List<String> ipDomainLines) {

        // 清理缓存，同时清理自定义DNS域名
        clear();

        Map<String, List<String>> domainIpMap = buildDnsMapByIpDomainLines(ipDomainLines, true);

        if (!domainIpMap.isEmpty()) {

            Map<String, InetAddress[]> map = buildDnsInetAddressMap(domainIpMap);

            domainIpsMap.clear();
            if (!map.isEmpty()) {
                domainIpsMap = map;
            }
        }

        return domainIpMap;
    }

    /**
     * 添加自定义的DNS，每一行是一个域名ip对应关系： domain ip
     * 一个域名可以对应多个IP
     * 语义： 如果历史存在的，会直接覆盖
     *
     * @param domainIpLines 域名-IP 列表
     * @return 返回 域名 to IP 列表
     */
    public Map<String, List<String>> add(List<String> domainIpLines) {

        return add(buildDnsMapByIpDomainLines(domainIpLines, true));
    }

    /**
     * 更新自定义的DNS，该域名的历史DNS会清理掉
     *
     * @param domain 域名
     * @param ips    ip 列表
     */
    public void add(String domain, String... ips) {
        add(domain, Arrays.asList(ips));
    }

    /**
     * 更新自定义的DNS，该域名的历史DNS会清理掉
     *
     * @param domain 域名
     * @param ips    ip 集合
     */
    public void add(String domain, Collection<String> ips) {
        if (isBlank(domain)) {
            throw new RuntimeException("域名不能为空！");
        }

        Map<String, List<String>> domainIpMap = new HashMap<String, List<String>>(1);
        List<String> ipList = new ArrayList<String>();

        for (String ip : ips) {
            if (!isBlank(ip) || isValidIp(ip)) {
                ipList.add(ip);
            }
        }

        if (ipList.isEmpty()) {
            throw new RuntimeException("没有指定[" + domain + "] 对应的解析IP");
        }

        domainIpMap.put(domain, ipList);

        add(domainIpMap);
    }

    /**
     * 删除自定义域名的 DNS
     *
     * @param domain 域名
     */
    public void remove(String domain) {
        remove(new ArrayList<String>(Collections.singletonList(domain)));
    }

    /**
     * 移除域名列表
     *
     * @param domains 要移除的域名列表
     */
    public void remove(Collection<String> domains) {

        if (null != domains && !domains.isEmpty()) {
            for (String domain : domains) {
                domainIpsMap.remove(domain);
            }
        }
    }

    /**
     * 清空所有的自定义 DNS
     */
    public void clear() {
        domainIpsMap = new HashMap<String, InetAddress[]>();
    }

    /**
     * 是否是自定义的 DNS 解析， 开启了自定义dns并且包含这个域名才返回true
     *
     * @param domain 要检查的域名
     * @return true 表示启用了自定义dns并且该域名是自定义的域名
     */
    public boolean isVirtualDns(String domain) {
        return !isLocalHosts(domain) && contains(domain);
    }

    /**
     * 是否包含指定的自定义域名DNS
     *
     * @param domain 要检查的域名
     * @return 返回是否是自定义的DNS域名
     */
    public boolean contains(String domain) {
        return domainIpsMap.containsKey(domain);
    }

    /**
     * 本地hosts的域名映射MAP， 域名 to IP 列表
     */
    private Map<String, List<String>> LOCAL_HOSTS_MAP = reloadLocalHosts();

    /**
     * 每隔 10 秒重新刷新本地 host
     */
    private int localHostReloadInterval = 10;

    public int getLocalHostReloadInterval() {
        return localHostReloadInterval < 1 ? 10 : localHostReloadInterval;
    }

    /**
     * 设置刷新本地 host的时间间隔
     *
     * @param localHostReloadInterval 单位是 秒
     */
    public void setLocalHostReloadInterval(int localHostReloadInterval) {
        if (localHostReloadInterval > 0) {
            this.localHostReloadInterval = localHostReloadInterval;
        }
    }

    /**
     * 调度执行本地hosts刷新
     */
    private Thread scheduleReloadLocalHost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(getLocalHostReloadInterval() * 1000);
                        reloadLocalHosts();
                    } catch (Exception ignored) {
                    }
                }
            }
        });

        thread.start();
        return thread;
    }

    /**
     * 是否是系统本身的host， 通过检查系统的host配置文件来判断
     *
     * @param host 主机IP或者域名
     * @return true - 是， false - 否
     */
    public boolean isLocalHosts(String host) {

        if (null == LOCAL_HOSTS_MAP) {
            return false;
        }
        return LOCAL_HOSTS_MAP.containsKey(host);
    }

    /**
     * 返回 呢没动 Hosts 的域名 to IP 列表映射
     *
     * @return 返回域名 to IP 列表映射
     */
    public Map<String, List<String>> getLocalHostsMap() {
        return Collections.unmodifiableMap(LOCAL_HOSTS_MAP);
    }

    /**
     * 重新加载本地hosts
     * windows:   C:/windows/System32/drivers/etc/hosts
     * 非windows:  /etc/hosts
     *
     * @return 返回域名IP列表MAP
     */
    public Map<String, List<String>> reloadLocalHosts() {

        String hostsFilePath = null;

        if (OsUtil.isWindows()) {
            // Windows 操作系统，加载路径： C:/windows/System32/drivers/etc/hosts 的配置
            hostsFilePath = "C:/windows/System32/drivers/etc/hosts";
        } else {
            // 非 Windows 操作系统，加载路径: /etc/hosts
            hostsFilePath = "/etc/hosts";
        }

        // 从标准的 hosts 文件中加载域名-IP解析
        Map<String, List<String>> domainIpMap = loadDnsFromStdHostsFile(hostsFilePath);
        if (null == domainIpMap) {
            domainIpMap = new HashMap<String, List<String>>(0);
        }
        LOCAL_HOSTS_MAP = domainIpMap;

        return getLocalHostsMap();
    }

    /**
     * 获取制定主机地址的IP
     *
     * @param host 主机地址（域名或IP）
     * @return 返回IP
     */
    public static String getIpByHost(String host) {
        try {
            return InetAddress.getByName(host).getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    /**
     * 获取自定义的IP解析
     *
     * @param host 主机地址
     * @return 返回自定义域名解析的IP地址
     */
    public String getVirtualIpByHost(String host) {
        if (domainIpsMap != null && domainIpsMap.containsKey(host)) {
            InetAddress[] inetAddresses = domainIpsMap.get(host);
            if (inetAddresses != null && inetAddresses.length > 0) {

                for (InetAddress inetAddress : inetAddresses) {
                    if (inetAddress != null) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取本地HOST配置的域名解析IP
     *
     * @param host 主机地址
     * @return 返回本地host配置
     */
    public String getIpByLocalhost(String host) {
        if (LOCAL_HOSTS_MAP != null && LOCAL_HOSTS_MAP.containsKey(host)) {

            List<String> ips = LOCAL_HOSTS_MAP.get(host);
            if (null != ips && !ips.isEmpty()) {
                for (String ip : ips) {
                    if (!isBlank(ip)) {
                        return ip;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 加载hosts文件标准格式的域名IP映射
     *
     * @param hostsFormatFilePath host 标准格式的配置文件，即 IP DOMAIN 格式的
     * @return 返回域名 to IP 列表
     */
    private Map<String, List<String>> loadDnsFromStdHostsFile(String hostsFormatFilePath) {

        if (isBlank(hostsFormatFilePath)) {
            return null;
        }

        List<String> ipDomainList = new ArrayList<String>();
        BufferedReader reader = null;

        try {

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(hostsFormatFilePath)));

            String line = null;

            while ((line = reader.readLine()) != null) {

                ipDomainList.add(line.trim());
            }

        } catch (Exception ignored) {
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }

        return buildDnsMapByIpDomainLines(ipDomainList, false);
    }

    protected Map<String, List<String>> add(Map<String, List<String>> domainIpMap) {
        if (null != domainIpMap && !domainIpMap.isEmpty()) {

            Map<String, InetAddress[]> map = buildDnsInetAddressMap(domainIpMap);

            if (!map.isEmpty()) {
                domainIpsMap.putAll(map);
            }

        }

        return domainIpMap;
    }

    protected boolean isBlank(String value) {
        return null == value || "".equals(value.trim());
    }

    protected boolean isAnyBlank(String... strings) {

        if (null == strings || strings.length < 1) {
            return true;
        }

        for (String string : strings) {
            if (isBlank(string)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 构造 domain to InetAddress[] Map
     *
     * @param domainIpMap 域名 to ip 列表 MAP
     * @return 返回 域名 to InetAddress 数组
     */
    private Map<String, InetAddress[]> buildDnsInetAddressMap(Map<String, List<String>> domainIpMap) {
        Map<String, InetAddress[]> map = new HashMap<String, InetAddress[]>();

        for (Map.Entry<String, List<String>> entry : domainIpMap.entrySet()) {

            if (isLocalHosts(entry.getKey())) {
                continue;
            }

            List<InetAddress> inetAddressList = new ArrayList<InetAddress>();

            for (String ip : entry.getValue()) {
                try {
                    InetAddress inetAddress = InetAddress.getByAddress(ip, InetAddress.getByName(ip).getAddress());
                    inetAddressList.add(inetAddress);
                } catch (UnknownHostException ignored) {
                }
            }

            if (!inetAddressList.isEmpty()) {
                map.put(entry.getKey(), inetAddressList.toArray(new InetAddress[inetAddressList.size()]));
            }
        }
        return map;
    }

    /**
     * 构造 DNS MAP, 传入参数 IP DOMAIN
     *
     * @param ipDomainLines    配置行列表，每一行要求输入的文件格式是一行表是一个 IP 域名， 可以使用 # 注释
     * @param excludeLocalHost 是否要排除本地Host
     * @return 返回 域名IP列表
     */
    public Map<String, List<String>> buildDnsMapByIpDomainLines(List<String> ipDomainLines, boolean excludeLocalHost) {
        Map<String, List<String>> domainIpMap = new HashMap<String, List<String>>();
        if (null == ipDomainLines || ipDomainLines.isEmpty()) {
            return domainIpMap;
        }

        for (String line : ipDomainLines) {
            if (isBlank(line)) {
                continue;
            }

            String value = line.trim();
            if (value.startsWith("#")) {
                // 过滤掉注释行
                continue;
            }

            // 去掉注释行
            value = value.replaceAll("\\s*#.*$", "");

            String[] array = value.split("\\s+");
            if (array.length < 2) {
                continue;
            }
            String ip = array[0];

            for (int i = 1; i < array.length; ++i) {
                String domain = array[i];
                if (isBlank(domain) || (excludeLocalHost && isLocalHosts(domain))) {
                    continue;
                }
                List<String> set = domainIpMap.get(domain);
                if (set == null) {
                    set = new ArrayList<String>();
                    domainIpMap.put(domain, set);
                    set.add(ip);
                } else {
                    // 去重
                    if (!set.contains(ip)) {
                        set.add(ip);
                    }
                }
            }
        }

        return domainIpMap;
    }

    /**
     * 检查IP地址格式是否合法
     *
     * @param ip ip地址
     * @return true 表示地址合法，false 标识不合法
     */
    protected boolean isValidIp(String ip) {
        return IPAddressUtil.isIPv4LiteralAddress(ip) || IPAddressUtil.isIPv6LiteralAddress(ip);
    }
}
