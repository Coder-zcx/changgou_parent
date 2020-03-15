package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/25 22:52
 */
@FeignClient(name = "goods")
public interface SkuFeign {

    @GetMapping("/sku/spu/{spuId}")
    List<Sku> findListBySpuId(@PathVariable String spuId);

    @GetMapping("/sku/{id}")
    public Result<Sku> findById(@PathVariable String id);

    @RequestMapping("/sku/decr/count")
    public Result decrCount(@RequestParam("username") String username);
}
