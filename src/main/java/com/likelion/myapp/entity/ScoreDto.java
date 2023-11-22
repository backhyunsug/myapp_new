package com.likelion.myapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="TB_SCORE")
@Builder
@ToString
@NoArgsConstructor(access= AccessLevel.PUBLIC)//기본생성자
@AllArgsConstructor //생성자를 이용한 객체 생성자를 막자
@Getter
@Setter

@SequenceGenerator(
        name="SEQ_SCORE_GENERATOR",
        sequenceName = "SEQ_TB_SCORE",
        initialValue = 1,
        allocationSize = 1
)  //oracle의 시퀀스를 사용하기 위한 코드이다

public class ScoreDto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_SCORE_GENERATOR")
    Long id;
    String name;
    Integer kor;
    Integer eng;
    Integer mat;
    Integer total;
    Double avg;
}
