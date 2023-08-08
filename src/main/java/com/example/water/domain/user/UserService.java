package com.example.water.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    public User getUserById(Long userId) {
        return userRepository.getById(userId);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public boolean existsEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

