package com.duowan.common.dns;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/7 9:48
 */
public class MapDnsResolverTest {

    @Test
    public void resolve() throws UnknownHostException {

        MapDnsResolver resolver = new MapDnsResolver();
        resolver.add("www.baidu.com", "127.0.0.1");

        InetAddress[] addresses = resolver.resolve("www.baidu.com");

        System.out.println(addresses);


    }
}