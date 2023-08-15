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
    WRITE_DIARY_SUCCESS(OK, "일기 작성에 성공했습니다."),
    SAVE_COMMENT_SUCCESS(OK, "답변 저장에 성공했습니다."),
    CUSTOM_CREATED_SUCCESS(CREATED, "~ 생성에 성공했습니다.");


    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}