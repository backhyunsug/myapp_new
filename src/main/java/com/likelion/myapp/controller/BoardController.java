package com.likelion.myapp.controller;

import com.likelion.myapp.entity.BoardDto;
import com.likelion.myapp.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value="/api/board")
public class BoardController {

    @Autowired
    BoardService service;


    @GetMapping("/list")  //http://localhost:9000/api/board/list?page=0&size=5
    public List<BoardDto> getAllList(BoardDto dto, Pageable pageable)
    {

        return service.getList(dto, pageable);
    }

    @PostMapping("/save")
    public HashMap<String, String> save(@RequestBody BoardDto dto )
    {
        service.insert(dto);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("result", "success");
        map.put("msg", "등록성공");
        return map;
    }
}
