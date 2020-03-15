package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.dao.ESManagerMapper;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.EsManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/25 23:27
 */
@Service
public class EsManagerServiceImpl implements EsManagerService {
    @Autowired
    private ESManagerMapper esManagerMapper;

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void createIndexAndMapping() {
        elasticsearchTemplate.createIndex(SkuInfo.class);
        elasticsearchTemplate.putMapping(SkuInfo.class);
    }

    @Override
    public void importAll() {
        List<Sku> skuList = skuFeign.findListBySpuId("all");
        if (skuList == null || skuList.size() < 1) {
            throw new RuntimeException("无数据");
        }
        //将list转成json
        String jsonString = JSON.toJSONString(skuList);
        //json转成对象
        List<SkuInfo> skuInfos = JSON.parseArray(jsonString, SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            Map map = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(map);
        }
        esManagerMapper.saveAll(skuInfos);

    }

    @Override
    public void importDataToESBySpuId(String spuId) {
        List<Sku> listBySpuId = skuFeign.findListBySpuId(spuId);
        if (listBySpuId == null || listBySpuId.size() < 1) {
            throw new RuntimeException("无数据");
        }
        String jsonString = JSON.toJSONString(listBySpuId);
        List<SkuInfo> skuInfos = JSON.parseArray(jsonString, SkuInfo.class);
        for (SkuInfo skuInfo : skuInfos) {
            Map map = JSON.parseObject(skuInfo.getSpec(), Map.class);
            skuInfo.setSpecMap(map);
        }
        esManagerMapper.saveAll(skuInfos);
    }

    @Override
    public void delDataBySpuId(String spuId) {

        List<Sku> listBySpuId = skuFeign.findListBySpuId(spuId);
        if (listBySpuId == null || listBySpuId.size() < 1) {
            throw new RuntimeException("无数据");
        }
        for (Sku sku : listBySpuId) {
            esManagerMapper.deleteById(Long.parseLong(sku.getId()));
        }

    }

}
