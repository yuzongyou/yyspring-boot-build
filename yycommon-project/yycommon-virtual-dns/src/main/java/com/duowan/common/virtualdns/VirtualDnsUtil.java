package com.duowan.common.virtualdns;

import com.duowan.common.dns.exception.DnsException;
import com.duowan.common.dns.util.InetAddressUtil;
import com.duowan.common.dns.util.OsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.net.spi.nameservice.NameService;
import sun.net.util.IPAddressUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Arvin
 */
public class VirtualDnsUtil {

    private VirtualDnsUtil() {
        throw new IllegalStateException("Utility Class");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(VirtualDnsUtil.class);

    /**
     * 默认是没有启用自定义的DNS解析
     */
    private static volatile boolean enabled = false;

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    /**
     * 域名 to InetAddress 数组 MAP
     */
    private static Map<String, InetAddress[]> domainIpsMap = new HashMap<>();

    /**
     * 自定义的 NameService， 实现自己的 DNS 解析
     */
    private static final NameService VIRTUAL_NAME_SERVICE = new NameService() {
        @Override
        public InetAddress[] lookupAllHostAddr(String host) throws UnknownHostException {
            if (!isEnabled()) {
                // 未启用自定义 DNS， 直接抛出异常，让别的 NameService 解析
                throw new UnknownHostException(host);
            }

            // 非 host 的才会解析
            if (domainIpsMap != null && !isLocalHosts(host)) {
                InetAddress[] addresses = domainIpsMap.get(host);
                if (addresses == null || addresses.length < 1 || addresses[0] == null) {
                    throw new UnknownHostException(host);
                }
                return addresses;
            }

            throw new UnknownHostException(host);
        }

        @Override
        public String getHostByAddr(byte[] bytes) throws UnknownHostException {
            // 不进行解析，让别的NameService进行解析
            throw new UnknownHostException();
        }
    };

    /**
     * 是否启用了自定义解析
     *
     * @return true - 是， false - 否
     */
    public static boolean isEnabled() {
        return enabled;
    }

    /**
     * 启用自定义的 DNS
     */
    public static void enabled() {
        enabled = true;
        // 将给定的 NameService 设置为第一个生效的 NameService
        InetAddressUtil.addAsFirst(VIRTUAL_NAME_SERVICE, null);

        // 调度清理hosts
        scheduleReloadLocalHost();
    }

    /**
     * 禁用自定义的 DNS
     */
    public static void disabled() {
        enabled = false;
        // 移除指定的 NameService
        InetAddressUtil.unwrapNameService();
    }

    /**
     * 设置自定义的DNS，每一行是一个域名ip对应关系： domain ip
     * 设置的含义是： 会把之前的自定义的DNS都清理，然后以本次输入的为主
     * 一个域名可以对应多个IP
     *
     * @param ipDomainLines 域名-IP 列表
     * @return 返回 域名 to IP 列表
     */
    public static Map<String, List<String>> set(List<String> ipDomainLines) {

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
    public static Map<String, List<String>> add(List<String> domainIpLines) {

        return add(buildDnsMapByIpDomainLines(domainIpLines, true));
    }

    /**
     * 更新自定义的DNS，该域名的历史DNS会清理掉
     *
     * @param domain 域名
     * @param ips    ip 列表
     */
    public static void add(String domain, String... ips) {
        add(domain, Arrays.asList(ips));
    }

    /**
     * 更新自定义的DNS，该域名的历史DNS会清理掉
     *
     * @param domain 域名
     * @param ips    ip 集合
     */
    public static void add(String domain, Collection<String> ips) {
        if (isBlank(domain)) {
            throw new DnsException("域名不能为空！");
        }

        Map<String, List<String>> domainIpMap = new HashMap<>(1);
        List<String> ipList = new ArrayList<>();

        for (String ip : ips) {
            if (!isBlank(ip) || isValidIp(ip)) {
                ipList.add(ip);
            }
        }

        if (ipList.isEmpty()) {
            throw new DnsException("没有指定[" + domain + "] 对应的解析IP");
        }

        domainIpMap.put(domain, ipList);

        add(domainIpMap);
    }

    /**
     * 删除自定义域名的 DNS
     *
     * @param domain 域名
     */
    public static void remove(String domain) {
        remove(new ArrayList<String>(Collections.singletonList(domain)));
    }

    /**
     * 移除域名列表
     *
     * @param domains 要移除的域名列表
     */
    public static void remove(Collection<String> domains) {

        if (null != domains && !domains.isEmpty()) {
            for (String domain : domains) {
                domainIpsMap.remove(domain);
            }
        }
    }

    /**
     * 清空所有的自定义 DNS
     */
    public static void clear() {
        domainIpsMap = new HashMap<>();
    }

    /**
     * 是否是自定义的 DNS 解析， 开启了自定义dns并且包含这个域名才返回true
     *
     * @param domain 要检查的域名
     * @return true 表示启用了自定义dns并且该域名是自定义的域名
     */
    public static boolean isVirtualDns(String domain) {
        return isEnabled() && !isLocalHosts(domain) && contains(domain);
    }

    /**
     * 是否包含指定的自定义域名DNS
     *
     * @param domain 要检查的域名
     * @return 返回是否是自定义的DNS域名
     */
    public static boolean contains(String domain) {
        return domainIpsMap.containsKey(domain);
    }

    /**
     * 本地hosts的域名映射MAP， 域名 to IP 列表
     */
    private static Map<String, List<String>> localHostsMap = reloadLocalHosts();

    /**
     * 每隔 10 秒重新刷新本地 host
     */
    private static int localHostReloadInterval = 10;

    public static int getLocalHostReloadInterval() {
        return localHostReloadInterval < 1 ? 10 : localHostReloadInterval;
    }

    /**
     * 设置刷新本地 host的时间间隔
     *
     * @param localHostReloadInterval 单位是 秒
     */
    public static void setLocalHostReloadInterval(int localHostReloadInterval) {
        if (localHostReloadInterval > 0) {
            VirtualDnsUtil.localHostReloadInterval = localHostReloadInterval;
        }
    }

    static {
        // 调度刷新 local host
        scheduleReloadLocalHost();
    }

    /**
     * 调度执行本地hosts刷新
     */
    private static void scheduleReloadLocalHost() {
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            try {
                if (isEnabled()) {
                    reloadLocalHosts();
                }
            } catch (Exception e) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(e.getMessage(), e);
                }
            }
        }, 0L, getLocalHostReloadInterval() * 1000L, TimeUnit.MILLISECONDS);
    }

    /**
     * 是否是系统本身的host， 通过检查系统的host配置文件来判断
     *
     * @param host 主机IP或者域名
     * @return true - 是， false - 否
     */
    public static boolean isLocalHosts(String host) {

        if (null == localHostsMap) {
            return false;
        }
        return localHostsMap.containsKey(host);
    }

    /**
     * 返回 呢没动 Hosts 的域名 to IP 列表映射
     *
     * @return 返回域名 to IP 列表映射
     */
    public static Map<String, List<String>> getLocalHostsMap() {
        return Collections.unmodifiableMap(localHostsMap);
    }

    private static final String WINDOWS_HOST_FILE_PATH = String.format("C:%swindows%sSystem32%sdrivers%setc%shosts", File.separator, File.separator, File.separator, File.separator, File.separator);
    private static final String LINUX_HOST_FILE_PATH = File.pathSeparator + "etc " + File.pathSeparator + "hosts";

    /**
     * 重新加载本地hosts
     * windows:   C:/windows/System32/drivers/etc/hosts
     * 非windows:  /etc/hosts
     *
     * @return 返回域名IP列表MAP
     */
    public static Map<String, List<String>> reloadLocalHosts() {

        String hostsFilePath = null;

        if (OsUtil.isWindows()) {
            // Windows 操作系统，加载路径： C:/windows/System32/drivers/etc/hosts 的配置
            hostsFilePath = WINDOWS_HOST_FILE_PATH;
        } else {
            // 非 Windows 操作系统，加载路径: /etc/hosts
            hostsFilePath = LINUX_HOST_FILE_PATH;
        }

        // 从标准的 hosts 文件中加载域名-IP解析
        Map<String, List<String>> domainIpMap = loadDnsFromStdHostsFile(hostsFilePath);
        if (null == domainIpMap) {
            domainIpMap = new HashMap<>(0);
        }
        localHostsMap = domainIpMap;

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
    public static String getVirtualIpByHost(String host) {
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
    public static String getIpByLocalhost(String host) {
        if (localHostsMap != null && localHostsMap.containsKey(host)) {

            List<String> ips = localHostsMap.get(host);
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
    private static Map<String, List<String>> loadDnsFromStdHostsFile(String hostsFormatFilePath) {

        if (isBlank(hostsFormatFilePath)) {
            return null;
        }

        List<String> ipDomainList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(hostsFormatFilePath)))) {

            String line = null;

            while ((line = reader.readLine()) != null) {

                ipDomainList.add(line.trim());
            }

        } catch (Exception e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e.getMessage(), e);
            }
        }

        return buildDnsMapByIpDomainLines(ipDomainList, false);
    }

    protected static Map<String, List<String>> add(Map<String, List<String>> domainIpMap) {
        if (null != domainIpMap && !domainIpMap.isEmpty()) {

            Map<String, InetAddress[]> map = buildDnsInetAddressMap(domainIpMap);

            if (!map.isEmpty()) {
                domainIpsMap.putAll(map);
            }

        }

        return domainIpMap;
    }

    protected static boolean isBlank(String value) {
        return null == value || "".equals(value.trim());
    }

    protected static boolean isAnyBlank(String... strings) {

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
    private static Map<String, InetAddress[]> buildDnsInetAddressMap(Map<String, List<String>> domainIpMap) {
        Map<String, InetAddress[]> map = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : domainIpMap.entrySet()) {

            if (isLocalHosts(entry.getKey())) {
                continue;
            }

            List<InetAddress> inetAddressList = new ArrayList<>();

            for (String ip : entry.getValue()) {
                try {
                    InetAddress inetAddress = InetAddress.getByAddress(ip, InetAddress.getByName(ip).getAddress());
                    inetAddressList.add(inetAddress);
                } catch (UnknownHostException e) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(e.getMessage(), e);
                    }
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
    public static Map<String, List<String>> buildDnsMapByIpDomainLines(List<String> ipDomainLines, boolean excludeLocalHost) {
        Map<String, List<String>> domainIpMap = new HashMap<>();
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
                    set = new ArrayList<>();
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
    protected static boolean isValidIp(String ip) {
        return IPAddressUtil.isIPv4LiteralAddress(ip) || IPAddressUtil.isIPv6LiteralAddress(ip);
    }

}
