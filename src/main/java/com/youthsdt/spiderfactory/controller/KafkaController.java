package com.youthsdt.spiderfactory.controller;

import com.alibaba.fastjson.JSONObject;
import com.youthsdt.spiderfactory.entity.RetResult;
import com.youthsdt.spiderfactory.kafka.KfkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiangjing
 * @version 1.0
 * @date 2020/8/3 14:21
 */
@RestController
public class KafkaController {
    @Autowired
    private KfkaProducer producer;

    @PostMapping("/sendMsg")
    public RetResult testSendMsg(@RequestBody JSONObject json) {
        return producer.sendMsg(json);
    }

    @PostMapping("/sendWithRedisService")
    public RetResult SendMsg(@RequestBody JSONObject json) {
        return producer.sendMsgWithRedisService(json);
    }
}
