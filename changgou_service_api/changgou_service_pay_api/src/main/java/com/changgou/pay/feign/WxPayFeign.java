package com.changgou.pay.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/6 21:58
 */
@FeignClient(name = "pay")
public interface WxPayFeign {

    @GetMapping("/nativePay")
    public Result nativePay(@RequestParam("orderId")String orderId, @RequestParam("money") Integer money);
}
