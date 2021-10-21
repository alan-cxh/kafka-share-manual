package com.chinatower.framework.producer.service;

import com.chinatower.framework.mq.kafka.api.KafkaUtils;
import com.chinatower.framework.producer.hanler.DemoHanler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class KafkaDemoService {
    @Autowired
    private KafkaUtils kafkaUtils;
//    @Autowired
//    private DemoHanler Hanler;
    @RequestMapping("async")
    public String sendByAsync(String topic){
        System.out.println("in");
        topic = topic.trim();
        String str = "from haosc";
        try {
        	 String result=  kafkaUtils.asyncSend(topic, str);
             
             if(!result.equals("successful")){

             }
		} catch (Exception e) {
			// 重试或者其它操作
		}
     
        return "ok";
    }
}
