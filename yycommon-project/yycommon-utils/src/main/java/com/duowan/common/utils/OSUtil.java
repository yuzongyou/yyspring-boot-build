package com.duowan.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 功能：IP 工具类
 *
 * @author zengyuan on 2018/4/23.
 */
public class OSUtil {

    private OSUtil() {
        throw new IllegalStateException("Utility Class");
    }

    private static final String OS_NAME_WINDOWS = "windows";

    /**
     * IP的最后一个数字
     */
    private static String lastIpNum = null;

    /**
     * 本进程ID
     */
    private static String processPid = null;

    /**
     * 本地IP
     */
    private static String localIp = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(OSUtil.class);

    /**
     * 判断是否为windows
     *
     * @return 是否
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains(OS_NAME_WINDOWS)) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取本机IP
     *
     * @return 是否
     */
    public static String getLocalIp() {
        if (localIp == null) {
            localIp = doGetLocalIp();
        }
        return localIp;
    }

    /**
     * 获取本机IP
     *
     * @return 返回本机IP
     */
    private static String doGetLocalIp() {
        try {
            if (isWindowsOS()) {
                return InetAddress.getLocalHost().getHostAddress();
            } else {
                // 本地IP，如果没有配置外网IP则返回它
                String localIp = null;
                // 外网IP
                String netIp = null;

                Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
                //获取所有子接口IP地址
                Set<String> subInterfaceIps = getSubInterfaceAddresses();

                InetAddress ip = null;
                // 是否找到外网IP
                boolean found = false;

                String ipAddr = null;

                while (netInterfaces.hasMoreElements() && !found) {
                    NetworkInterface ni = netInterfaces.nextElement();
                    Enumeration<InetAddress> address = ni.getInetAddresses();

                    while (address.hasMoreElements()) {
                        ip = address.nextElement();
                        ipAddr = ip.getHostAddress();

                        if (!ip.isSiteLocalAddress()
                                && !ip.isLoopbackAddress()
                                && !subInterfaceIps.contains(ipAddr) // 不是子接口的IP
                                && ipAddr.indexOf(':') == -1) {
                            // 外网IP
                            netIp = ipAddr;
                            found = true;
                            break;
                        } else if (ip.isSiteLocalAddress()
                                && !ip.isLoopbackAddress()
                                && ipAddr.indexOf(':') == -1) {
                            // 内网IP
                            localIp = ipAddr;
                        }
                    }
                }

                LOGGER.info("netIp: {}, localIp: {}", netIp, localIp);
                if (netIp != null && !"".equals(netIp)) {
                    return netIp;
                } else {
                    return localIp;
                }
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取网卡子接口（虚拟接口，例如eth0:1、eth0:cnc_246等）的IP地址
     *
     * @return 返回网卡子接口
     * @throws SocketException 异常
     */
    private static Set<String> getSubInterfaceAddresses() throws SocketException {
        //所有网络接口
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        Set<String> subInterfaceAddresses = new HashSet<>();

        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();

            //子接口
            Enumeration<NetworkInterface> subInterfaces = networkInterface.getSubInterfaces();

            while (subInterfaces.hasMoreElements()) {
                NetworkInterface subInterface = subInterfaces.nextElement();

                //子接口绑定的IP
                Enumeration<InetAddress> inetAddresses = subInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    subInterfaceAddresses.add(inetAddresses.nextElement().getHostAddress());
                }
            }
        }
        return subInterfaceAddresses;
    }

    /**
     * 获得IP的最后一个数字
     *
     * @return 返回IP最后一个数字
     */
    public static String getIpLastNumber() {
        if (lastIpNum == null) {
            return doGetIpLastNumber();
        }
        return lastIpNum;
    }

    private static String doGetIpLastNumber() {
        String ip = getLocalIp();
        if (ip == null) {
            ip = "127.0.0.1";
        }
        int pos = ip.lastIndexOf('.');
        String num;
        if (pos >= 0) {
            num = ip.substring(pos + 1);
        } else {
            // maybe ipv6
            int positiveHashCode = ip.hashCode() < 0 ? ip.hashCode() + -1 : ip.hashCode();
            num = String.valueOf(positiveHashCode % 1000L);
        }
        if (num.length() == 1) {
            num = "00" + num;
        } else if (num.length() == 2) {
            num = "0" + num;
        }
        lastIpNum = num;
        return num;
    }

    /**
     * 获得java进程id
     *
     * @return java进程id
     */
    public static String getPid() {
        if (processPid == null) {
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            String processName = runtimeMXBean.getName();
            String pid = null;
            if (processName.indexOf('@') != -1) {
                pid = processName.substring(0, processName.indexOf('@'));
            }

            if (pid == null) {
                LOGGER.warn("Cannot get pid, random one ...");
                Random rand = new Random(System.currentTimeMillis());
                pid = rand.nextInt(65536) + "";
            }
            if (pid.length() > 5) {
                pid = pid.substring(0, 5);
            }
            processPid = pid;
            LOGGER.info("pid = {}", processPid);
        }
        return processPid;
    }
}
