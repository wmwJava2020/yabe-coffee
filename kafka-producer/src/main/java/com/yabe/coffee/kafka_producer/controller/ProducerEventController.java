package com.yabe.coffee.kafka_producer.controller;

import com.yabe.coffee.kafka_producer.dto.ProducerCustomer;
import com.yabe.coffee.kafka_producer.service.CoffeeMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/events")
public class ProducerEventController {

    @Autowired
    private CoffeeMessagePublisher msgPublisher;

    public ProducerEventController() {
    }
     /*
    // Publish via path variables: /publish/{topic}/{message}
    @GetMapping("/publish/{message}")
    public ResponseEntity<?> publishMessage(@PathVariable("message") String msg) {
        try {
            for(int i = 0; i < 1000000; i++) {
            msgPublisher.publishMessageToTopic("coffee-topic-104", msg +":"+ i);
            }
            return ResponseEntity.ok("Message published successfully : " + LocalDate.now());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    } */


    @GetMapping("/publish/customer")
    public void createCustomer(@RequestBody ProducerCustomer customer)  {
        for(int i = 0;i<4;i++) {
            msgPublisher.publishCustomerDataToTopic(customer);
        }
    }



}
