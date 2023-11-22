package com.likelion.myapp.repository;

import com.likelion.myapp.entity.ScoreDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<ScoreDto, Long> {
}
