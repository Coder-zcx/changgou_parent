package com.changgou.order.service.impl;

import com.changgou.entity.Result;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/2 18:48
 */
@Service
public class CartServiceImpl implements CartService {

    private static final String CART = "cart_";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Override
    public void add(String skuId, Integer num, String username) {

        OrderItem orderItem = (OrderItem) redisTemplate.boundHashOps(CART + username).get(skuId);
        if (orderItem != null) {
            orderItem.setNum(orderItem.getNum() + num);
            if (orderItem.getNum() <= 0) {
                redisTemplate.boundHashOps(CART + username).delete(skuId);
                return;
            }
            orderItem.setMoney(orderItem.getNum() * orderItem.getPrice());
            orderItem.setPayMoney(orderItem.getMoney() * orderItem.getPrice());

        } else {
            Sku sku = skuFeign.findById(skuId).getData();

            Spu spu = spuFeign.findSpuById(sku.getSpuId()).getData();

            orderItem = this.sku2OrderItem(sku, spu, num);
        }
        redisTemplate.boundHashOps(CART + username).put(skuId, orderItem);
    }


    @Override
    public Map list(String username) {
        List<OrderItem> orderItems = redisTemplate.boundHashOps(CART + username).values();
        if (orderItems.size() > 0 && orderItems != null) {
            Map map = new HashMap();
            //商品数量与总价格
            Integer totalNum = 0;
            Integer totalPrice = 0;
            for (OrderItem orderItem : orderItems) {
                totalNum += orderItem.getNum();
                totalPrice += orderItem.getMoney();
            }
            map.put("orderItems", orderItems);
            map.put("totalNum", totalNum);
            map.put("totalMoney", totalPrice);
            return map;
        }
        return null;
    }


    private OrderItem sku2OrderItem(Sku sku, Spu spu, Integer num) {
        OrderItem orderItem = new OrderItem();
        orderItem.setSpuId(sku.getSpuId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(orderItem.getPrice() * num);
        orderItem.setPayMoney(orderItem.getPrice() * num);
        orderItem.setImage(sku.getImage());
        orderItem.setWeight(sku.getWeight() * num);
        //分类信息
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());

        return orderItem;
    }
}
