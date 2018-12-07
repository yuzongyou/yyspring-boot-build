package com.duowan.common.dns.util;

import sun.net.spi.nameservice.NameService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author Arvin
 */
public class NameServiceListWrapper implements NameService {

    /**
     * 拦截器
     */
    private final NameServiceInterceptor interceptor;

    /**
     * Name Server 列表最大下标
     */
    private final int maxIndex;

    /**
     * 域名解析服务列表
     */
    private final List<NameService> nameServiceList;

    /**
     * 最后一个生效的 NameService
     */
    private final NameService lastNameService;

    public NameServiceListWrapper(NameServiceInterceptor interceptor, List<NameService> nameServiceList) {
        assert (null != nameServiceList && !nameServiceList.isEmpty());

        this.interceptor = interceptor;
        this.nameServiceList = nameServiceList;

        maxIndex = this.nameServiceList.size() - 1;
        lastNameService = this.nameServiceList.get(maxIndex);

    }


    @Override
    public InetAddress[] lookupAllHostAddr(String host) throws UnknownHostException {
        if (null != interceptor) {
            try {
                interceptor.before(host);
            } catch (Exception ignored) {
            }
        }


        Exception exception = null;
        InetAddress[] addresses = null;
        try {
            for (int i = 0; i < maxIndex; ++i) {

                NameService nameService = nameServiceList.get(i);
                try {
                    addresses = nameService.lookupAllHostAddr(host);
                    return addresses;
                } catch (UnknownHostException ignored) {
                }
            }

            addresses = lastNameService.lookupAllHostAddr(host);
            return addresses;

        } catch (UnknownHostException e) {
            exception = e;
            throw e;
        } catch (Exception e) {
            UnknownHostException tempE = new UnknownHostException(host);
            exception = tempE;
            throw tempE;
        } finally {
            if (interceptor != null) {
                try {
                    interceptor.after(host, addresses, exception);
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Override
    public String getHostByAddr(byte[] bytes) throws UnknownHostException {
        for (int i = 0; i < maxIndex; ++i) {

            NameService nameService = nameServiceList.get(i);
            try {
                return nameService.getHostByAddr(bytes);
            } catch (UnknownHostException ignored) {
            }
        }

        return lastNameService.getHostByAddr(bytes);
    }
}
