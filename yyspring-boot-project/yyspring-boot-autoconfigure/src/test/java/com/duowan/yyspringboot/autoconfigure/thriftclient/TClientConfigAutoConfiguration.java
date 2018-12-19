package com.duowan.yyspringboot.autoconfigure.thriftclient;

import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.factory.protocol.TBinaryProtocolFactory;
import com.duowan.common.thrift.client.factory.transport.TSocketTransportFactory;
import com.duowan.common.thrift.client.servernode.FixedServerNodeDiscovery;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/19 17:23
 */
@Configuration
public class TClientConfigAutoConfiguration {

//    @Bean
//    public TClientConfig testServiceThriftClientConfig() {
//        TClientConfig clientConfig = new TClientConfig(
//                new TFastFramedTransportFactory(),
//                Arrays.asList(
//                        new TMultiplexedCompactProtocolFactory(TestService.class, "test1"),
//                        new TMultiplexedCompactProtocolFactory(TestService2.class, "test2")
//                ),
//                new FixedServerNodeDiscovery("127.0.0.1", 25000)
//        );
//
//        return clientConfig;
//    }

    @Bean
    public TClientConfig testServiceThriftClientConfig2() {
        TClientConfig clientConfig = new TClientConfig(
                new TSocketTransportFactory(),
                new TBinaryProtocolFactory(TestService.class),
                new FixedServerNodeDiscovery("127.0.0.1", 25001)
        );
        return clientConfig;
    }
}
