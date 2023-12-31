package com.example.water.global.auth.controller;
import com.example.water.domain.user.dto.UserResponse;
import com.example.water.domain.user.service.UserService;
import com.example.water.domain.user.entity.User;
import com.example.water.global.BaseResponse;
import com.example.water.global.ErrorCode;
import com.example.water.global.SuccessCode;
import com.example.water.global.auth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class KakaoController {

    private final KakaoService kakaoService;
    private final UserService userService;

    @Value("${kakao.client.id}")
    private String CLIENT_ID;
    @Value("${kakao.client.secret}")
    private String CLIENT_SECRET;
    @Value("${kakao.redirect.url}")
    private String REDIRECT_URL;

    @GetMapping("/login")
    public String kakaoLogin() {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URL +
                "&response_type=code" +
                "&scope=profile_nickname profile_image account_email";

        return "redirect:" + kakaoLoginUrl;
    }

    @ResponseBody
    @GetMapping("/kakao")
    public ResponseEntity<BaseResponse<UserResponse>> getUI(@RequestParam String code) throws IOException, ParseException {
        String default_image = "https://media.discordapp.net/attachments/1133490407649591316/1138778386492301463/-04.png?width=662&height=662";
        String access_token = kakaoService.getToken(code);
        Map<String, Object> userInfoMap = kakaoService.getUserInfo(access_token);
        UserResponse userInfo = UserResponse.builder()
                .userId((Long) userInfoMap.get("userId"))
                .nickname((String) userInfoMap.get("nickname"))
                .image((String) userInfoMap.get("profileImage"))
                .email((String) userInfoMap.get("email"))
                .token(access_token)
                .build();

        String email = userInfo.getEmail();
        User user = userService.findByEmail(email);

        if (user == null) {
            String profileImage = (String) userInfoMap.get("profileImage");
            if (profileImage == null || profileImage.isEmpty()) {
                userInfo.setImage(default_image);
            } else {
                userInfo.setImage(profileImage);
            }
            userService.createUser(userInfo);// 새로운 사용자 정보를 DB에 등록
        }
        // BaseResponse 객체를 생성하여 JSON 응답 데이터 구성
        BaseResponse<UserResponse> response = BaseResponse.success(SuccessCode.USER_INFO_SUCCESS, userInfo);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ResponseBody
    @PostMapping("/exit")
    public ResponseEntity<BaseResponse<String>> exitKakao(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String access_token=authorizationHeader.substring(7);
            Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);
            String email = (String) userInfo.get("email");

            User user = userService.findByEmail(email);//email을 사용하여 DB에서 유저 정보 조회

            Long userId = user.getUserId();
            kakaoService.deleteUser(access_token, userId);
            BaseResponse<String> response = BaseResponse.success(SuccessCode.EXIT_SUCCESS);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            BaseResponse<String> errorResponse = BaseResponse.error(ErrorCode.REQUEST_VALIDATION_EXCEPTION, "계정 탈퇴에 실패하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @ResponseBody
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<String>> logout(@RequestHeader("Authorization") String authorizationHeader) {

        String access_token=authorizationHeader.substring(7);
        // 카카오 로그아웃 API 호출
        kakaoService.logout(access_token);

        // 로그아웃 성공 메시지를 응답으로 전송
        BaseResponse<String> response = BaseResponse.success(SuccessCode.LOGOUT_SUCCESS);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}


