package com.changgou.search.dao;

import com.changgou.search.pojo.SkuInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/25 23:13
 */
public interface ESManagerMapper extends ElasticsearchRepository<SkuInfo,Long> {

}
