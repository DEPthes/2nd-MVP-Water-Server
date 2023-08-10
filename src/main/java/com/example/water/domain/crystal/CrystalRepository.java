package com.example.water.domain.crystal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrystalRepository extends JpaRepository<Crystal, Long> {

}
