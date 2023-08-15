package com.example.water.domain.diary.controller;

import com.example.water.domain.diary.service.DiaryService;
import com.example.water.domain.diary.dto.response.DiaryResponse;
import com.example.water.domain.diary.dto.request.DiaryRequest;
import com.example.water.global.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.water.global.SuccessCode.WRITE_DIARY_SUCCESS;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {
    private final DiaryService diaryService;

    @GetMapping()
    public String test() {
        return "eerr";
    }

    // 일기 작성
    @PostMapping("")
    public BaseResponse<DiaryResponse> writeDiary(@RequestBody DiaryRequest diary) {
        DiaryResponse data = diaryService.writeDiary(diary);
        return BaseResponse.success(WRITE_DIARY_SUCCESS, data);
    }
}