package com.duowan.common.dns.hook;

import com.duowan.common.dns.DnsResolver;
import com.duowan.common.dns.util.NameServiceInterceptor;
import com.duowan.common.dns.util.NameServiceListWrapper;
import com.duowan.common.dns.util.ReflectUtil;
import sun.net.spi.nameservice.NameService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Jdk6 Hook
 *
 * @author Arvin
 * @version 1.0
 * @since 2018/12/7 9:12
 */
public class Jdk6InetAddressHook extends AbstractInetAddressHook {

    /**
     * JDK 1.6 InetAddress 对象默认的NameService对象
     */
    private static final NameService ORIGIN_NAME_SERVICE = lookupCurrentNameService();

    /**
     * 自定义的 NameService， 实现自己的 DNS 解析
     */
    private final NameService virtualNameService;

    /**
     * Hook NameService
     **/
    private final NameService hookNameService;

    public Jdk6InetAddressHook(DnsResolver customDnsResolver, NameServiceInterceptor interceptor) {
        super(customDnsResolver);

        this.virtualNameService = createVirtualNameService();
        this.hookNameService = createHookNameService(interceptor);
    }

    private NameService createHookNameService(NameServiceInterceptor interceptor) {

        List<NameService> nsList = new ArrayList<NameService>(2);
        nsList.add(virtualNameService);
        nsList.add(ORIGIN_NAME_SERVICE);

        return new NameServiceListWrapper(interceptor, nsList);

    }

    private NameService createVirtualNameService() {
        return new NameService() {
            @Override
            public InetAddress[] lookupAllHostAddr(String host) throws UnknownHostException {
                if (!hookEnabled) {
                    // 未启用自定义 DNS， 直接抛出异常，让别的 NameService 解析
                    throw new UnknownHostException(host);
                }
                InetAddress[] addresses = customDnsResolver.resolve(host);
                if (null == addresses || addresses.length < 1) {
                    throw new UnknownHostException(host);
                }
                return addresses;
            }

            @Override
            public String getHostByAddr(byte[] bytes) throws UnknownHostException {
                // 不进行解析，让别的NameService进行解析
                throw new UnknownHostException();
            }
        };
    }

    /**
     * JDK 1.6 是使用单个 NameService 进行获取DNS解析
     */
    private static NameService lookupCurrentNameService() {

        try {

            return ReflectUtil.getFieldInstance(InetAddress.class, NameService.class, "nameService");

        } catch (Exception ignored) {
        }

        return null;
    }

    @Override
    public void hook() {
        if (!hookEnabled) {
            NameService nameService = lookupCurrentNameService();
            if (nameService instanceof NameServiceListWrapper) {
                return;
            }

            try {
                ReflectUtil.setFieldInstance(InetAddress.class, "nameService", hookNameService);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setHookEnabled(true);
    }

    @Override
    public void unhook() {
        try {
            ReflectUtil.setFieldInstance(InetAddress.class, "nameService", ORIGIN_NAME_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setHookEnabled(false);
    }
}
