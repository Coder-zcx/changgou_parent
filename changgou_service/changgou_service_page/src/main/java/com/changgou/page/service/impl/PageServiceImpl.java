package com.changgou.page.service.impl;

import com.changgou.goods.feign.CategoryFeign;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.page.service.PageService;
import com.netflix.ribbon.proxy.annotation.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.Writer;
import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/29 19:46
 */
@Service
public class PageServiceImpl implements PageService {

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private CategoryFeign categoryFeign;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${pagepath}")
    private String pagepath;

    @Override
    public void generateItemPage(String spuId) {

        Context context = new Context();
        Map<String, Object> itemData = this.findItemData(spuId);
        context.setVariables(itemData);
        File file = new File(pagepath);
        if (file == null) {
            file.mkdirs();
        }
        Writer out = null;


        templateEngine.process("item", context, out);

    }

    private Map<String, Object> findItemData(String spuId) {
        return null;
    }
}
