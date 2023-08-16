package com.example.water.domain.diary.service;

import com.example.water.domain.comment.repository.CommentRepository;
import com.example.water.domain.diary.dto.response.DiaryResponse;
import com.example.water.domain.diary.dto.request.DiaryRequest;
import com.example.water.domain.user.entity.User;
import com.example.water.domain.user.repository.UserRepository;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiaryService {
    private final ChatgptService chatgptService;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public DiaryResponse writeDiary(Long userId, DiaryRequest diary) {
        String diaryContent = diary.getDiary();
        log.info("diaryContent = {}", diaryContent);

        Long emotionId = deterEmotion(diaryContent);
        log.info("emotionId = {}", emotionId);

        Long myCrystalCount = calcMyCrystalCount(userId);
        log.info("myCrystalCount = {}" + myCrystalCount);

        return DiaryResponse.of(diaryContent, LocalDateTime.now(), emotionId, myCrystalCount);
    }

    // 일기 감정 판별
    public Long deterEmotion(String diary) {
        String deterEmotionPrompt = "너는 감정을 판단하는 로봇이야. 다음 예문이 \"분노, 슬픔, 불안, 외로움, 짜증, 무력감, 수치심, 증오, 스트레스, 불만\" 10 가지 감정 중 어느것에 해당하는지 판단해야 해. 그 감정의 순서를 답해줘.\n" +
                "예를들어 만약 슬픔이라면 2를 출력하면 돼. 부가 설명 없이 숫자만 출력해.\n" +
                "\n" + "\"" + diary + "\"";

        String emotionIdStr = chatgptService.sendMessage(deterEmotionPrompt);
        // 띄어쓰기 들어가면 안됨
        String trimmedEmotionIdStr = emotionIdStr.trim();

        Long emotionId = Long.parseLong(trimmedEmotionIdStr);
        return emotionId;
    }

    // myCrystalCount 계산
    public Long calcMyCrystalCount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Long countMyComment = commentRepository.countByUserId(user);
        Long myCrystalCount = (countMyComment - 1) / 20 + 1;

        return myCrystalCount;
    }
}