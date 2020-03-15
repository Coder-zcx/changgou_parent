package com.changgou.search.service;

import java.util.Map; /**
 * @Author: Coder-zcx
 * @Date: 2019/11/27 19:01
 */
public interface SearchService {
    Map<String,Object> search(Map<String,String> searchMap);
}
