package com.example.water.domain.comment.service;

import com.example.water.domain.chatGpt.ChatGptMessage;
import com.example.water.domain.chatGpt.config.ChatGptConfig;
import com.example.water.domain.chatGpt.dto.ChatGptQuestionRequest;
import com.example.water.domain.chatGpt.dto.ChatGptRequest;
import com.example.water.domain.chatGpt.dto.ChatGptResponse;
import com.example.water.domain.comment.Comment;
import com.example.water.domain.comment.dto.request.Color;
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

    // 위로 답안 stream
    public Flux<String> comfortComment(CommentRequest commentRequest) throws JsonProcessingException {

        WebClient client = WebClient.builder()
                .baseUrl(ChatGptConfig.CHAT_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey)
                .build();


        String content = commentRequest.getDiary() + "위 상황에 대해서 다정한 말투로 편을 들어주는 위로를 해줘. 편지쓰는 것처럼 하지 마. 처음에 인사하지마. 네가 ChatGPT인 티를 내지 말고, 사람처럼 말해. 반말은 하지마. 끝에 감사인사도 하지마. 세줄로 답변해줘.";


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

    // 편들기 답안 stream
    public Flux<String> mysideComment(CommentRequest commentRequest) throws JsonProcessingException {

        WebClient client = WebClient.builder()
                .baseUrl(ChatGptConfig.CHAT_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + apiKey)
                .build();


        String content = commentRequest.getDiary() + "위 상황에 대해서 내 편을 들어주고, 상대방 욕해줘. 상대방이 잘못했다고 말해줘. 편지쓰는 것처럼 하지 마. 처음에 인사하지마. 네가 ChatGPT인 티를 내지 말고, 사람처럼 말해. 반말은 하지마. 끝에 감사인사도 하지마. 세줄로 답변해줘.";


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
    public void save(Long userId, SaveRequest saveRequest) {
        Long myCrystalCount = saveRequest.getMyCrystalCount();
        Emotion emotion = emotionRepository.findById(saveRequest.getEmotionId()).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        Comment comment = Comment.of(saveRequest.getComment(), myCrystalCount, emotion, user);
        commentRepository.save(comment);

        Long countMyComment = commentRepository.countByUserId(user);
        // 20개째면 결정 저장 (20, 40, 60 ..)
        if (countMyComment % 20 == 0) {

            Long crystalCount = (countMyComment / 20);
            Color color = calcCrytalColor(crystalCount);

            // 색깔 계산
            Crystal crystal = Crystal.of(user, color.getRed(), color.getGreen(), color.getBlue());
            crystalRepository.save(crystal);
        }
    }

    // 답변 20개 평균 색깔 계산 (rgb)
    public Color calcCrytalColor(Long crystalCount) {
        // 답변 20개 가져오기
        List<Comment> commentList = commentRepository.findAllByMyCrystalCount(crystalCount);

        Long red = 0L;
        Long green = 0L;
        Long blue = 0L;

        for (Comment c : commentList) {
            red += c.getEmotionId().getRed();
            green += c.getEmotionId().getGreen();
            green += c.getEmotionId().getBlue();
        }

        return Color.of(red, green, blue);
    }
}