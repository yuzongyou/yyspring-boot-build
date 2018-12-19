package com.duowan.common.dns.hook;

import com.duowan.common.dns.MapDnsResolver;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/7 10:29
 */
public class Jdk6InetAddressHookTest {

    @Test
    public void hook() throws UnknownHostException {

        MapDnsResolver resolver = new MapDnsResolver();
        resolver.add("www.baidu.com", "127.0.0.1");

        Jdk6InetAddressHook addressHook = new Jdk6InetAddressHook(resolver, null);
        addressHook.hook();

        InetAddress inetAddress = InetAddress.getByName("www.baidu.com");
        System.out.println(inetAddress.getHostAddress());
    }

}