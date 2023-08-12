package com.example.water.domain.comment;

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

    public List<Map<String, Object>> getCommentResponses(Long userId, Long crystalId) {
        Optional<User> user = userRepository.findById(userId);

        List<Comment> comments = user.get().getComments();
        List<Map<String, Object>> commentResponses = new ArrayList<>();

        for (Comment comment : comments) {
            if (comment.getCrystalId().getCrystalId().equals(crystalId)) {
                Map<String, Object> response = new HashMap<>();
                response.put("diary_id",comment.getDiaryId());
                response.put("user_id", comment.getUserId().getUserId());
                response.put("crystal_id", comment.getCrystalId().getCrystalId());
                response.put("content", comment.getContent());
                response.put("date", comment.getDate());
                commentResponses.add(response);
            }
        }
        return commentResponses;
    }

}
