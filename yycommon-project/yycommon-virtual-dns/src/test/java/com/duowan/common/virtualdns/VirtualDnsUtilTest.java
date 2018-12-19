package com.duowan.common.virtualdns;

import org.junit.Before;
import org.junit.Test;
import sun.net.InetAddressCachePolicy;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 17:07
 */
public class VirtualDnsUtilTest {

    /**
     * 设置 DNS 缓存时间
     *
     * @param seconds 缓存时间，单位是秒, 0 表示不缓存，-1表示永久缓存， 正数表示缓存的秒数
     */
    public static void setDnsCacheTime(int seconds) throws Exception {

        int expire = seconds;
        if (seconds < 0) {
            expire = InetAddressCachePolicy.FOREVER;
        }

        Class<InetAddressCachePolicy> clazz = InetAddressCachePolicy.class;
        try {
            Field field = clazz.getDeclaredField("cachePolicy");
            field.setAccessible(true);
            field.set(clazz, expire);
            field.setAccessible(false);
        } catch (Exception ignored) {
        }

        try {
            Field setField = clazz.getDeclaredField("set");
            setField.setAccessible(true);
            setField.set(clazz, true);
            setField.setAccessible(false);
        } catch (Exception ignored) {
        }
    }

    @Before
    public void prepare() throws Exception {

        // 设置为永不缓存
        setDnsCacheTime(InetAddressCachePolicy.NEVER);
    }

    @Test
    public void testIsEnabled() throws Exception {
        VirtualDnsUtil.disabled();

        assertFalse(VirtualDnsUtil.isEnabled());

        VirtualDnsUtil.enabled();
        assertTrue(VirtualDnsUtil.isEnabled());
    }

    @Test
    public void testEnabled() throws Exception {
        VirtualDnsUtil.enabled();
        assertTrue(VirtualDnsUtil.isEnabled());
    }

    @Test
    public void testDisabled() throws Exception {
        VirtualDnsUtil.disabled();

        assertFalse(VirtualDnsUtil.isEnabled());
    }

    @Test
    public void testSet() throws Exception {
        VirtualDnsUtil.set(Collections.singletonList("127.0.0.1 www.hello.com"));

        VirtualDnsUtil.enabled();

        String address = InetAddress.getByName("www.hello.com").getHostAddress();

        assertEquals("127.0.0.1", address);
    }

    @Test
    public void testAdd() throws Exception {
        VirtualDnsUtil.enabled();
        VirtualDnsUtil.add("www.hello.com", "127.0.0.1");

        String address = InetAddress.getByName("www.hello.com").getHostAddress();

        assertEquals("127.0.0.1", address);
    }

    @Test
    public void testAdd1() throws Exception {
        VirtualDnsUtil.enabled();
        VirtualDnsUtil.add(Collections.singletonMap("www.hello.com", Collections.singletonList("127.0.0.1")));

        String address = InetAddress.getByName("www.hello.com").getHostAddress();

        assertEquals("127.0.0.1", address);
    }

    @Test
    public void testAdd2() throws Exception {
        VirtualDnsUtil.enabled();
        VirtualDnsUtil.add(Collections.singletonList("127.0.0.1 www.hello.com"));

        String address = InetAddress.getByName("www.hello.com").getHostAddress();

        assertEquals("127.0.0.1", address);
    }

    @Test
    public void testRemove() throws Exception {

        VirtualDnsUtil.enabled();
        VirtualDnsUtil.add(Collections.singletonList("127.0.0.1 www.hello.com"));

        String address = InetAddress.getByName("www.hello.com").getHostAddress();

        assertEquals("127.0.0.1", address);

        VirtualDnsUtil.remove("www.hello.com");

        address = InetAddress.getByName("www.hello.com").getHostAddress();
        assertNotEquals("127.0.0.1", address);
    }

    @Test
    public void testRemove1() throws Exception {
        VirtualDnsUtil.enabled();
        VirtualDnsUtil.add(Collections.singletonList("127.0.0.1 www.hello.com"));

        String address = InetAddress.getByName("www.hello.com").getHostAddress();

        assertEquals("127.0.0.1", address);

        VirtualDnsUtil.remove(Collections.singleton("www.hello.com"));

        address = InetAddress.getByName("www.hello.com").getHostAddress();
        assertNotEquals("127.0.0.1", address);
    }

    @Test
    public void testClear() throws Exception {
        VirtualDnsUtil.enabled();
        VirtualDnsUtil.add(Collections.singletonList("127.0.0.1 www.hello.com"));

        String address = InetAddress.getByName("www.hello.com").getHostAddress();

        assertEquals("127.0.0.1", address);

        VirtualDnsUtil.clear();

        address = InetAddress.getByName("www.hello.com").getHostAddress();
        assertNotEquals("127.0.0.1", address);
    }

    @Test
    public void testClearCache() throws Exception {
        VirtualDnsUtil.enabled();
        VirtualDnsUtil.add(Collections.singletonList("127.0.0.1 www.hello.com"));

        String address = InetAddress.getByName("www.hello.com").getHostAddress();

        assertEquals("127.0.0.1", address);

        VirtualDnsUtil.add(Collections.singletonList("127.0.0.2 www.hello.com"));

        // 清理缓存之后
        address = InetAddress.getByName("www.hello.com").getHostAddress();
        assertEquals("127.0.0.2", address);
    }

    @Test
    public void testIsVirtualDns() throws Exception {

        VirtualDnsUtil.clear();
        VirtualDnsUtil.disabled();

        assertFalse(VirtualDnsUtil.isVirtualDns("www.hello.com"));

        VirtualDnsUtil.add("www.hello.com", "127.0.0.1");

        assertFalse(VirtualDnsUtil.isVirtualDns("www.hello.com"));

        VirtualDnsUtil.enabled();

        assertTrue(VirtualDnsUtil.isVirtualDns("www.hello.com"));

        VirtualDnsUtil.remove("www.hello.com");

        assertFalse(VirtualDnsUtil.isVirtualDns("www.hello.com"));


    }

    @Test
    public void testContains() throws Exception {

        VirtualDnsUtil.clear();
        VirtualDnsUtil.disabled();

        assertFalse(VirtualDnsUtil.contains("www.hello.com"));

        VirtualDnsUtil.add("www.hello.com", "127.0.0.1");

        assertTrue(VirtualDnsUtil.contains("www.hello.com"));

        VirtualDnsUtil.remove("www.hello.com");

        assertFalse(VirtualDnsUtil.contains("www.hello.com"));
    }

    @Test
    public void testIsLocalHosts() throws Exception {
        assertFalse(VirtualDnsUtil.isLocalHosts("www.hello.com"));
    }

    @Test
    public void testGetLocalHostsMap() throws Exception {
        Exception exception = null;
        try {
            VirtualDnsUtil.getLocalHostsMap().remove("www.hello.com");
        } catch (Exception e) {
            exception = e;
        }
        assertNotNull(exception);
    }
}