package com.example.water.global.auth;
import com.example.water.domain.user.UserDto;
import com.example.water.domain.user.UserService;
import com.example.water.global.BaseResponse;
import com.example.water.global.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
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

    // test
    @GetMapping("/test")
    public String test() {
        return "test!!!";
    }

    @GetMapping("/login")
    public String kakaoLogin() {
        String kakaoLoginUrl = "https://kauth.kakao.com/oauth/authorize" +
                "?client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URL +
                "&response_type=code" +
                "&scope=profile_nickname profile_image account_email";

        return "redirect:" + kakaoLoginUrl;
    }

    @GetMapping("/kakao")
    public ResponseEntity<BaseResponse<UserDto>> getUI(@RequestParam String code) throws IOException, ParseException {
        String default_image = "https://media.discordapp.net/attachments/1133490407649591316/1138778386492301463/-04.png?width=662&height=662";
        String access_token = kakaoService.getToken(code);
        Map<String, Object> userInfoMap = kakaoService.getUserInfo(access_token, default_image);
        UserDto userInfo = UserDto.builder()
                .nickname((String) userInfoMap.get("nickname"))
                .image((String) userInfoMap.get("profileImage"))
                .email((String) userInfoMap.get("email"))
                .build();

        String email = userInfo.getEmail();

        if (!userService.existsEmail(email)) {
            String profileImage = (String) userInfoMap.get("profileImage");
            if (profileImage != null) {
                userInfo.setImage(profileImage);
            } else {
                userInfo.setImage(default_image);
            }
            userService.createUser(userInfo);
        }
        // BaseResponse 객체를 생성하여 JSON 응답 데이터 구성
        BaseResponse<UserDto> response = BaseResponse.success(SuccessCode.CUSTOM_SUCCESS, userInfo);

        // ResponseEntity를 사용하여 HTTP 상태 코드와 함께 응답 데이터 반환
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}


