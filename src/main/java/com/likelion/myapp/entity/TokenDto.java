package com.likelion.myapp.entity;


import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String access_token;
    private String refresh_token;
}
