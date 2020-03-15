package com.changgou.goods.feign;

import com.changgou.entity.Result;
import com.changgou.goods.pojo.Spu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/29 20:10
 */
@FeignClient(name = "goods")
public interface SpuFeign {

    @GetMapping("/spu/findSpuById/{spuId}")
    public Result<Spu> findSpuById(@PathVariable String spuId);
}
