package com.duowan.recachedns;

import com.duowan.common.dns.DnsInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/7 12:01
 */
@Configuration
public class ReCacheDnsConfiguration {

    @Bean
    public DnsInterceptor dnsInterceptor() {
        return new DnsInterceptor() {
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
        };
    }
}
