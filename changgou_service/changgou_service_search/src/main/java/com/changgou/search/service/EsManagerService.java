package com.changgou.search.service;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/25 23:27
 */
public interface EsManagerService {
    void createIndexAndMapping();

    void importAll();

    void importDataToESBySpuId(String spuId);

    void delDataBySpuId(String spuId);
}
