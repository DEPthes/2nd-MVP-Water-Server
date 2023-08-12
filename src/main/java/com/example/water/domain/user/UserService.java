package com.example.water.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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


    public User findByEmail(String email) { return userRepository.findByEmail(email);
    }

    public void updateNickname(Map<String, Object> userInfo, String newNickname){
        String email = (String) userInfo.get("email");
        User user = userRepository.findByEmail(email);
        user.setNickname(newNickname);
        userRepository.save(user); // 수정된 User 객체를 저장

    }
}

