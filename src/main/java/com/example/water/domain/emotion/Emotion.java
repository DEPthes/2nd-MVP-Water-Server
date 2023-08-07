package com.example.water.domain.emotion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="EMOTION")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Emotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="emotionId")
    private Long emotionId;

    @Column(name="feeling")
    private String feeling;

    @Column(name="red")
    private Long red;

    @Column(name="green")
    private Long green;

    @Column(name="blue")
    private Long blue;

}
