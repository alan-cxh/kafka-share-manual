package com.demo.consumer;

import com.chinatower.framework.mq.kafka.consumer.ConsumerMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DemoConsumer implements ConsumerMessage {




    @Override
    public <T> T ack(ConsumerRecord consumerRecord) {
        String topic = consumerRecord.topic();
        if (topic.equals("ttcv")) {
        } else if (topic.equals("topic_CHNTRMS_IDMM")) {
        } else if (topic.equals("topic_IDMM_CHNTRMS")){
        }

        return null;
    }
}
