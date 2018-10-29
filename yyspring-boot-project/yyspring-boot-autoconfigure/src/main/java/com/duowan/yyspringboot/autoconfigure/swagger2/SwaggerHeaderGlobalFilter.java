package com.duowan.yyspringboot.autoconfigure.swagger2;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * @author Arvin
 * @version 1.0
 * @since 2018/10/19 21:21
 */
public class SwaggerHeaderGlobalFilter implements GlobalFilter, Ordered {

    private static final String SWAGGER2_API_DOCS_URI = "/v2/api-docs";
    private static final String SWAGGER2_API_DOCS_YY_URI = "/vyy/api-docs";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        Object uriObj = exchange.getAttributes().get(GATEWAY_REQUEST_URL_ATTR);
        if (uriObj != null) {
            URI uri = (URI) uriObj;
            if (!uri.toString().endsWith(SWAGGER2_API_DOCS_URI)) {
                return chain.filter(exchange);
            }
            try {
                URI newUri = new URI(uri.toString().replace(SWAGGER2_API_DOCS_URI, SWAGGER2_API_DOCS_YY_URI));
                exchange.getAttributes().put(GATEWAY_REQUEST_URL_ATTR, newUri);
            } catch (URISyntaxException ignored) {
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return LoadBalancerClientFilter.LOAD_BALANCER_CLIENT_FILTER_ORDER + 1;
    }

}
