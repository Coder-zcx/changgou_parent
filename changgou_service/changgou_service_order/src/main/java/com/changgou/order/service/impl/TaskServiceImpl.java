package com.changgou.order.service.impl;

import com.changgou.order.dao.TaskHisMapper;
import com.changgou.order.dao.TaskMapper;
import com.changgou.order.pojo.Task;
import com.changgou.order.pojo.TaskHis;
import com.changgou.order.service.TaskService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Author: Coder-zcx
 * @Date: 2019/12/5 22:35
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskHisMapper taskHisMapper;


    @Override
    public void delTask(Task task) {

        task.setDeleteTime(new Date());
        Long id = task.getId();
        task.setId(null);

        TaskHis taskHis = new TaskHis();
        BeanUtils.copyProperties(task, taskHis);
        taskHisMapper.insertSelective(taskHis);
        task.setId(id);
        taskMapper.deleteByPrimaryKey(id);
    }
}
