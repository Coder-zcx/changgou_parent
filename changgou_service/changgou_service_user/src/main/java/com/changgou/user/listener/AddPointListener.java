package com.changgou.user.listener;


import com.alibaba.fastjson.JSON;
import com.changgou.order.pojo.Task;
import com.changgou.user.config.RabbitMQConfig;
import com.changgou.user.service.UserService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/5 21:44
 */
@Component
public class AddPointListener {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.CG_BUYING_ADDPOINT)
    public void receiveMessage(String m) {
        Task task = JSON.parseObject(m, Task.class);
        if (task == null && StringUtils.isEmpty(task.getRequestBody())) {
            return;
        }
        Object value = redisTemplate.boundValueOps(task.getId()).get();
            if (value != null) {
            return;
        }

        int result = userService.updateUserPoints(task);
        if (result == 0) {
            return;
        }
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_BUYING_ADDPOINTUSER, RabbitMQConfig.CG_BUYING_FINISHADDPOINT_KEY, JSON.toJSONString(task));
    }
}
