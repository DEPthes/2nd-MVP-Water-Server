package com.example.water.domain.comment.controller;

import com.example.water.domain.comment.dto.request.CommentRequest;
import com.example.water.domain.comment.service.CommentService;
import com.example.water.global.BaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    // 위로 답변 받기
    @PostMapping(value="comfort", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> comfortComment(Locale locale,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestBody CommentRequest commentRequest){
        try {
            return commentService.comfortComment(commentRequest);
        }catch (JsonProcessingException je){
            log.error(je.getMessage());
            return Flux.empty();
        }
    }

    // 편들기 답변 받기
    @PostMapping(value="myside", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> mysideComment(Locale locale,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestBody CommentRequest commentRequest){
        try {
            return commentService.mysideComment(commentRequest);
        }catch (JsonProcessingException je){
            log.error(je.getMessage());
            return Flux.empty();
        }
    }

    // 닫기 눌렀을 때
    @PostMapping("/save")
    public BaseResponse<Void> save(String comment) {

        return null;
    }
}