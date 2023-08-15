package com.example.water.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

// https://velog.io/@dldydrhkd/The-dependencies-of-some-of-the-beans-in-the-application-context-form-a-cycle

@Configuration
public class RestTemplateConfig {
    /*
        rest Config
     */

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
