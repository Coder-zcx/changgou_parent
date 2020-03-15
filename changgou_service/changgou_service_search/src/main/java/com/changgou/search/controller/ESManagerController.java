package com.changgou.search.controller;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.search.service.EsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/25 23:15
 */
@RestController
@RequestMapping("/manager")
public class ESManagerController {

    @Autowired
    private EsManagerService esManagerService;

    @GetMapping("/create")
    public Result createIndexAndMappint() {
        esManagerService.createIndexAndMapping();
        return new Result(true, StatusCode.OK, "创建索引结构成功");
    }

    @GetMapping("/importAll")
    public Result importAllData() {
        esManagerService.importAll();
        return new Result(true, StatusCode.OK, "全部导入成功");
    }
}
