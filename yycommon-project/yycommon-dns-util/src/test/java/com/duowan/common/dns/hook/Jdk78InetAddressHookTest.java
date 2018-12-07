package com.duowan.common.dns.hook;

import com.duowan.common.dns.MapDnsResolver;
import com.duowan.common.dns.util.JdkUtil;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/7 10:29
 */
public class Jdk78InetAddressHookTest {

    @Test
    public void hook() throws UnknownHostException {

        System.out.println(JdkUtil.isIsJava6());
        System.out.println(JdkUtil.isIsJava7());
        System.out.println(JdkUtil.isIsJava8());
        System.out.println(JdkUtil.isIsJava9());
        System.out.println(JdkUtil.isIsJava10());
        System.out.println(JdkUtil.isIsJava11());

        MapDnsResolver resolver = new MapDnsResolver();
        resolver.add("www.baidu.com", "127.0.0.1");

        Jdk78InetAddressHook addressHook = new Jdk78InetAddressHook(resolver, null);
        addressHook.hook();

        InetAddress inetAddress = InetAddress.getByName("www.baidu.com");
        System.out.println(inetAddress.getHostAddress());
    }

}