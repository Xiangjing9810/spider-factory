package com.demo.spiderfactory.controller;

import com.alibaba.fastjson.JSONObject;
import com.demo.spiderfactory.kafka.KfkaProducer;
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
public class KafkaTestController {
    @Autowired
    private KfkaProducer producer;

    @RequestMapping("/testSendMsg")
    @ResponseBody
    public String testSendMsg(@RequestBody JSONObject json) {
        return producer.send(json);
    }
}
