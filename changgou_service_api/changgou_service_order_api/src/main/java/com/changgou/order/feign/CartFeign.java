package com.changgou.order.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/2 19:44
 */
@FeignClient(name = "order")
public interface CartFeign {


    @RequestMapping("/cart/add")
    public Result add(@RequestParam("skuId") String skuId, @RequestParam("num") Integer num);

    @RequestMapping("/cart/list")
    public Map list();
}
