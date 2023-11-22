package com.likelion.myapp.service;

import com.likelion.myapp.entity.ScoreDto;
import com.likelion.myapp.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreService {
    @Autowired
    ScoreRepository scoreRepo;

    public List<ScoreDto> getList ()
    {
        return  scoreRepo.findAll();
    }
}
