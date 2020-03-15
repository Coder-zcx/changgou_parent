package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @Author: Coder-zcx
 * @Date: 2019/11/27 19:06
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        Map<String, Object> resultMap = new HashMap<>();
        if (searchMap != null) {
            //封装查询条件
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
            //关键词
            if (!StringUtils.isEmpty(searchMap.get("keywords"))) {
                boolQuery.must(QueryBuilders.matchQuery("name", searchMap.get("keywords")).operator(Operator.AND));
            }
            //1:条件 品牌
            if (!StringUtils.isEmpty(searchMap.get("brand"))) {
                boolQuery.filter(QueryBuilders.termQuery("brandName", searchMap.get("brand")));
            }
            //2:条件 规格
            for (String key : searchMap.keySet()) {
                if (key.startsWith("spec_")) {
                    String value = searchMap.get(key).replace("%2B", "+");
                    boolQuery.filter(QueryBuilders.termQuery("specMap." + key.substring(5) + ".keyword", value));
                }
            }
            //3:条件 价格
            if (!StringUtils.isEmpty(searchMap.get("price"))) {
                String value = searchMap.get("price");
                String[] split = value.split("-");
                if (split.length == 2) {
                    boolQuery.filter(QueryBuilders.rangeQuery("price").lte(split[1]));
                }
                boolQuery.filter(QueryBuilders.rangeQuery("price").gte(split[0]));
            }

            //4. 原生搜索实现类
            NativeSearchQueryBuilder nativeSearch = new NativeSearchQueryBuilder();
            nativeSearch.withQuery(boolQuery);

            //5:高亮
            HighlightBuilder.Field file = new HighlightBuilder
                    .Field("name")
                    .preTags("<span style='color:red'>")
                    .postTags("</span>");
            nativeSearch.withHighlightFields(file);

            //6. 品牌聚合(分组)查询
            String skuBrand = "skuBrand";
            nativeSearch.addAggregation(AggregationBuilders.terms(skuBrand).field("brandName"));
            //7. 规格聚合(分组)查询
            String skuSpec = "skuSpec";
            nativeSearch.addAggregation(AggregationBuilders.terms(skuSpec).field("spec.keyword"));
            //8: 排序
            if (!StringUtils.isEmpty(searchMap.get("sortField"))) {
                if ("ASC".equals(searchMap.get("sortRule"))) {
                    nativeSearch.withSort(SortBuilders.fieldSort(searchMap.get("sortField")).order(SortOrder.ASC));
                } else {
                    nativeSearch.withSort(SortBuilders.fieldSort(searchMap.get("sortField")).order(SortOrder.DESC));
                }
            }
            //9: 分页
            String pageNum = searchMap.get("pageNum");
            String pageSize = searchMap.get("pageSize");
            if (pageNum == null) {
                pageNum = "1";
            }
            if (pageSize == null) {
                pageSize = "30";
            }
            nativeSearch.withPageable(PageRequest.of(Integer.parseInt(pageNum) - 1, Integer.parseInt(pageSize)));
            //获取结果
            AggregatedPage<SkuInfo> skuInfos = esTemplate.queryForPage(nativeSearch.build(), SkuInfo.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                    SearchHits hits = searchResponse.getHits();
                    List<T> list = new ArrayList();
                    if (hits != null) {
                        for (SearchHit hit : hits) {
                            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);
                            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                            if (null != highlightFields && highlightFields.size() > 0) {
                                skuInfo.setName(highlightFields.get("name").getFragments()[0].toString());
                            }
                            list.add((T) skuInfo);
                        }
                    }
                    return new AggregatedPageImpl<T>(list, pageable, hits.getTotalHits(), searchResponse.getAggregations());
                }
            });

            //封装查询结果
            resultMap.put("total", skuInfos.getTotalElements());
            resultMap.put("totalPage", skuInfos.getTotalPages());
            resultMap.put("rows", skuInfos.getContent());
            StringTerms stringTerms = (StringTerms) skuInfos.getAggregation(skuBrand);
            List<String> brandList = stringTerms.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("brandList", brandList);
            StringTerms aggregation = (StringTerms) skuInfos.getAggregation(skuSpec);
            List<String> specList = aggregation.getBuckets().stream().map(bucket -> bucket.getKeyAsString()).collect(Collectors.toList());
            resultMap.put("specList", specList);
            //当前页
            resultMap.put("pageNum",pageNum);
            return resultMap;
        }
        return null;
    }


    public Map<String, Set<String>> formatSpec(List<String> specList) {
        Map<String, Set<String>> setMap = new HashMap<>();
        if (specList != null && specList.size() > 0) {
            for (String spec : specList) {
                Map<String, String> map = JSON.parseObject(spec, Map.class);
                for (String key : map.keySet()) {
                    Set<String> set = setMap.get(key);
                    if (set == null) {
                        set = new HashSet<>();
                    }
                    set.add(map.get(key));
                    setMap.put(key, set);
                }
            }
        }
        return setMap;
    }
}
