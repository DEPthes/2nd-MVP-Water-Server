package com.example.water.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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

    public Map<String, Object> updateNickname(Map<String, Object> userInfo, String newNickname){
        String email = (String) userInfo.get("email");
        User user = userRepository.findByEmail(email);

        user.setNickname(newNickname);
        userRepository.save(user);

        // 변경된 정보를 응답에 포함시키기 위한 처리
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("user_id", user.getUserId());
        responseData.put("newNickname", newNickname);


        return responseData;

    }
}

