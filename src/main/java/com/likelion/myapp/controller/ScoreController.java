package com.likelion.myapp.controller;

import com.likelion.myapp.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;


@RestController
@RequestMapping(value="/api/score")
class ScoreController {
    @Autowired
    ScoreService scoreService;
    @GetMapping("/list")
    public HashMap<String, Object> score_list()
    {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("result", "success");
        map.put("scoreList", scoreService.getList());
        return map;
    }
}