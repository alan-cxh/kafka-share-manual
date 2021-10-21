package com.alan.kafkaexample.consume.service;

import com.alan.kafkaexample.consume.entity.User;
import com.alan.kafkaexample.consume.utils.JsonUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class KafkaProducer {

    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.producer.topic}")
    public String topicTest;

    public void send(User obj) throws ExecutionException, InterruptedException {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<String, Object>(topicTest, JsonUtil.toJsonString(obj));

        this.send2(obj);

        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(producerRecord);
        this.buildCallback(future);
    }

    public void send2(User obj) throws ExecutionException, InterruptedException {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<String, Object>(topicTest, JsonUtil.toJsonString(obj));
        SendResult<String, Object> stringObjectSendResult = kafkaTemplate.send(producerRecord).get();
        System.out.println(JSON.toJSON(stringObjectSendResult));
    }

    /**
     * 执行消息生产者并
     *
     * @param future
     */
    private void buildCallback(ListenableFuture<SendResult<String, Object>> future) {
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onFailure(Throwable throwable) {
                //发送失败的处理
                log.info(topicTest + " - 生产者 发送消息失败：" + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Object> stringObjectSendResult) {
                //成功的处理
                log.info(topicTest + " - 生产者 发送消息成功：" + stringObjectSendResult.toString());
            }
        });
    }

    public void send1(String key, String jsonStr) {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<String, Object>(topicTest, jsonStr);
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(producerRecord);
        this.buildCallback(future);
    }


}
