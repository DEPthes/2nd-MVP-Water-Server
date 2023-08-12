package com.example.water.domain.diary.service;

import com.example.water.domain.diary.dto.request.DiaryRequest;
import com.example.water.domain.diary.dto.response.DiaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DiaryService {

    public DiaryRequest writeDiary(DiaryResponse diary) {
        String diaryContent = diary.getDiary();
        return DiaryRequest.of(diaryContent, LocalDateTime.now());
    }
}
