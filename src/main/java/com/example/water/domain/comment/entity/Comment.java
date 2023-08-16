package com.example.water.domain.comment.entity;

import com.example.water.domain.emotion.Emotion;
import com.example.water.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name="COMMENT")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="commentId")
    private Long commentId;

    @Column(name="commentContent")
    private String commentContent;

    @Column(name="myCrystalCount")
    private Long myCrystalCount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotionId", referencedColumnName="emotionId")
    private Emotion emotionId;

    @ManyToOne
    @JoinColumn(name="userId")
    private User userId;

    @Column(name="date")
    private LocalDate date;

    public static Comment of(String commentContent, Long myCrystalCount, Emotion emotionId, User userId) {
        return Comment.builder()
                .commentContent(commentContent)
                .myCrystalCount(myCrystalCount)
                .emotionId(emotionId)
                .userId(userId)
                .date(LocalDate.now())
                .build();
    }
}