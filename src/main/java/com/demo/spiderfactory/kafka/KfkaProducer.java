package com.demo.spiderfactory.kafka;

import com.alibaba.fastjson.JSONObject;
import com.demo.spiderfactory.entity.Message;
import com.demo.spiderfactory.util.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author xiangjing
 * @version 1.0
 * @date 2020/8/3 14:10
 */
@Component
public class KfkaProducer {
    private static Logger logger = LoggerFactory.getLogger(KfkaProducer.class);
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //发送消息方法
    public String send(JSONObject json) {
        try {
            Message message = new Message();
            message.setId(UUIDUtils.getUUID());
            message.setMsg(json.toJSONString());
            message.setSendTime(System.currentTimeMillis());
            System.out.println(JSONObject.toJSONString(message));
            //kafkaTemplate.send("news", JSONObject.toJSONString(message));
            return "200";
        } catch (Exception e) {
            return "500";
        }

    }
}
