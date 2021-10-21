package com.alan.kafkaexample.consume.service;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 */
@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topicPattern = "${kafka.topic.pattern}", groupId = "${kafka.group-id}", containerFactory = "batchContainerFactory")
    public void topicTest(List<ConsumerRecord<String, Object>> recordList, Acknowledgment ack) {
        for (ConsumerRecord<String, Object> record : recordList) {
            log.info("成功消费到数据 >> {}, \n record = {}", record.value(), record);
        }
        ack.acknowledge();
    }
}
