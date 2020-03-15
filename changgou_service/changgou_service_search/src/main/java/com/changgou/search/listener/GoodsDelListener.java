package com.changgou.search.listener;

import com.changgou.search.config.RabbitMQConfig;
import com.changgou.search.service.EsManagerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/26 22:16
 */
@Component
public class GoodsDelListener {

    @Autowired
    private EsManagerService esManagerService;

    @RabbitListener(queues = "search_del_queue")
    public void receiveMessage(String spuId){
        System.out.println("接收到消息："+spuId);
        esManagerService.delDataBySpuId(spuId);
    }
}
