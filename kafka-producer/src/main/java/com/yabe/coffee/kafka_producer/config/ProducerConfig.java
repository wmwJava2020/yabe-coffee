package com.yabe.coffee.kafka_producer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerConfig {


    @Bean
    public NewTopic createNewTopic(){
        return new NewTopic("custom-topic0", 1, (short) 1);
    }
}

