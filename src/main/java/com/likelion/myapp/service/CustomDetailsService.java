package com.likelion.myapp.service;

import com.likelion.myapp.entity.CustomUserDetails;
import com.likelion.myapp.entity.UserDto;
import com.likelion.myapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
public class CustomDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;  //레포지토리 객체와 연결(DI-Dependency Inject)

    @Autowired
    PasswordEncoder encoder;

    //이 함수 하나만 overloading를 한다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //username => 디비에거서 객체를 찾아오기 : repository를 통해서 findById 객체를 가져오기
        //User -> username, password, roles 밖에 없음
        //디비에서  username 으로 읽어오면
        //withDefaultPasswordEncoder가 작동해서 패스워드를 만춘다

        //username : 이 파라미터 하나만 받는다 login.html => username 으로 user 값을 보냄
        //UserDetailsService 의 loadUserByUsername함수가 호출된다.
        //이 앞단계는 스프링프레임워크에게 전달하고 우리가 loadUserByUsername를 호출해줄게
        //loadUserByUsername 다시 정의하자
        //상속성격의 장점 부모클래스 웬만한거 다해결하고 우리가 마무리할것이 있으면 그 부분만 인터페이스로 노출시킨다

        //디비에가서 객체를 username으로 찾아서 가져와서 => UserDetails
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("1234")
//                .roles("USER")
//                .build();
        //커스너마이징 - 디비로부터 UserDto 객체를 가져오자
        //로그온   - /login  을 post 방식
        //로그아웃   - /logout  을 post 방식

        UserDto userDto =  userRepository.findByUserName(username);
        //details 에 암호화된 패스워드가 전달되어야 한다
        userDto.setPassword(encoder.encode(userDto.getPassword()) );
        //회원가입시 encoding을 해서 저장하면 굳이 이 작업이 필요없다.
        //현재 회원가입기능이 없어서

        CustomUserDetails details = new CustomUserDetails(userDto);
        return details;
    }
}
