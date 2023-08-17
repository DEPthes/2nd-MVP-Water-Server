package com.example.water.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Getter
@AllArgsConstructor
public enum SuccessCode {
    // api 만들고 수정하기
    CUSTOM_SUCCESS(OK, "~ 조회에 성공했습니다."),
    GET_ALL_COMMENTS_SUCCESS(OK, "모든 답변 조회에 성공했습니다."),
    WRITE_DIARY_SUCCESS(OK, "일기 작성에 성공했습니다."),
    CUSTOM_CREATED_SUCCESS(CREATED, "~ 생성에 성공했습니다."),
    USER_INFO_SUCCESS(OK, "사용자 조회에 성공했습니다."),
    EXIT_SUCCESS(OK,"사용자 탈퇴가 완료되었습니다."),
    LOGOUT_SUCCESS(OK, "사용자 로그아웃이 완료되었습니다."),
    CRYSTAL_INFO_SUCCESS(OK,"모든 결정 조회에 성공했습니다."),
    UPDATE_IMAGE_SUCCESS(OK,"프로필 이미지 변경에 성공했습니다."),
    UPDATE_NICKNAME_SUCCESS(OK,"닉네임 변경에 성공했습니다."),
    SAVE_COMMENT_SUCCESS(OK, "답변 저장에 성공했습니다."),
    CREATE_COMMENT_SUCCESS(OK, "답변 생성에 성공했습니다.");


    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}