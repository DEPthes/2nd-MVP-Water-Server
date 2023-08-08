package com.example.water.global.auth;
import com.example.water.domain.user.UserDto;
import com.example.water.domain.user.UserService;
import com.example.water.global.BaseResponse;
import com.example.water.global.SuccessCode;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class KakaoController {

    @Autowired
    KakaoService s;
    @Autowired
    UserService userService;

    @Value("${kakao.client.id}")
    private String CLIENT_ID;
    @Value("${kakao.client.secret}")
    private String CLIENT_SECRET;
    @Value("${kakao.redirect.url}")
    private String REDIRECT_URL;

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("CLIENT_ID", CLIENT_ID);
        model.addAttribute("REDIRECT_URL", REDIRECT_URL);
        return "login";
    }
    @GetMapping("/kakao")
    public ResponseEntity<BaseResponse<UserDto>> getUI(@RequestParam String code) throws IOException, ParseException {
        String default_image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBMnebXD_QrhqKXKApXSZ7fbUlFaElymTpgQ&usqp=CAU";
        String access_token = s.getToken(code);
        Map<String, Object> userInfoMap = s.getUserInfo(access_token, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBMnebXD_QrhqKXKApXSZ7fbUlFaElymTpgQ&usqp=CAU");
        UserDto userInfo = UserDto.builder()
                .nickname((String) userInfoMap.get("nickname"))
                .image((String) userInfoMap.get("profileImage"))
                .email((String) userInfoMap.get("email"))
                .build();

        String email = userInfo.getEmail();

        if (!userService.existsEmail(email)) {
            userService.createUser(userInfo);
        }
        // BaseResponse 객체를 생성하여 JSON 응답 데이터 구성
        BaseResponse<UserDto> response = BaseResponse.success(SuccessCode.CUSTOM_SUCCESS, userInfo);

        // ResponseEntity를 사용하여 HTTP 상태 코드와 함께 응답 데이터 반환
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}


