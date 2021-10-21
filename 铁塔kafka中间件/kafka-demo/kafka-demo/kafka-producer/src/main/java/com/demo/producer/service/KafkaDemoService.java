package com.demo.producer.service;

import com.chinatower.framework.mq.kafka.api.KafkaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class KafkaDemoService {
    @Autowired
    private KafkaUtils kafkaUtils;

    @RequestMapping("async")
    public String sendByAsync(@RequestParam(required = false, defaultValue = "ttcv") String topic){
        try {
            String str = "this is chinatower-res-topic";
            String s = kafkaUtils.syncSend(topic, str);
            System.out.println("返回值：" + s);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
