package com.changgou.order.task;

import com.alibaba.fastjson.JSON;
import com.changgou.order.config.RabbitMQConfig;
import com.changgou.order.dao.TaskMapper;
import com.changgou.order.pojo.Task;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/5 21:26
 */
@Component
public class QueryPointTask {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "0/2 * * * * ?")
    public void queryTask() {
        //1.获取小于系统当前时间数据
        List<Task> taskList = taskMapper.findTaskLessTanCurrentTime(new Date());

        if (taskList != null && taskList.size() > 0) {
            for (Task task : taskList) {
                rabbitTemplate.convertAndSend(RabbitMQConfig.EX_BUYING_ADDPOINTUSER, RabbitMQConfig.CG_BUYING_FINISHADDPOINT_KEY, JSON.toJSONString(task));
            }
        }
    }


}
