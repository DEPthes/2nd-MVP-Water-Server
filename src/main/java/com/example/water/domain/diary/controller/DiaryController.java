package com.example.water.domain.diary.controller;

import com.example.water.domain.diary.service.DiaryService;
import com.example.water.domain.diary.dto.response.DiaryResponse;
import com.example.water.domain.diary.dto.request.DiaryRequest;
import com.example.water.domain.user.User;
import com.example.water.domain.user.UserService;
import com.example.water.global.BaseResponse;
import com.example.water.global.auth.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.water.global.SuccessCode.WRITE_DIARY_SUCCESS;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final KakaoService kakaoService;
    private final DiaryService diaryService;
    private final UserService userService;

    @GetMapping()
    public String test() {
        return "eerr";
    }

    // 일기 작성
    @PostMapping("")
    public BaseResponse<DiaryResponse> writeDiary(@RequestHeader("Authorization") String authorizationHeader, @RequestBody DiaryRequest diary) {
        String access_token=authorizationHeader.substring(7);
        Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);
        String email = (String) userInfo.get("email");

        User user = userService.findByEmail(email);//email을 사용하여 DB에서 유저 정보 조회

        Long userId = user.getUserId();

        DiaryResponse data = diaryService.writeDiary(userId, diary);
        return BaseResponse.success(WRITE_DIARY_SUCCESS, data);
    }
}