package com.example.water.domain.comment.controller;

import com.example.water.domain.comment.dto.request.CommentRequest;
import com.example.water.domain.comment.dto.request.SaveRequest;
import com.example.water.domain.comment.service.CommentService;
import com.example.water.domain.user.entity.User;
import com.example.water.domain.user.service.UserService;
import com.example.water.global.BaseResponse;
import com.example.water.global.auth.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;
import java.util.Map;
import static com.example.water.global.SuccessCode.SAVE_COMMENT_SUCCESS;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;
    private final KakaoService kakaoService;
    private final UserService userService;

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

    // 닫기 눌렀을 때 (답변 저장)
    @PostMapping("/save")
    public BaseResponse<Void> save(@RequestHeader("Authorization") String authorizationHeader, @RequestBody SaveRequest saveRequest) {
        String access_token=authorizationHeader.substring(7);
        Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);
        String email = (String) userInfo.get("email");

        User user = userService.findByEmail(email);//email을 사용하여 DB에서 유저 정보 조회

        Long userId = user.getUserId();

        commentService.save(userId, saveRequest);

        return BaseResponse.success(SAVE_COMMENT_SUCCESS);
    }
}