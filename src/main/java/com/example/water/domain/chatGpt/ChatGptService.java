package com.example.water.domain.chatGpt;

import com.example.water.domain.chatGpt.config.ChatGptConfig;
import com.example.water.domain.chatGpt.dto.ChatGptQuestionRequest;
import com.example.water.domain.chatGpt.dto.ChatGptRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGptService {
    @Value("${GPT_TOKEN}")
    private String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE );
    public Flux<String> ask(ChatGptQuestionRequest chatGptQuestionRequest) throws JsonProcessingException {

        WebClient client = WebClient.builder()
                .baseUrl(ChatGptConfig.CHAT_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey)
                .build();

        List<ChatGptMessage> messages = new ArrayList<>();
        messages.add(ChatGptMessage.builder()
                .role(ChatGptConfig.ROLE)
                .content(chatGptQuestionRequest.getQuestion())
                .build());
        ChatGptRequest chatGptRequest = new ChatGptRequest(
                ChatGptConfig.CHAT_MODEL,
                ChatGptConfig.MAX_TOKEN,
                ChatGptConfig.TEMPERATURE,
                ChatGptConfig.STREAM,
                messages
                //ChatGptConfig.TOP_P
        );
        String requestValue = objectMapper.writeValueAsString(chatGptRequest);

        Flux<String> eventStream = client.post()
                .bodyValue(requestValue)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
        return eventStream;
    }
}