package com.example.water.domain.diary.service;

import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class DiaryServiceTest {

    @Autowired
    private ChatgptService chatgptService;


    // 감정 판단
    @Test
    void deterEmotion() {
        String diary = "오늘 진짜 짜증나는 일이 있었어... 친구랑 만나 기로 약속을 했는데 친구가 나한테 말도 없이 2 시간이나 지각했어. 만났는데 사과도 안하더라. 정말 짜증났는데 분위기를 망치기 싫어서 화도 못냈어... 지금 친구랑 헤어지고 집에 왔는데 기분이 너무 안 좋아. 나를 위로해줘.";
        String deterEmotionPrompt = "너는 감정을 판단하는 로봇이야. 다음 예문이 \"분노, 슬픔, 불안, 외로움, 짜증, 무력감, 수치심, 증오, 스트레스, 불만\" 10 가지 감정 중 어느것에 해당하는지 판단해야 해. 그 감정의 순서를 답해줘.\n" +
                "예를들어 만약 슬픔이라면 2를 출력하면 돼. 부가 설명 없이 숫자만 출력해.\n" +
                "\n" + "\"" + diary + "\"";

        log.info("deterEmotionPrompt = {}", deterEmotionPrompt);

        String emotionIdStr = chatgptService.sendMessage(deterEmotionPrompt);
        // 띄어쓰기 들어가면 안됨
        String trimmedEmotionIdStr = emotionIdStr.trim();

        Long emotionId = Long.parseLong(trimmedEmotionIdStr);
        log.info("emotionId = {}", emotionId);
    }

    @Test
    void 스트링공백제거() {
        String str = " 5";
        String s2 = str.trim();
        Long strTolong = Long.parseLong(s2);
        log.info("strTolong = {}", strTolong);
    }

    @Test
    void gpt단문() {
        String answer = chatgptService.sendMessage("1+14는 뭐야?");
        log.info("answer = {}", answer);
    }

    @Test
    void calcMyCrystalCount() {
        int inputNumber = 67;  // 입력된 숫자

        int range = (inputNumber - 1) / 20 + 1;

        log.info(String.valueOf(range));
    }
}