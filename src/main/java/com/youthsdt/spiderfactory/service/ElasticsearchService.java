package com.youthsdt.spiderfactory.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youthsdt.spiderfactory.entity.SearchParam;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author xiangjing
 * @version 1.0
 * @date 2020/8/27 20:53
 */

@Service
public class ElasticsearchService {

    @Qualifier("EsRestClient")
    @Autowired
    private RestClient client;
    private static Logger logger = Logger.getLogger(ElasticsearchService.class);

    /**
     * 根据id查询文档内容
     *
     * @param param
     * @return
     */
    public JSONObject getDataById(SearchParam param) {
        //拼接查询内容
        String url = "/" + param.getIndex() + "/" + param.getType() + "/" + param.getId();
        Request request = new Request("GET", url);
        JSONObject jsonObject = new JSONObject();
        try {
            Response response = client.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
            JSONObject json = JSONObject.parseObject(responseBody);
            //获取我们需要的内容
            Object source = json.get("_source");
            jsonObject = JSONObject.parseObject(source.toString());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return jsonObject;
    }

    public JSONArray getDataList(SearchParam searchParam) {
        //拼接查询内容
        String url = "/" + searchParam.getIndex() + "/_search";
        String body = "{\"query\": {\"match_all\": {}},\"size\": " + searchParam.getSize() + ",\"sort\": [{\"" + searchParam.getSort() + "\": {\"order\": \"" + searchParam.getSortType() + "\"}}]}";
        String responseBody = executeRequest(url, body);
        JSONObject json = JSONObject.parseObject(responseBody);
        //获取我们需要的内容
        JSONArray array = json.getJSONObject("hits").getJSONArray("hits");
        return formatJson(array);
    }

    public JSONArray search(SearchParam searchParam) {
        //拼接查询内容
        String url = "/" + searchParam.getIndex() + "/_search";
        StringBuffer qurryStr = new StringBuffer("{\"_source\": [\"title\",\"publish_time\" ,\"source_url\",\"publishdate\",\"source_name\",\"author\"],");
        qurryStr.append("\"query\": {\"match_phrase\": {\"content\":\"").append(searchParam.getContent()).append("\"}},\"highlight\": {\"fields\": {\"content\": {}}},\"size\": ").append(searchParam.getSize()).append(",\"sort\": [{\"").append(searchParam.getSort()).append("\": {\"order\": \"").append(searchParam.getSortType()).append("\"}}]}");
        String responseBody = executeRequest(url, qurryStr.toString());
        JSONObject json = JSONObject.parseObject(responseBody);
        //获取我们需要的内容
        JSONArray array = json.getJSONObject("hits").getJSONArray("hits");

        return formatJson2(array);
    }

    private String executeRequest(String url, String body) {
        Request request = new Request("GET", url);
        request.setJsonEntity(body);
        try {
            Response response = client.performRequest(request);
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private JSONArray formatJson(JSONArray array) {
        for (Object item : array) {
            JSONObject object = (JSONObject) item;
            object.remove("sort");
            object.remove("_index");
            object.remove("_score");
            object.remove("_type");
            object.remove("_id");
            String content = object.getJSONObject("_source").getString("content");
            if (content.length() > 199) {
                content = content.substring(0, 199) + "...";
            }
            object.getJSONObject("_source").put("content", content);
        }
        return array;
    }

    private JSONArray formatJson2(JSONArray array) {
        for (Object item : array) {
            JSONObject object = (JSONObject) item;
            object.remove("sort");
            object.remove("_index");
            object.remove("_score");
            object.remove("_type");
            object.remove("_id");
            StringBuffer content = new StringBuffer();
            JSONArray jsonArray = object.getJSONObject("highlight").getJSONArray("content");
            for (Object highlight : jsonArray) {
                content.append(highlight);
            }
            object.remove("highlight");
            String centStr = content.toString();
            if (centStr.length() > 199) {
                centStr = centStr.substring(0, 199) + "...";
            }
            object.getJSONObject("_source").put("content", centStr);
        }
        return array;
    }

}
