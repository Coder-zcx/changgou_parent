package com.changgou.goods.feign;

import com.changgou.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/29 19:56
 */
@FeignClient(name = "goods")
public interface CategoryFeign {

    @GetMapping("/category/{spuId}")
    public Result findById(@PathVariable Integer spuId);
}
