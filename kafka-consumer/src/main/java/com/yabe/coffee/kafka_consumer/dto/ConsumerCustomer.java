package com.yabe.coffee.kafka_consumer.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ConsumerCustomer {
    private String id = UUID.randomUUID().toString().substring(0,4);
    private String firstName;
    private String lastName;
    private String email;

}
