package com.example.water.domain.crystal;

import com.example.water.domain.comment.CommentService;
import com.example.water.domain.user.User;
import com.example.water.global.BaseResponse;
import com.example.water.global.ErrorCode;
import com.example.water.global.SuccessCode;
import com.example.water.global.auth.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@RestController
@RequiredArgsConstructor
@RequestMapping("/crystal")
public class CrystalController {
    private final CrystalService crystalService;
    private final KakaoService kakaoService;
    private final CommentService commentService;

    @GetMapping("/all")
    public ResponseEntity<BaseResponse<List<Map<String, Object>>>> getAllCrystal(@RequestParam("access_token") String access_token) {
        try {
            Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);

            Long userId= (Long) userInfo.get("user_id");
            List<Map<String, Object>> crystalResponses = crystalService.getCrystalResponses(userId);

            return ResponseEntity.ok(BaseResponse.success(SuccessCode.CUSTOM_SUCCESS, crystalResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(ErrorCode.REQUEST_VALIDATION_EXCEPTION, "모든 결정 조회에 실패했습니다."));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<Map<String, Object>>> getAllComment(@RequestParam("access_token") String access_token, @PathVariable("id") Long id){
        try {
            // 카카오 로그인 토큰 값의 이메일을 사용하여 user_id 조회
            Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);
            String idStr = (String) userInfo.get("id");
            Long userId = Long.parseLong(idStr);

            // user_id, crystal_id에 대한 Comment 객체 조회
            List<Map<String, Object>> commentResponses = commentService.getComment(userId, id);

            // 응답 데이터 구성
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("comment_list", commentResponses);

            // 응답 반환
            return ResponseEntity.ok(BaseResponse.success(SuccessCode.CUSTOM_SUCCESS, responseData));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(ErrorCode.REQUEST_VALIDATION_EXCEPTION, "모든 답변 조회에 실패했습니다."));
        }

    }

}
