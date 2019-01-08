package com.duowan.yyspringboot.autoconfigure.web;

import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 * @author Arvin
 * @version 1.0
 * @since 2019/1/8 12:35
 */
public class LegacyCookieProcessorCustomizer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addContextCustomizers((TomcatContextCustomizer) context -> context.setCookieProcessor(new LegacyCookieProcessor()));
    }
}
