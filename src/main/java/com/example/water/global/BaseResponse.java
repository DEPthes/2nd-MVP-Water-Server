package com.example.water.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.reactivex.annotations.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private final int status;
    private final String message;
    // null인 데이터는 json결과에 나타나지 않음
    // https://junho85.pe.kr/1626
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private BaseResponse() {
        throw new IllegalStateException();
    }

    public static <T> BaseResponse<T> success(SuccessCode success) {
        return new BaseResponse<>(success.getHttpStatusCode(), success.getMessage());
    }

    public static <T> BaseResponse<T> success(SuccessCode successCode, T data) {
        return new BaseResponse<>(successCode.getHttpStatusCode(), successCode.getMessage(), data);
    }

    public static <T> BaseResponse<T> error(ErrorCode error) {
        return new BaseResponse<>(error.getHttpStatusCode(), error.getMessage());
    }

    public static <T> BaseResponse<T> error(ErrorCode error, @Nullable String message) {
        return new BaseResponse<>(error.getHttpStatusCode(), message);
    }
}