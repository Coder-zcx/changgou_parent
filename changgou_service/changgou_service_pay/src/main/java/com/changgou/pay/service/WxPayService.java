package com.changgou.pay.service;

import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/6 21:43
 */
public interface WxPayService {
    Map nativePay(String orderId, Integer money);

    Map queryOrder(String orderId);
}

