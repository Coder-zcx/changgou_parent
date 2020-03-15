package com.changgou.order.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/2 18:45
 */
public interface CartService {
    void add(String skuId, Integer num, String username);

    Map list(String username);
}
