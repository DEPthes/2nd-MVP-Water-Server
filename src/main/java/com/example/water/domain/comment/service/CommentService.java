package com.example.water.domain.comment.service;

import com.example.water.domain.chatGpt.ChatGptMessage;
import com.example.water.domain.chatGpt.config.ChatGptConfig;
import com.example.water.domain.chatGpt.dto.ChatGptQuestionRequest;
import com.example.water.domain.chatGpt.dto.ChatGptRequest;
import com.example.water.domain.chatGpt.dto.ChatGptResponse;
import com.example.water.domain.comment.Comment;
import com.example.water.domain.comment.dto.request.CommentRequest;
import com.example.water.domain.comment.dto.request.SaveRequest;
import com.example.water.domain.comment.repository.CommentRepository;
import com.example.water.domain.crystal.Crystal;
import com.example.water.domain.crystal.repository.CrystalRepository;
import com.example.water.domain.emotion.Emotion;
import com.example.water.domain.emotion.repository.EmotionRepository;
import com.example.water.domain.user.User;
import com.example.water.domain.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final EmotionRepository emotionRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CrystalRepository crystalRepository;
    @Autowired
    private RestTemplate restTemplate;


    @Value("${GPT_TOKEN}")
    private String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);


    // chat gpt 단답
    public HttpEntity<ChatGptRequest> buildHttpEntity(ChatGptRequest chatGptRequest) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        httpHeaders.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey);
        return new HttpEntity<>(chatGptRequest, httpHeaders);
    }

    // 답안 stream
    public Flux<String> comment(CommentRequest commentRequest) throws JsonProcessingException {

        WebClient client = WebClient.builder()
                .baseUrl(ChatGptConfig.CHAT_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey)
                .build();


        String content = commentRequest.getDiary() + "위 내용에 대한 위로를 해줘. 한줄로 짧게 해줘.";


        List<ChatGptMessage> messages = new ArrayList<>();
        messages.add(ChatGptMessage.builder()
                .role(ChatGptConfig.ROLE)
                .content(content)
                .build());
        ChatGptRequest chatGptRequest = new ChatGptRequest(
                ChatGptConfig.CHAT_MODEL,
                ChatGptConfig.MAX_TOKEN,
                ChatGptConfig.TEMPERATURE,
                ChatGptConfig.STREAM_TRUE,
                messages
                //ChatGptConfig.TOP_P
        );
        String requestValue = objectMapper.writeValueAsString(chatGptRequest);

        Flux<String> eventStream = client.post()
                .bodyValue(requestValue)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);

        // 답안
        return eventStream;
    }

    // gpt 단답에 사용됨
    public ChatGptResponse getResponse(HttpEntity<ChatGptRequest> chatGptRequestHttpEntity) {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(60000);
        //답변이 길어질 경우 TimeOut Error가 발생하니 1분정도 설정해줍니다.
        requestFactory.setReadTimeout(60 * 1000);   //  1min = 60 sec * 1,000ms
        restTemplate.setRequestFactory(requestFactory);

        ResponseEntity<ChatGptResponse> responseEntity = restTemplate.postForEntity(
                ChatGptConfig.CHAT_URL,
                chatGptRequestHttpEntity,
                ChatGptResponse.class);

        return responseEntity.getBody();
    }

    // gpt 단답
    public ChatGptResponse askQuestion(ChatGptQuestionRequest questionRequest) {
        List<ChatGptMessage> messages = new ArrayList<>();
        messages.add(ChatGptMessage.builder()
                .role(ChatGptConfig.ROLE)
                .content(questionRequest.getQuestion())
                .build());
        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequest(
                                ChatGptConfig.CHAT_MODEL,
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.STREAM_FALSE,
                                messages
                                //ChatGptConfig.TOP_P
                        )
                )
        );
    }

    // 클라가 답변 전문, 감정 id, 결정 id 보내주면 한꺼번에 저장 (닫기 눌렀을 때)
    public void save(SaveRequest saveRequest) {
        Emotion emotion = emotionRepository.findById(saveRequest.getEmotionId()).orElseThrow();
        Long myCrystalCount = saveRequest.getMyCrystalCount();
        User user = userRepository.findById(1L).orElseThrow();

        Comment comment = Comment.of(emotion, myCrystalCount, user, saveRequest.getComment());
        commentRepository.save(comment);

        // ???????
        Long countMyComment = commentRepository.countByUserId(user);
        // 10개째면 결정 저장
        if (countMyComment == 10) {

            // 색깔 계산
            Crystal crystal = Crystal.of(user, 1L, 1L, 1L);
            crystalRepository.save(crystal);
        }
    }

    // 색깔 계산 (rgb)
    public void calcCrytalColor() {

    }
}