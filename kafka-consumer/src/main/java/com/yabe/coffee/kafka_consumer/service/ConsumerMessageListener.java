package com.yabe.coffee.kafka_consumer.service;

import com.yabe.coffee.kafka_consumer.dto.ConsumerCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerMessageListener {

   Logger logger = LoggerFactory.getLogger(ConsumerMessageListener.class);

   @KafkaListener(topics = "custom-topic0", groupId = "customer-group")
   public void customerMessage(ConsumerCustomer customer){
      logger.info("Message received: {}", customer.toString());
    }

   /*
   @KafkaListener(topics = "coffee-topic-104", groupId = "consumer-group-1")
   void consumeMessage(String message) {
      logger.info("Message received: {}", message +":"+ "from CG1");
   }

   @KafkaListener(topics = "coffee-topic-104", groupId = "consumer-group-2")
   void consumeMessageCG1(String message) {
      logger.info("Message received: {}", message +":"+ "from CG2");
   }

   @KafkaListener(topics = "coffee-topic-104", groupId = "consumer-group-3")
   void consumeMessageCG2(String message) {
      logger.info("Message received: {}", message +":"+ "from CG3");
   }
  */



}
