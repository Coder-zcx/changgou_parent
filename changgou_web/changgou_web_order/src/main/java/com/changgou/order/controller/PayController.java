package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.pay.feign.WxPayFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/6 22:00
 */
@Controller
@RequestMapping("/wxpay")
public class PayController {

    @Autowired
    private WxPayFeign wxPayFeign;

    @Autowired
    private OrderFeign orderFeign;

    @GetMapping
    public String wxPay(String orderId, Model model) {

        Order order = orderFeign.findById(orderId).getData();
        if (order == null) {
            return "fail";
        }
        if ("1".equals(order.getPayStatus())) {
            return "fail";
        }
        Map map = (Map) wxPayFeign.nativePay(orderId, order.getPayMoney()).getData();
        if (map == null) {
            return "fail";
        }
        map.put("orderId", orderId);
        map.put("payMoney", order.getPayMoney());
        model.addAttribute(map);
        return "wxpay";

    }

    @GetMapping("/topaysuccess")
    public String toPaySuccess(String payMoney, Model model) {
        model.addAttribute("payMoney", payMoney);
        return "paysuccess";
    }

}
