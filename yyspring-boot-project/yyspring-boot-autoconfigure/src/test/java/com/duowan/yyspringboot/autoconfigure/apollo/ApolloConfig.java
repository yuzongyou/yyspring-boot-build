package com.duowan.yyspringboot.autoconfigure.apollo;

import com.ctrip.framework.apollo.core.ConfigConsts;
import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ApolloConfig {

    @EnableApolloConfig(ConfigConsts.NAMESPACE_APPLICATION)
    static class TokenApolloConfig {
        @Bean
        public TokenConfigBean tokenConfigBean() {
            return new TokenConfigBean();
        }
    }

    @EnableApolloConfig("qp-oauth2-yy")
    static class CustomNameSpaceConfig {

        @Bean
        public Oauth2YyConfig oauth2YyConfig() {
            return new Oauth2YyConfig();
        }
    }
}
