package com.likelion.myapp.service;

import com.likelion.myapp.entity.UserDto;
import com.likelion.myapp.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    //아이디중복체크 생략
    @Transactional
    public void user_regist(UserDto user)
    {
        userRepository.save(user); //디비 저장하기
    }

    public UserDto getUserInfo(UserDto user)
    {
        return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
    }

    public UserDto getUserInfoByName(UserDto user){
        return userRepository.findByUserName(user.getUsername());
    }
}











