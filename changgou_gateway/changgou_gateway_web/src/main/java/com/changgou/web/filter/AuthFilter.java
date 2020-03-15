package com.changgou.web.filter;

import com.changgou.web.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/30 22:55
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthService authService;

    private static final String LOGIN_URL = "http://localhost:8001/api/oauth/toLogin";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        if ("/api/oauth/login".equals(path) || "/api/oauth/toLogin".equals(path)) {
            return chain.filter(exchange);
        }


        String jti = authService.getJtiFromCookie(request);
        if (jti == null) {
            //拒绝访问
          /*  response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();*/
            //跳转登录页面
            return this.toLoginPage(LOGIN_URL + "?FROM=" + request.getURI().getPath(), exchange);

        }

        String jwt = authService.getTokenFromCookie(jti);
        if (jwt == null) {
            //拒绝访问
          /*  response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();*/
            return this.toLoginPage(LOGIN_URL, exchange);
        }
        request.mutate().header("Authorization", "Bearer " + jwt);
        return chain.filter(exchange);

    }

    //跳转登录页面
    private Mono<Void> toLoginPage(String loginUrl, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.SEE_OTHER);
        response.getHeaders().set("Location", loginUrl);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
