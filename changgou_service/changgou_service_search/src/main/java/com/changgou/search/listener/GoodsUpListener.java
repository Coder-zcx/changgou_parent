package com.changgou.search.listener;

import com.changgou.search.config.RabbitMQConfig;
import com.changgou.search.service.EsManagerService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/25 23:36
 */
@Component
public class GoodsUpListener {

    @Autowired
    private EsManagerService esManagerService;

    @RabbitListener(queues = RabbitMQConfig.SEARCH_ADD_QUEUE)
    public void receiveMessage(String spuId){
        System.out.println("接收到消息："+spuId);
        esManagerService.importDataToESBySpuId(spuId);
    }


}
