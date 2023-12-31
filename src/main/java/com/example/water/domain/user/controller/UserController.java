package com.example.water.domain.user.controller;

import com.example.water.domain.user.service.UserService;
import com.example.water.domain.user.dto.Mypage;
import com.example.water.global.BaseResponse;
import com.example.water.global.ErrorCode;
import com.example.water.global.SuccessCode;
import com.example.water.global.auth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Mypage>> mypage(@RequestHeader("Authorization") String authorizationHeader){
        try {
            String access_token = authorizationHeader.substring(7);

            Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);
            Mypage mypageResponse = userService.getMypage(userInfo);

            return ResponseEntity.ok(BaseResponse.success(SuccessCode.USER_INFO_SUCCESS, mypageResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(ErrorCode.REQUEST_VALIDATION_EXCEPTION, "마이페이지 조회에 실패했습니다."));
        }
    }

    @PatchMapping("/nickname")
    public ResponseEntity<BaseResponse<Map<String,Object>>> updateNickname(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Map<String, String> requestBody) {
        try {
            String access_token=authorizationHeader.substring(7);
            Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);

            String newNickname = requestBody.get("newNickname");
            Map<String, Object> updateNicknameResponse = userService.updateNickname(userInfo, newNickname);

            return ResponseEntity.ok(BaseResponse.success(SuccessCode.UPDATE_NICKNAME_SUCCESS,updateNicknameResponse));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(ErrorCode.REQUEST_VALIDATION_EXCEPTION, "닉네임 변경에 실패했습니다."));
        }
    }

    @PatchMapping("/image")
    public ResponseEntity<BaseResponse<Map<String,Object>>> updateImage(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam("image") MultipartFile newImage){
        try{
            String access_token=authorizationHeader.substring(7);
            Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);

            Map<String, Object> updateImageResponse = userService.updateImage(userInfo, newImage);

            return ResponseEntity.ok(BaseResponse.success(SuccessCode.UPDATE_IMAGE_SUCCESS,updateImageResponse));
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(ErrorCode.REQUEST_VALIDATION_EXCEPTION, "프로필 이미지 변경에 실패했습니다."));
        }
    }

    @PatchMapping("/default-image")
    public ResponseEntity<BaseResponse<Map<String,Object>>> updateImage_default( @RequestHeader("Authorization") String authorizationHeader ){
        try{
            String access_token=authorizationHeader.substring(7);
            Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);

            Map<String, Object> updateImageResponse = userService.updateImage(userInfo);

            return ResponseEntity.ok(BaseResponse.success(SuccessCode.UPDATE_IMAGE_SUCCESS,updateImageResponse));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(ErrorCode.REQUEST_VALIDATION_EXCEPTION, "프로필 이미지 변경에 실패했습니다."));
        }
    }
}
