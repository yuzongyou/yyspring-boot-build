package com.duowan.yyspringboot.autoconfigure.wxmp;

import com.duowan.udb.sdk.UdbClient;
import com.duowan.wxmpsdk.client.WxmpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/8 16:48
 */
@Configuration
@ConditionalOnClass({WxmpClient.class, UdbClient.class})
@ComponentScan({"com.duowan.yyspringboot.autoconfigure.wxmp.parameter"})
public class WxmpAutoConfiguration {

}

