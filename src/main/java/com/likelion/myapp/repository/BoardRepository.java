package com.likelion.myapp.repository;

import com.likelion.myapp.entity.BoardDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//자체가 인터페이스라 객체 못만들음
//JpaRepository<BoardDto(디비에서가져올객체타입), long(primary key 형태),
public interface BoardRepository extends JpaRepository<BoardDto, Long> {
    List<BoardDto> findByTitleOrderByIdDesc(String title);
}

