package com.chinatower.framework.consumer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.chinatower.framework.mq.kafka.api.KafkaUtils;
import com.chinatower.framework.mq.kafka.consumer.ConsumerMessage;

@Data
@Slf4j
@Component
public class DemoConsumer implements ConsumerMessage {

    KafkaUtils kafkaUtils;
    private  final String  LISTENER_ID="test";
    @Override
    @KafkaListener(id = LISTENER_ID, topics = {"${kafka.topic.topicName}"})
    public <T> T ack(ConsumerRecord consumerRecord) {
    	String test=consumerRecord.topic();
        log.info("---------- kafka consumer "+consumerRecord.value()+"  --------");
        return null;
    }
}
