package com.duowan.yyspringboot.autoconfigure.udbpage;

import com.duowan.udb.sdk.UdbOauth;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/9/29 18:40
 */
@Configuration
@ConditionalOnClass({UdbOauth.class})
@ComponentScan({"com.duowan.yyspringboot.autoconfigure.udbpage.parameter"})
public class UdbPageParameterAutoConfiguration {
}
