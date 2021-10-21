package com.demo.consumer.filter;

import com.chinatower.framework.mq.kafka.filter.CustomFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerFilter implements CustomFilter {
    @Override
    public boolean rule(ConsumerRecord consumerRecord) {
        log.info("topic is {},value is {}", consumerRecord.topic(), consumerRecord.value());
        return false;
    }
}
