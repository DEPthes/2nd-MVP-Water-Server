package com.example.water.domain.chatGpt.config;

import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Slf4j
@Configuration
public class ChatGptConfig {
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String CHAT_MODEL = "gpt-3.5-turbo";
    public static final Integer MAX_TOKEN = 300;
    public static final Boolean STREAM_TRUE = true;
    public static final Boolean STREAM_FALSE = false;
    public static final String ROLE = "user";
    public static final Double TEMPERATURE = 0.6;
    //public static final Double TOP_P = 1.0;
    public static final String MEDIA_TYPE = "application/json; charset=UTF-8";
    //completions : 질답
    public static final String CHAT_URL = "https://api.openai.com/v1/chat/completions";

    @Value("${GPT_TOKEN}")
    private String token;

//    @Bean
//    public OpenAiService openAiService() {
//        log.info("token: {}을 활용한 OpenApiService를 생성합니다!", token);
//        return new OpenAiService(token, Duration.ofSeconds(60));
//    }
}