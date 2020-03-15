package com.changgou.order.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.order.feign.CartFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/2 19:56
 */
@Controller
@RequestMapping("/wcart")
public class CartController {

    @Autowired
    private CartFeign cartFeign;

    @RequestMapping("/list")
    public String list(Model model) {
        Map list = cartFeign.list();
        model.addAttribute("items", list);
        return "cart";
    }

    @RequestMapping("/add")
    @ResponseBody
    public Result add(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num) {
        cartFeign.add(skuId, num);
        Map list = cartFeign.list();
        return new Result(true, StatusCode.OK, "添加成功", list);
    }

}
