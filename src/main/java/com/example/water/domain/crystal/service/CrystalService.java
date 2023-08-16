package com.example.water.domain.crystal.service;

import com.example.water.domain.comment.dto.response.CommentResponse;
import com.example.water.domain.comment.entity.Comment;
import com.example.water.domain.comment.repository.CommentRepository;
import com.example.water.domain.crystal.dto.response.CrystalResponse;
import com.example.water.domain.crystal.entity.Crystal;
import com.example.water.domain.crystal.repository.CrystalRepository;
import com.example.water.domain.user.entity.User;
import com.example.water.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrystalService {
    private final UserRepository userRepository;
    private final CrystalRepository crystalRepository;
    private final CommentRepository commentRepository;

    public List<CrystalResponse> getCrystalResponses(Map<String, Object> userInfo) {
        String email = (String) userInfo.get("email");
        User user = userRepository.findByEmail(email);
        List<Crystal> crystals = user.getCrystals();

        List<CrystalResponse> crystalResponses = new ArrayList<>();

        for (Crystal crystal : crystals) {
            CrystalResponse dto = CrystalResponse.builder()
                    .crystalId(crystal.getCrystalId())
                    .red(crystal.getRed())
                    .green(crystal.getGreen())
                    .blue(crystal.getBlue())
                    .build();
            crystalResponses.add(dto);
        }

        return crystalResponses;
    }

    // 결정의 모든 답변 조회
    public List<CommentResponse> getAllComments(Long crystalId, User user) {

        Long crystalCount = crystalRepository.getByCrystalId(crystalId).getMyCrystalCount();

        List<Comment> commentList = commentRepository.findAllByMyCrystalCountAndUserId(crystalCount, user);

        List<CommentResponse> commentResponseList = commentList.stream()
                .map(m-> new CommentResponse(m.getDate(), m.getCommentContent()))
                .collect(Collectors.toList());

        return commentResponseList;
    }
}
