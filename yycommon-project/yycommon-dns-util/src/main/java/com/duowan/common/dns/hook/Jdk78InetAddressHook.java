package com.duowan.common.dns.hook;

import com.duowan.common.dns.DnsResolver;
import com.duowan.common.dns.util.NameServiceInterceptor;
import com.duowan.common.dns.util.NameServiceListWrapper;
import com.duowan.common.dns.util.ReflectUtil;
import sun.net.spi.nameservice.NameService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/12/7 10:16
 */
public class Jdk78InetAddressHook extends AbstractInetAddressHook {

    /**
     * JDK 1.7, 1.8 InetAddress 对象默认的NameService对象列表
     */
    private final List<NameService> originNameServices;

    /**
     * 自定义的 NameService， 实现自己的 DNS 解析
     */
    private final NameService virtualNameService;

    /**
     * Hook 列表
     **/
    private final List<NameService> hookNameServices;


    /**
     * JDK 1.7 是使用多个 NameService 进行获取DNS解析
     */
    private List<NameService> lookupCurrentNameServices() {

        try {

            return ReflectUtil.getFieldInstance(InetAddress.class, List.class, "nameServices");

        } catch (Exception ignored) {
        }

        return null;
    }

    public Jdk78InetAddressHook(DnsResolver customDnsResolver, NameServiceInterceptor interceptor) {
        super(customDnsResolver);
        this.originNameServices = lookupCurrentNameServices();
        this.virtualNameService = createVirtualNameService();
        this.hookNameServices = createHookNameServices(interceptor);

    }

    private List<NameService> createHookNameServices(NameServiceInterceptor interceptor) {

        List<NameService> nsList = new ArrayList<NameService>(2);
        nsList.add(new NameServiceListWrapper(interceptor, Collections.singletonList(virtualNameService)));
        if (null != originNameServices && !originNameServices.isEmpty()) {
            nsList.addAll(originNameServices);
        }

        return nsList;
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


    @Override
    public void hook() {
        if (!hookEnabled) {
            List<NameService> nameService = lookupCurrentNameServices();
            if (!originNameServices.equals(nameService)) {
                return;
            }

            try {
                ReflectUtil.setFieldInstance(InetAddress.class, "nameServices", hookNameServices);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setHookEnabled(true);
    }

    @Override
    public void unhook() {
        try {
            ReflectUtil.setFieldInstance(InetAddress.class, "nameServices", originNameServices);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setHookEnabled(false);
    }
}
