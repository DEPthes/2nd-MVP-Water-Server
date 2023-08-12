package com.example.water.domain.comment;

import com.example.water.domain.crystal.Crystal;
import com.example.water.domain.crystal.CrystalRepository;
import com.example.water.domain.user.User;
import com.example.water.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final UserRepository userRepository;
    private final CrystalRepository crystalRepository;
    private final CommentRepository commentRepository;

    public List<Map<String, Object>> getCommentResponses(Long userId, Long crystalId) {
        Optional<User> userOptional = userRepository.findById(userId);

        User user = userOptional.get();

        List<Comment> comments = new ArrayList<>();

        // User의 Crystals에서 해당하는 Crystal을 찾는 부분 추가
        for (Crystal crystal : user.getCrystals()) {
            if (crystal.getCrystalId().equals(crystalId)) {
                comments = crystal.getComments();
                break;
            }
        }

        List<Map<String, Object>> commentResponses = new ArrayList<>();

        for (Comment comment : comments) {
            Map<String, Object> response = new HashMap<>();
            response.put("user_id", comment.getUserId());
            response.put("crystal_id", comment.getCrystalId());
            response.put("content", comment.getContent());
            response.put("date", comment.getDate());
            commentResponses.add(response);
        }
        return commentResponses;
    }

}
