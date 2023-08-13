package com.example.water.domain.diary.service;

import com.example.water.domain.comment.repository.CommentRepository;
import com.example.water.domain.diary.dto.response.DiaryResponse;
import com.example.water.domain.diary.dto.request.DiaryRequest;
import com.example.water.domain.emotion.Emotion;
import com.example.water.domain.emotion.repository.EmotionRepository;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryService {
    private final ChatgptService chatgptService;
    private final EmotionRepository emotionRepository;
    private final CommentRepository commentRepository;

    public DiaryResponse writeDiary(DiaryRequest diary) {
        String diaryContent = diary.getDiary();

        Long emotionId = deterEmotion(diaryContent);

        Long myCrystalCount = calcMyCrystalCount();

        return DiaryResponse.of(diaryContent, LocalDateTime.now(), emotionId, myCrystalCount);
    }

    // 일기 감정 판별
    public Long deterEmotion(String diary) {
        String emotion = chatgptService.sendMessage(diary);

        List<Emotion> allEmotion = emotionRepository.findAll();

        Long emotionId = -1L;

        for (Emotion e : allEmotion) {
            // gpt가 판별한 감정값에 db에 있는 값 포함된 경우
            if (emotion.contains(e.getFeeling())) {
                emotionId = e.getEmotionId();
            }
        }
        return emotionId;
    }

    // myCrystalCount 계산
    public Long calcMyCrystalCount() {
        Long countMyComment = commentRepository.countByUserId(1L);
        int length = (int)(Math.log10(countMyComment)+1);
        Long myCrystalCount = Long.valueOf(length);

        return myCrystalCount;
    }
}
