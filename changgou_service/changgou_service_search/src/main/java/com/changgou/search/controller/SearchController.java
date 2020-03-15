package com.changgou.search.controller;

import com.changgou.entity.Page;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/27 19:07
 */
@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/list")
    public String list(@RequestParam Map<String, String> searchMap, Model model) {
        //特殊符号处理
        this.handlerSearchMap(searchMap);

        //获取查询结果
        Map resultMap = searchService.search(searchMap);
        model.addAttribute("result",resultMap);
        model.addAttribute("searchMap",searchMap);

        //封装分页数据并返回
        //1.总记录数
        //2.当前页
        //3.每页显示多少条
        Page<SkuInfo> page = new Page<SkuInfo>(
                Long.parseLong(String.valueOf( resultMap.get("total"))),
                Integer.parseInt(String.valueOf(resultMap.get("pageNum"))),
                Page.pageSize
        );
        model.addAttribute("page",page);

        //拼装url
        StringBuilder url = new StringBuilder("/search/list");
        if (searchMap != null && searchMap.size()>0){
            //是由查询条件
            url.append("?");
            for (String paramKey : searchMap.keySet()) {
                if (!"sortRule".equals(paramKey) && !"sortField".equals(paramKey) && !"pageNum".equals(paramKey)){
                    url.append(paramKey).append("=").append(searchMap.get(paramKey)).append("&");
                }
            }
            //http://localhost:9009/search/list?keywords=手机&spec_网络制式=4G&
            String urlString = url.toString();
            //去除路径上的最后一个&
            urlString=urlString.substring(0,urlString.length()-1);
            model.addAttribute("url",urlString);
        }else{
            model.addAttribute("url",url);
        }
        return "search";
    }

    @GetMapping
    @ResponseBody
    public Map list(@RequestParam Map<String, String> searchMap) {
        handlerSearchMap(searchMap);
        return searchService.search(searchMap);
    }


    public void handlerSearchMap(@RequestParam Map<String, String> searchMap) {
        if (searchMap != null) {
            for (Map.Entry<String, String> maps : searchMap.entrySet()) {
                if (maps.getKey().startsWith("spec_")) {
                    searchMap.put(maps.getKey(), maps.getValue().replace("+", "%2B"));
                }
            }
        }
    }

}
