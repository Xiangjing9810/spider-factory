package com.youthsdt.spiderfactory.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youthsdt.spiderfactory.entity.SearchParam;
import com.youthsdt.spiderfactory.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


/**
 * @author xiangjing
 * @version 1.0
 * @date 2020/8/27 20:34
 */
@RestController
@RequestMapping("es")
public class ESController {

    @Autowired
    private ElasticsearchService service;

    @PostMapping("/getDataList")
    public JSONObject getDataList(@RequestBody SearchParam searchParam) throws IOException {
        return service.getDataList(searchParam);
    }

    @PostMapping("/search")
    public JSONObject search(@RequestBody SearchParam searchParam) throws IOException {
        if ("".equals(searchParam.getContent())) {
            return service.getDataList(searchParam);
        } else {
            return service.search(searchParam);
        }

    }
}
