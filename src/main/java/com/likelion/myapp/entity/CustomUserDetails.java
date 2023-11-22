package com.likelion.myapp.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
public class CustomUserDetails implements UserDetails{

    private String username;                                     // DB의 P.K
    private String password;                                     // DB의 비밀번호
    private boolean accountNonLocked =true;                      // 계정 잠금 여부
    private boolean accountNonExpired =true ;                    // 사용자 계정 만료 없음
    private boolean credentialsNonExpired =true ;                // 비밀번호 만료 없음
    private boolean enabled =true;                               // 사용자 활성화 여부
    private Collection<? extends GrantedAuthority> authorities;  // 사용자 권한 목록

    // 추가로 설정하고 싶은 내용
    private String realName;                 // 사용자의 진짜 이름
    private String email;                    // 사용자 email

    public CustomUserDetails(UserDto dto) {
        this.username = dto.username;
        this.password = dto.password;
        this.email = dto.email;
        this.realName = dto.realname; //실제이름

        //다 고치고 실행했을때 아래 메시지 나오면 현재까지 잘 따라온 상태입니다
        //There is no PasswordEncoder mapped for the id "null"


        //USER,SYSTEM,ADMIN
        //Arrays.stream  : 자바가 ArrayList, 나도 람다를 만들고 싶어
        //filter, map , c언에 함수포인터(GO, Python, Kotllin, ecma script, dark
        //함수기반형 언어.  원래부터 함수는 주소(참조) 함수도 변수처럼 쓸 수 있다
        //변슈에 주소를 저장할 수 있다. 그말은 함수도 저장가능하다. C언어
        //기존의 컬렉션 관점에 끼워넣기 위해서 만든게 stream 이다

        Collection<GrantedAuthority> roles =
                Arrays.stream(dto.roles.split(","))
                        .map(role -> new SimpleGrantedAuthority(username))
                        .collect(Collectors.toList());
        this.authorities = roles;
    }

}