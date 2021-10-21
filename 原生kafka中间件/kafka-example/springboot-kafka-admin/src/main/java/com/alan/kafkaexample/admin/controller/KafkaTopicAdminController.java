package com.alan.kafkaexample.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * @author Administrator
 */
@RestController
@RequestMapping("admin")
@Slf4j
public class KafkaTopicAdminController {

    @RequestMapping("listAllTopic")
    public void listAllTopic() {
        AdminClient adminClient = KafkaAdminClient.create(this.getProps());
        ListTopicsResult result = adminClient.listTopics();
        KafkaFuture<Set<String>> names = result.names();
        try {
            names.get().forEach(log::info);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        adminClient.close();
    }


    private Properties getProps() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.60.128:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }
}
