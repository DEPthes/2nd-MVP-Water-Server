package com.example.water.domain.emotion.repository;

import com.example.water.domain.emotion.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
}