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
    public ResponseEntity<BaseResponse<List<Map<String, Object>>>> getAllComment(@RequestParam("access_token") String access_token, @PathVariable("id") Long id){
        try {
            Map<String, Object> userInfo = kakaoService.getUserInfo(access_token);
            Long userId= (Long) userInfo.get("user_id");
            List<Map<String, Object>> commentResponses = commentService.getCommentResponses(userId, id);

            return ResponseEntity.ok(BaseResponse.success(SuccessCode.CUSTOM_SUCCESS, commentResponses));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(BaseResponse.error(ErrorCode.REQUEST_VALIDATION_EXCEPTION, "모든 답변 조회에 실패했습니다."));
        }

    }

}
