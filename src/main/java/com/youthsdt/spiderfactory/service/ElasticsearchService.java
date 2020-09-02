package com.youthsdt.spiderfactory.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youthsdt.spiderfactory.entity.SearchParam;
import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author xiangjing
 * @version 1.0
 * @date 2020/8/27 20:53
 */

@Service
public class ElasticsearchService {

    @Qualifier("EsRestClient")
    @Autowired
    private RestHighLevelClient client;
    private static Logger logger = Logger.getLogger(ElasticsearchService.class);


    public JSONObject getDataList(SearchParam searchParam) throws IOException {
        SearchRequest searchRequest = new SearchRequest(searchParam.getIndex());
        searchRequest.types(searchParam.getType());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        //分页查询，设置起始下标，从0开始
        searchSourceBuilder.from(0);
        //每页显示个数
        searchSourceBuilder.size(searchParam.getSize());
        //source源字段过虑
        String[] fields = {"article_public_id", "websitelogo", "es_index", "spider_time", "es_type", "contenthtml"};
        searchSourceBuilder.fetchSource(new String[]{}, fields);
        // 排序
        FieldSortBuilder fsb = SortBuilders.fieldSort("spider_time");
        fsb.order(SortOrder.DESC);
        searchSourceBuilder.sort(fsb);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        long totalHits = hits.getTotalHits();
        JSONObject res = new JSONObject();
        JSONArray array = new JSONArray();
        res.put("total", totalHits);
        for (SearchHit searchHit : hits.getHits()) {
            Map json = searchHit.getSourceAsMap();
            JSONObject item = new JSONObject(json);
            if (item.getString("content").length() > 199) {
                item.put("content", item.getString("content").substring(0, 199) + "...");
            }
            array.add(item);
        }
        res.put("data", array);
        return res;

    }


    public JSONObject search(SearchParam searchParam) throws IOException {
        SearchRequest searchRequest = new SearchRequest(searchParam.getIndex());
        searchRequest.types(searchParam.getType());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchPhraseQuery("content", searchParam.getContent()));
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<em>");//设置前缀
        highlightBuilder.postTags("</em>");//设置后缀
        highlightBuilder.field("content");
        searchSourceBuilder.highlighter(highlightBuilder);

        //分页查询，设置起始下标，从0开始
        searchSourceBuilder.from((searchParam.getPage() - 1) * searchParam.getSize());
        //每页显示个数
        searchSourceBuilder.size(searchParam.getSize());
        //source源字段过虑
        String[] fields = {"article_public_id", "websitelogo", "es_index", "spider_time", "es_type", "contenthtml"};
        searchSourceBuilder.fetchSource(new String[]{}, fields);
        // 排序
        FieldSortBuilder fsb = SortBuilders.fieldSort("spider_time");
        fsb.order(SortOrder.DESC);
        searchSourceBuilder.sort(fsb);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        long totalHits = hits.getTotalHits();
        JSONObject res = new JSONObject();
        JSONArray array = new JSONArray();
        res.put("total", totalHits);
        res.put("currentPage", searchParam.getPage());
        for (SearchHit searchHit : hits.getHits()) {
            Map json = searchHit.getSourceAsMap();
            JSONObject item = new JSONObject(json);
            //获取高亮字段
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            HighlightField contentField = highlightFields.get("content");
            if (contentField != null) {
                Text[] fragments = contentField.fragments();
                String content = "";
                for (Text text : fragments) {
                    content += text;
                }
                item.put("content", content);
            }
            array.add(item);
        }
        res.put("data", array);
        return res;
    }


}
