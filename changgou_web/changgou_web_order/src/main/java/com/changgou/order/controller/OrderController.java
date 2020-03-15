package com.changgou.order.controller;


import com.changgou.entity.Result;
import com.changgou.order.feign.CartFeign;
import com.changgou.order.feign.OrderFeign;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.user.feign.UserFeign;
import com.changgou.user.pojo.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/3 19:15
 */
@Controller
@RequestMapping("/worder")
public class OrderController {

    @Autowired
    private CartFeign cartFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private OrderFeign orderFeign;


    @RequestMapping("/ready/order")
    public String readyOrder(Model model) {
        List<Address> addressList = userFeign.list().getData();

        model.addAttribute("address", addressList);

        for (Address address : addressList) {
            if ("1".equals(address.getIsDefault())) {
                model.addAttribute("deAddr", address);
            }
        }

        Map map = cartFeign.list();
        List<OrderItem> orderItems = (List<OrderItem>) map.get("orderItems");
        Integer totalNum = (Integer) map.get("totalNum");
        Integer totalMoney = (Integer) map.get("totalMoney");
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("totalMoney", totalMoney);

        return "order";
    }

    @PostMapping("/add")
    @ResponseBody
    public Result add(@RequestBody Order order) {

        Result result = orderFeign.add(order);
        return result;
    }

    @GetMapping("/toPayPage")
    public String toPayPage(String orderId, Model model) {

        Order order =  orderFeign.findById(orderId).getData();
        model.addAttribute("orderId", orderId);
        model.addAttribute("payMoney", order.getPayMoney());

        return "pay";

    }

}
