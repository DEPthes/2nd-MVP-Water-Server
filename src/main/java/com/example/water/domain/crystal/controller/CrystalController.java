package com.example.water.domain.crystal.controller;

import com.example.water.domain.comment.dto.response.CommentResponse;
import com.example.water.domain.crystal.service.CrystalService;
import com.example.water.domain.crystal.dto.response.CrystalResponse;
import com.example.water.domain.user.entity.User;
import com.example.water.domain.user.service.UserService;
import com.example.water.global.BaseResponse;
import com.example.water.global.ErrorCode;
import com.example.water.global.SuccessCode;
import com.example.water.global.auth.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.example.water.global.SuccessCode.GET_ALL_COMMENTS_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crystal")
public class CrystalController {
    private final CrystalService crystalService;
    private final KakaoService kakaoService;
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponse<List<CrystalResponse>>> getAllCrystal(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String access_token=authorizationHeader.substring(7);

            Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);
            List<CrystalResponse> crystalResponses = crystalService.getCrystalResponses(userInfo);

            return ResponseEntity.ok(BaseResponse.success(SuccessCode.CRYSTAL_INFO_SUCCESS, crystalResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(ErrorCode.REQUEST_VALIDATION_EXCEPTION, "모든 결정 조회에 실패했습니다."));
        }
    }

    // 결정 상세 조회
    @GetMapping("/{crystalId}")
    public BaseResponse<List<CommentResponse>> getAllComments(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("crystalId") Long crystalId) {
        String access_token=authorizationHeader.substring(7);
        Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);
        String email = (String) userInfo.get("email");

        User user = userService.findByEmail(email);//email을 사용하여 DB에서 유저 정보 조회

        List<CommentResponse> allComments = crystalService.getAllComments(crystalId, user);

        return BaseResponse.success(GET_ALL_COMMENTS_SUCCESS, allComments);
    }
}