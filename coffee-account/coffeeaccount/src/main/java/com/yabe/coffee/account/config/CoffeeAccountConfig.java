package com.yabe.coffee.account.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CoffeeAccountConfig {

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
