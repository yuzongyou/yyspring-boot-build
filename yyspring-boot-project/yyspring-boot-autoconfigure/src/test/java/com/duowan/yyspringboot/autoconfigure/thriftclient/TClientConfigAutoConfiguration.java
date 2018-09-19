package com.duowan.yyspringboot.autoconfigure.thriftclient;

import com.duowan.common.thrift.client.config.TClientConfig;
import com.duowan.common.thrift.client.factory.protocol.TBinaryProtocolFactory;
import com.duowan.common.thrift.client.factory.protocol.TMultiplexedCompactProtocolFactory;
import com.duowan.common.thrift.client.factory.transport.TFastFramedTransportFactory;
import com.duowan.common.thrift.client.factory.transport.TSocketTransportFactory;
import com.duowan.common.thrift.client.servernode.FixedServerNodeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;

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
//                new FixedServerNodeProvider("127.0.0.1", 25000)
//        );
//
//        return clientConfig;
//    }

//    @Bean(initMethod = "init")
//    public ThriftService thriftService() {
//        return new ThriftService();
//    }


    @Bean
    public TClientConfig testServiceThriftClientConfig2() {
        TClientConfig clientConfig = new TClientConfig(
                new TSocketTransportFactory(),
                Arrays.asList(
                        new TBinaryProtocolFactory(TestService.class)),
                new FixedServerNodeProvider("127.0.0.1", 25001)
        );

        clientConfig.setEnabledLogging(true);

        return clientConfig;
    }

    @Bean(initMethod = "init")
    public ThriftService3 thriftService3() {
        return new ThriftService3();
    }
}
