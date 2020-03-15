package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.config.TokenDecode;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/2 18:33
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private TokenDecode tokenDecode;


    @RequestMapping("/add")
    public Result add(String skuId, Integer num) {
      //  String username = "itheima";
        String username = tokenDecode.getUserInfo().get("username");
        cartService.add(skuId, num, username);
        return new Result(true, StatusCode.OK, "添加成功");
    }


    @RequestMapping("/list")
    public Map list() {
       // String username = "itheima";
        String username = tokenDecode.getUserInfo().get("username");
        Map map =  cartService.list(username);
        return map;
    }


}
