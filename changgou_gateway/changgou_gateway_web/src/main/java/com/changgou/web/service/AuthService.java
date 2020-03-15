package com.changgou.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/30 23:02
 */
@Component
public class AuthService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getJtiFromCookie(ServerHttpRequest request) {
        HttpCookie cookie = request.getCookies().getFirst("uid");
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }


    public String getTokenFromCookie(String jti) {

        return stringRedisTemplate.boundValueOps(jti).get();

    }
}



