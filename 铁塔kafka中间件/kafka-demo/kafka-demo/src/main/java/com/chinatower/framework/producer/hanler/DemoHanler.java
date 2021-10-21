package com.chinatower.framework.producer.hanler;

import com.chinatower.framework.mq.kafka.handler.ProducerHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoHanler implements ProducerHandler {
    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {
        log.info("DemoHanler sendTo topic success:{}",producerRecord.toString());
    }

    @Override
    public void onError(ProducerRecord producerRecord, Exception recordMetadata) {
        log.error("DemoHanler sendTo topic error:{}", producerRecord.toString());
    }
}
