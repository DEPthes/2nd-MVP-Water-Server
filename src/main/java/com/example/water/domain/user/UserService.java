package com.example.water.domain.user;

import com.example.water.domain.comment.Comment;
import com.example.water.domain.comment.CommentRepository;
import com.example.water.domain.user.DTO.MypageResponseDTO;
import com.example.water.domain.user.DTO.UserDto;
import com.example.water.domain.user.DTO.MypageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public Long createUser(UserDto request) {
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


    public MypageDTO getMypage(Map<String, Object> userInfo) {
        Long userId = (Long) userInfo.get("user_id");
        Optional<User> userOptional = userRepository.findById(userId);

        User user = userOptional.orElse(null);

        // 사용자의 크리스탈 count 가져오기
        Long crystalCount = (long) user.getCrystals().size();

        // 첫 content 이후 날짜 수 계산
        Comment firstComment = commentRepository.findFirstByUserIdOrderByDate(user);
        LocalDate firstCommentDate = (firstComment != null) ? firstComment.getDate() : LocalDate.now();
        LocalDate currentDate = LocalDate.now();
        long sinceDate = ChronoUnit.DAYS.between(firstCommentDate, currentDate);

        // MypageDTO 생성 & 반환
        return new MypageDTO(crystalCount, sinceDate, user.getNickname(), user.getImage());

    }
}


