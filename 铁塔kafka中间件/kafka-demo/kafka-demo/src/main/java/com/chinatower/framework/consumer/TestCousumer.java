package com.chinatower.framework.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;

public class TestCousumer {
	  @Autowired
	  private KafkaListenerEndpointRegistry registry;
	  public void stop(String kafkaListenerId) {
		    registry.getListenerContainer(kafkaListenerId).stop();
	  }

	 public void start(String kafkaListenerId) {
		    registry.getListenerContainer(kafkaListenerId).start();
	 }
}
