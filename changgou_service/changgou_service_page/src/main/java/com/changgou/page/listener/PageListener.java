package com.changgou.page.listener;

import com.changgou.page.config.RabbitMQConfig;
import com.changgou.page.service.PageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/29 19:39
 */
@Component
public class PageListener {

    @Autowired
    private PageService pageService;

    @RabbitListener(queues = RabbitMQConfig.PAGE_CREATE_QUEUE)
    public void receiveMessage(String spuId) {
        System.out.println("生成商品详情页面,商品id为: "+spuId);
        pageService.generateItemPage(spuId);

    }
}
