package com.changgou.goods.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/22 17:31
 */
public class Goods implements Serializable {
    private Spu spu;
    private List<Sku> skuList;

    public Spu getSpu() {
        return spu;
    }

    public void setSpu(Spu spu) {
        this.spu = spu;
    }

    public List<Sku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }
}
