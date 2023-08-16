package com.example.water.domain.crystal.repository;

import com.example.water.domain.crystal.entity.Crystal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrystalRepository extends JpaRepository<Crystal, Long> {
    Crystal getByCrystalId(Long crystalId);
}