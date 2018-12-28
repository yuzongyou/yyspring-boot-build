package com.duowan.yyspringboot.autoconfigure.recachedns;

import com.duowan.common.dns.DnsInterceptor;
import com.duowan.common.dns.MapDnsResolver;
import com.duowan.common.recachedns.DnsCacheUtil;
import com.duowan.common.recachedns.Jdk678DnsCacheHook;
import com.duowan.common.vdns.VirtualDnsUtil;
import com.duowan.yyspringboot.autoconfigure.BaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 11:07
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ReCacheDnsAutoConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:/com/duowan/yyspringboot/autoconfigure/recachedns/application.properties")
public class ReCacheDnsAutoConfigurationTest extends BaseTest {

    @Test
    public void dnsReCache() throws Exception {

        String host = "www.hello.com";

        DnsCacheUtil.setDnsInterceptor(new DnsInterceptor() {

            ThreadLocal<Long> startNanoTime = new ThreadLocal<Long>();

            @Override
            public void before(String host) {
                startNanoTime.set(System.nanoTime());
            }

            @Override
            public void after(String host, InetAddress[] addresses, Exception exception) {
                long currentNanoTime = System.nanoTime();
                long takeTime = currentNanoTime - startNanoTime.get();
                System.out.println("Host: [" + host + "] = {" + addressesToString(addresses) + "} 解析耗时： " + takeTime + " 纳秒， 合计 " + (takeTime / 1000000f) + " 毫秒！");
            }
        });

        MapDnsResolver resolver = new MapDnsResolver();
        resolver.add(host, "127.0.0.1");

        VirtualDnsUtil.hook(resolver, null);

        Map<String, Object> cacheMap = Jdk678DnsCacheHook.getPositiveAddressCache();

        assertTrue(!cacheMap.containsKey(host));

        // trigger dns access
        triggerHostAccess(host);
        assertTrue(cacheMap.containsKey(host));

        // 使得缓存过期
        Thread.sleep(2100);

        assertNotNull(Jdk678DnsCacheHook.getAddressFromCache(host));

    }

    private void triggerHostAccess(String host) {
        try {
            InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    private String addressesToString(InetAddress[] addresses) {
        StringBuilder builder = new StringBuilder("[");

        if (addresses.length > 0) {
            for (InetAddress inetAddress : addresses) {
                builder.append(inetAddress.getHostAddress()).append(",");
            }
            builder.setLength(builder.length() - 1);
        }
        builder.append("]");


        return builder.toString();
    }

}