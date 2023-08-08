package com.example.water.domain.chatGpt;

import com.example.water.domain.chatGpt.dto.ChatGptQuestionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

// https://firstws.tistory.com/66

@RestController
@RequestMapping("/today")
@RequiredArgsConstructor
@Slf4j
public class ChatGptController {
    private final ChatGptService aaService;

    @PostMapping(value="ask-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> ask(Locale locale,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestBody ChatGptQuestionRequest chatGptQuestionRequest){
        try {
            return aaService.ask(chatGptQuestionRequest);
        }catch (JsonProcessingException je){
            log.error(je.getMessage());
            return Flux.empty();
        }
    }
}