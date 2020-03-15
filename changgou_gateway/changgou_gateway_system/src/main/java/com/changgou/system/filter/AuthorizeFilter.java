package com.changgou.system.filter;

import com.changgou.system.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/21 20:19
 */
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    private static final String AUTHORIZE_TOKEN = "token";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if (request.getURI().getPath().contains("/admin/login")) {
            return chain.filter(exchange);
        }

        String jwt = request.getHeaders().getFirst(AUTHORIZE_TOKEN);

        if (StringUtils.isEmpty(jwt)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        try {

          //  JwtUtil.parseJWT(jwt);
            return chain.filter(exchange);
        } catch (Exception e) {

            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
