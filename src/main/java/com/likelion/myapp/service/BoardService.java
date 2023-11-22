package com.likelion.myapp.service;

import com.likelion.myapp.entity.BoardDto;
import com.likelion.myapp.repository.BoardRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@Transactional  //자동 트랙잭션 처리 => 콘서트 공연
public class BoardService {
    @Autowired
    private BoardRepository boardRepo;
    //page=0&size=15
    public List<BoardDto> getList(BoardDto dto, Pageable pagable)
    {

        return boardRepo.findAll(pagable).getContent();
        //return boardRepo.findAll(pagable);
    }

    public int getTotalCount(){
        return (int)boardRepo.count();
    }

    //insert, update, delete 등에는 @Transctional
    @Transactional
    public void insert(BoardDto dto)
    {
        //dto객체에 id 필드에 값이 없으면  insert 가 직동된다.
        boardRepo.save(dto);
    }

    @Transactional
    public void update(BoardDto dto)
    {
        //dto객체에 id 필드에 값이 있으면 update쿼리가 직동된다.
        boardRepo.save(dto);
    }

    @Transactional
    public void delete(BoardDto dto)
    {
        //dto객체에 id 필드에 있는 값하고 일치하는거 삭제한다.
        boardRepo.delete(dto);
    }

    public Optional<BoardDto> getView(Long id)
    {
        return boardRepo.findById(id);
    }

    //Optional => lambda : 객체가 Null값을 가질때 if문으로 객체가 null인경우와
    //아닌경우 나눠서 작업을 한다. Optional 객체값이 null알수 있다는말
}
