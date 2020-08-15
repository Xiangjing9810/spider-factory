package com.youthsdt.spiderfactory.controller;

import com.alibaba.fastjson.JSONObject;
import com.youthsdt.spiderfactory.entity.RetResult;
import com.youthsdt.spiderfactory.util.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @author xiangjing
 * @version 1.0
 * @date 2020/8/15 0:25
 */
@Controller
public class RedisServerController {
    @Resource
    private RedisUtil redisUtils;

    @RequestMapping("/checkRedis")
    @ResponseBody
    public String ifHasInRedis(@RequestBody JSONObject params) {
        String articlePublicId = params.getString("article_public_id");
        String value = redisUtils.get(articlePublicId);
        if (value != null) {
            return "true";
        } else {
            return "false";
        }
    }
}
