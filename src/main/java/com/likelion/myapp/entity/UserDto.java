package com.likelion.myapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="TB_USER")
@Builder
@ToString
@NoArgsConstructor(access= AccessLevel.PUBLIC)//기본생성자
@AllArgsConstructor //생성자를 이용한 객체 생성자를 막자
@Getter
@Setter

public class UserDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long member_id;

    @Column(name="username", length=100, unique = true)
    String username;
    String password;
    String email;
    String phone;
    String roles;
    String realname;
    LocalDateTime wdate = LocalDateTime.now();

    //참조되는 쪽에서 : 참조하는 엔터티에서 UserDto member  이 필드를 지정해야 한다
    @JsonIgnore
    @JsonIgnoreProperties( {"hibernateLazyInitializer", "handler"})
    @OneToMany(mappedBy="member", fetch=FetchType.LAZY)
    private List<BoardDto> boardList = new ArrayList<BoardDto>();
}



