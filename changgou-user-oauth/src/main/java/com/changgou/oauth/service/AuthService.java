package com.changgou.oauth.service;

import com.changgou.oauth.util.AuthToken;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/30 20:21
 */
public interface AuthService {
    AuthToken login(String username, String password, String clientId, String clientSecret);
}
