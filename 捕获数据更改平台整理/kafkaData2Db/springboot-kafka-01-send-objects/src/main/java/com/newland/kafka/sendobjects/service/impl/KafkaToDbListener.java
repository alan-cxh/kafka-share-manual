package com.newland.kafka.sendobjects.service.impl;

import com.newland.kafka.sendobjects.entity.TopicEntity;
import com.newland.kafka.sendobjects.service.KafkaToDbService;
import com.newland.kafka.sendobjects.utils.JsonUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @author: kangweixiang
 * @date: 2021年02月02日 15:58
 */
@Component
public class KafkaToDbListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaToDbListener.class);

    @Autowired
    private KafkaToDbService kafkaToDbService;


    @KafkaListener(topicPattern = "${kafka.topic.pattern}", groupId = "${kafka.group-id}", containerFactory = "batchContainerFactory")
    public void batchListener(List<ConsumerRecord> consumerRecords, Acknowledgment acknowledgment) {

        for (ConsumerRecord record : consumerRecords) {
            LOGGER.info(JsonUtils.toJson(record.topic()));
        }
        acknowledgment.acknowledge();
        Iterator<ConsumerRecord> iterator = consumerRecords.iterator();
        while (iterator.hasNext()) {
            ConsumerRecord consumerRecord = iterator.next();

            TopicEntity topicEntity = JsonUtils.toObject(String.valueOf(consumerRecord.value()), TopicEntity.class);
            if (topicEntity == null) {
                LOGGER.warn("body为空:" + JsonUtils.toJson(consumerRecord));
                continue;
            }
            Map<String, Object> kafkaKey = null;
            if (consumerRecord.key() != null) {
                kafkaKey = JsonUtils.toObject(String.valueOf(consumerRecord.key()), Map.class);
                kafkaKey = (Map<String, Object>) kafkaKey.get("payload");
            }
            if (kafkaKey == null) {
                LOGGER.warn("没有主键:" + JsonUtils.toJson(consumerRecord));
                continue;
            }
            kafkaToDbService.handleKafkaMessage(acknowledgment, kafkaKey, topicEntity);
        }
    }
}
