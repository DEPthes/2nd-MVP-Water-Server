package com.example.water.domain.user.service;

import com.example.water.domain.comment.entity.Comment;
import com.example.water.domain.comment.repository.CommentRepository;
import com.example.water.domain.user.dto.UserResponse;
import com.example.water.domain.user.dto.Mypage;
import com.example.water.domain.user.entity.User;
import com.example.water.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public Long createUser(UserResponse request) {
        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .image(request.getImage())
                .build();

        User savedUser = userRepository.save(user);

        // 생성된 사용자의 ID를 userInfo 객체에 설정
        request.setUserId(savedUser.getUserId());

        return savedUser.getUserId();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Map<String, Object> updateNickname(Map<String, Object> userInfo, String newNickname) {
        String email = (String) userInfo.get("email");
        User user = userRepository.findByEmail(email);

        user.setNickname(newNickname);
        userRepository.save(user);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("newNickname", newNickname);

        return responseData;
    }


    public Mypage getMypage(Map<String, Object> userInfo) {
        Long userId = (Long) userInfo.get("userId");
        Optional<User> userOptional = userRepository.findById(userId);

        User user = userOptional.get();

        // 사용자의 크리스탈 count 가져오기
        Long crystalCount = user.getCrystals() != null ? (long) user.getCrystals().size() : 0L;

        // 첫 content 이후 날짜 수 계산
        Comment firstComment = commentRepository.findFirstByUserIdOrderByDate(user);
        LocalDate firstCommentDate = (firstComment != null) ? firstComment.getDate() : LocalDate.now();
        LocalDate currentDate = LocalDate.now();
        long sinceDate = (firstComment != null) ? ChronoUnit.DAYS.between(firstCommentDate, currentDate) : 0;

        // MypageDTO 생성 & 반환
        return Mypage.builder()
                .crystalCount(crystalCount)
                .sinceDate(sinceDate)
                .nickname(user.getNickname())
                .image(user.getImage())
                .build();
    }
}

