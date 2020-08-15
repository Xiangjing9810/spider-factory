package com.youthsdt.spiderfactory.controller;

import com.alibaba.fastjson.JSONObject;
import com.youthsdt.spiderfactory.entity.RetResult;
import com.youthsdt.spiderfactory.kafka.KfkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xiangjing
 * @version 1.0
 * @date 2020/8/3 14:21
 */
@Controller
public class KafkaController {
    @Autowired
    private KfkaProducer producer;

    @RequestMapping("/sendMsg")
    @ResponseBody
    public RetResult testSendMsg(@RequestBody JSONObject json) {
        return producer.sendMsg(json);
    }

    @RequestMapping("/sendWithRedisService")
    @ResponseBody
    public RetResult SendMsg(@RequestBody JSONObject json) {
        return producer.sendMsgWithRedisService(json);
    }
}
