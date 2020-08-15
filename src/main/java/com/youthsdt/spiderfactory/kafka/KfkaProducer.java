package com.youthsdt.spiderfactory.kafka;

import com.alibaba.fastjson.JSONObject;
import com.youthsdt.spiderfactory.entity.Message;
import com.youthsdt.spiderfactory.entity.RetResult;
import com.youthsdt.spiderfactory.util.RedisUtil;
import com.youthsdt.spiderfactory.util.UUIDUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
    @Resource
    private RedisUtil redisUtils;

    //发送消息方法
    public RetResult sendMsg(JSONObject json) {
        try {
            String articlePublicId = json.getString("article_public_id");
            if (articlePublicId == null || "".equals(articlePublicId) || articlePublicId.isEmpty()) {
                return new RetResult(500, "article_public_id值为空", null);
            }
            kafkaTemplate.send("news", json.toJSONString());
            return new RetResult(200, "kafka队列消息发送成功:" + articlePublicId, null);
        } catch (Exception e) {
            return new RetResult(200, "kafka队列消息发送失败", null);
        }

    }

    public RetResult sendMsgWithRedisService(JSONObject json) {
        try {
            String articlePublicId = json.getString("article_public_id");
            if (articlePublicId == null || articlePublicId.equals("") || articlePublicId.isEmpty()) {
                return new RetResult(500, "article_public_id值为空", null);
            }
            String value = redisUtils.get(articlePublicId);
            if (value != null) {
                return new RetResult(500, "kafka队列消息发送失败," + articlePublicId + "已经存在redis中", null);
            } else {
                kafkaTemplate.send("news", JSONObject.toJSONString(json));
                redisUtils.set(articlePublicId, articlePublicId);
                return new RetResult(200, "kafka队列消息发送成功:" + articlePublicId, null);
            }
        } catch (Exception e) {
            return new RetResult(200, "kafka队列消息发送失败", null);
        }
    }
}
