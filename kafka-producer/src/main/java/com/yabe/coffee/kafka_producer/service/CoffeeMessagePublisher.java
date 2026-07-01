package com.yabe.coffee.kafka_producer.service;

import com.yabe.coffee.kafka_producer.dto.ProducerCustomer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class CoffeeMessagePublisher {

    private final KafkaTemplate<String, ProducerCustomer> kafkaTemplate;

    public CoffeeMessagePublisher(KafkaTemplate<String, ProducerCustomer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void publishCustomerDataToTopic(ProducerCustomer customer) {

        try {
            CompletableFuture<SendResult<String, ProducerCustomer>> future = kafkaTemplate.send("custom-topic0", customer);

            //below code help to move forward instead of blocked while waiting for the response
            future.whenComplete((result, ex) -> {
                if (ex == null) {

                        System.out.println("Customer Data Sent : " + customer.toString() + " with offset : " + result.getRecordMetadata().topic());
                } else {
                    System.out.println("Unable to send: " +customer.toString()+ ":  due to :  " + ex.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("Unable to send: " +customer.toString()+ ":  " + e.getMessage());
        }
    }
  /*
    public void publishMessageToTopic(String topic, String message) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);

        //below code help to move forward instead of blocked while waiting for the response
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                System.err.println("Failed to send message: " + ex.getMessage());
            } else {
                System.out.println("Message sent successfully to topic: " + result.getRecordMetadata().topic());
            }
        });
    }

*/

}
