package com.example.water.global.auth;
import com.example.water.domain.user.UserDto;
import com.example.water.domain.user.UserService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public String getUI(@RequestParam String code, Model model) throws IOException, ParseException {
        String default_image = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBMnebXD_QrhqKXKApXSZ7fbUlFaElymTpgQ&usqp=CAU";
        String access_token = s.getToken(code);

        Map<String, Object> userInfoMap = s.getUserInfo(access_token, default_image);
        UserDto userInfo = new UserDto();
        userInfo.setNickname((String) userInfoMap.get("nickname"));
        userInfo.setImage((String) userInfoMap.get("profileImage"));
        userInfo.setEmail((String) userInfoMap.get("email"));

        String email = userInfo.getEmail();
        String nickname = userInfo.getNickname();
        String profileImage = userInfo.getImage();

        if (profileImage == null) {
            profileImage = default_image;
        }

        if(!userService.existsEmail(email)){
            userService.createUser(userInfo);
        }

        model.addAttribute("code", code);
        model.addAttribute("access_token", access_token);
        model.addAttribute("userInfo", userInfo);

        return "index"; //로그인 결과 확인을 위함.
    }
}


