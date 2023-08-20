package com.example.water.domain.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.water.domain.comment.entity.Comment;
import com.example.water.domain.comment.repository.CommentRepository;
import com.example.water.domain.user.dto.UserResponse;
import com.example.water.domain.user.dto.Mypage;
import com.example.water.domain.user.entity.User;
import com.example.water.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final AmazonS3 amazonS3;
    private final String bucketName;

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
        LocalDateTime firstCommentDate = (firstComment != null) ? firstComment.getDate() : LocalDateTime.now();
        LocalDateTime currentDate = LocalDateTime.now();
        long sinceDate = (firstComment != null) ? ChronoUnit.DAYS.between(firstCommentDate, currentDate) : 0;

        // MypageDTO 생성 & 반환
        return Mypage.builder()
                .crystalCount(crystalCount)
                .sinceDate(sinceDate)
                .nickname(user.getNickname())
                .image(user.getImage())
                .build();
    }

    public Map<String, Object> updateImage(Map<String, Object> userInfo, MultipartFile newImage) {
        Long userId = (Long) userInfo.get("userId");
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();

        try {
            // upload Image to S3
            String fileName = UUID.randomUUID() + "_" + newImage.getOriginalFilename();

            amazonS3.putObject(new PutObjectRequest(bucketName, fileName, newImage.getInputStream(), null));

            String newImageUrl = amazonS3.getUrl(bucketName, fileName).toString();

            //update image / save user info
            user.setImage(newImageUrl);
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("profileImageUrl", newImageUrl);
            return response;

        } catch (IOException e) {
            throw new RuntimeException("프로필 이미지 업로드에 실패했습니다.");
        }

    }

    public Map<String, Object> updateImage(Map<String, Object> userInfo) {
        String default_image = "https://media.discordapp.net/attachments/1133490407649591316/1138778386492301463/-04.png?width=662&height=662";
        Long userId = (Long) userInfo.get("userId");
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();

        user.setImage(default_image);
        userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("profileImageUrl", default_image);
        return response;
    }
}
