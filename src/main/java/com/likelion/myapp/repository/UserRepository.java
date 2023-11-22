package com.likelion.myapp.repository;

import com.likelion.myapp.entity.BoardDto;
import com.likelion.myapp.entity.UserDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository  extends JpaRepository<UserDto, Long> {
    //select 객체   from 클래스명  객체  where 객체.필드=:파라미터
    @Query("select u from UserDto u where u.username=:username")
    UserDto findByUserName(@Param("username") String username);

    //@Query("select u from UserDto u where u.username=:username and u.password=:password")
    UserDto findByUsernameAndPassword(String username, String password);
}
