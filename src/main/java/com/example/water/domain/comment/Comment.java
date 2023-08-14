package com.example.water.domain.comment;

import com.example.water.domain.crystal.Crystal;
import com.example.water.domain.emotion.Emotion;
import com.example.water.domain.user.User;
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
    @Column(name="diaryId")
    private Long diaryId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emotionId", referencedColumnName="emotionId")
    private Emotion emotionId;

    @ManyToOne
    @JoinColumn(name="crystalId")
    private Crystal crystalId;

    @ManyToOne
    @JoinColumn(name="userID")
    private User userId;

    @Column(name="content")
    private String content;

    @Column(name="date")
    private LocalDate date;
}
