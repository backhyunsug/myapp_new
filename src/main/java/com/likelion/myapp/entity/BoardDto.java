package com.likelion.myapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
//매개변수가 없는 생성자를 PROTECED  접근권한으로 만들어라  == 직접객체생성 하지 마라
@NoArgsConstructor(access= AccessLevel.PROTECTED)
@Entity
@Table(name="TB_BOARD")

public class BoardDto
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;
    @Column(nullable=false, length=100)
    private String title;
    //조인키값 --  mysql 은 Autoincremet 가 무조건 primary key이다.
    @Column(nullable=false)
    private Long member_id;
    @Column(nullable=true, length=100)
    //@Lob  //clob
    private String contents;  //우리컴퓨터 시간이아니라서 서버시간으로 해야 한다.
    private LocalDateTime createDate = LocalDateTime.now();

    //개체 생성할때 생성자 안쓰고 builder 사용하는거
    @Builder
    public BoardDto(String title, String contents, Long member_id) {
        this.title = title;
        this.contents = contents;
        this.member_id = member_id;
    }

    //BoardDto  - UserDto : 글쓴이에 대한 정보
    //한명의 writer 에 여러개의 게시글  1:N ==> board 입장에서 N:1의 관계

    //늦은 초기호와 - 생성자를 이용한 초기화를 잘 안씀
    //join시 나중에 객체들이 초기화되어서 들어오나다. fetch-가져와라
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="member_id", insertable=false, updatable=false)
    //조인이 될 필드에 대한 정보 tb_user 테이블의 user_id 필드랑 조인할거다
    //실제 디비상에서는 프라이머리키어야 조인되는 거 아님, jpa 는 primary key  여애 조인 가능
    //jpa으  경우 자동으로 foreign key룰 만든다. 참조되는 필드가 unique 또는 primary key 여야 한다
    private UserDto member;

}




