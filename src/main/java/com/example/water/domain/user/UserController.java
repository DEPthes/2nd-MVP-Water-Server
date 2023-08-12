package com.example.water.domain.user;

import com.example.water.global.BaseResponse;
import com.example.water.global.ErrorCode;
import com.example.water.global.SuccessCode;
import com.example.water.global.auth.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @PostMapping("nickname")
    public ResponseEntity<BaseResponse<Map<String,Object>>> updateNickname(
            @RequestParam("access_token") String access_token,
            @RequestBody Map<String, String> requestBody) {
        try {
            Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);

            String newNickname = requestBody.get("newNickname");
            Map<String, Object> responseData = userService.updateNickname(userInfo, newNickname);

            return ResponseEntity.ok(BaseResponse.success(SuccessCode.CUSTOM_SUCCESS,responseData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(ErrorCode.REQUEST_VALIDATION_EXCEPTION, "닉네임 변경 실패"));
        }

    }


}
