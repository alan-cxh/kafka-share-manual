package com.newland.kafka.sendobjects.service;

import com.newland.kafka.sendobjects.entity.TopicEntity;
import org.springframework.kafka.support.Acknowledgment;

import java.util.Map;


public interface KafkaToDbService {
    void handleKafkaMessage(Acknowledgment acknowledgment, Map<String, Object> kafkaKey, TopicEntity topicEntity);
}
