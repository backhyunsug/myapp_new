package com.likelion.myapp.controller;

import com.likelion.myapp.entity.UserDto;
import com.likelion.myapp.jwt.TokenProvider;
import com.likelion.myapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.likelion.myapp.entity.TokenDto;
import java.util.*;

@RestController
@RequestMapping(value="/api")
public class ApiController {
    @Autowired
    UserService userService;

    private final TokenProvider tokenProvider; //토큰발행객체
    private final AuthenticationManagerBuilder authenticationManagerBuilder; //인증정보를 저장해서 다른필터를 통과하도록
    //하기위한 객체

    //스프링이 객체 생성할때 호출되어서 값이 초기화 된다.
    public ApiController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @GetMapping("/hello")
    public HashMap<String, Object> hello(){
        HashMap<String, Object> map = new  HashMap<String, Object>();
        map.put("result", "OK");
        map.put("msg", "Hello");
        return map;
    }

    @PostMapping("/signup")
    public HashMap<String, Object> signup(@RequestBody UserDto userDto){
        HashMap<String, Object> map = new  HashMap<String, Object>();

        //현재 중복 체크 아님
        userService.user_regist(userDto);

        map.put("result", "OK");
        map.put("msg", "회원 가입을 축하드립니다. ");
        map.put("userinfo", userDto);

        return map;
    }

    //여기서 디비 정보를 읽어와서 토큰 발행을 해서 보내줘야 한다
    @PostMapping("/login")  // /api/login
    public HashMap<String, Object> login(@RequestBody UserDto userDto){
        HashMap<String, Object> map = new  HashMap<String, Object>();

        //현재 중복 체크해야 한다. => 존재안할때 아이디와 패스워드로 찾았는데 존재않할때
        UserDto resultDto = userService.getUserInfo(userDto);
        if( resultDto==null)
        {
            //예외를 발생시켜도 되고 직접 메시지를 전달해도 된다.
            map.put("result", "fail");
            map.put("username", userDto.getUsername());
            map.put("message", "사용자를 찾을 수 없습니다");
            return map;
        }

        //2. 토큰발행
        //3. 다른필터를한태 통과되었음을 알려야 한다. SecurityContext 인증정보를 넣어놔야 한다.

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDto.getUsername(),
                        userDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //내부메모리여기에 인증정보를 저장해놔야 로그온 한걸려 알고 다른 필터를 통과해야 한다.
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //access_token과 리프레시 토큰을 만든다
        TokenDto tokenDto = tokenProvider.createToken(authentication);

        //tokenDto.getRefresh_token()을 디비에 저장하자 => redis (메모리 디비)

        System.out.println(tokenDto.getAccess_token());
        System.out.println(tokenDto.getRefresh_token());

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenDto.getAccess_token());
//
//        return new ResponseEntity<>(tokenDto, httpHeaders, HttpStatus.OK);
//
        //여기서 토큰을 발행해 보내야 한다.
        map.put("result", "success");
        map.put("accessToken", tokenDto.getAccess_token());
        map.put("refreshToken", tokenDto.getRefresh_token());
        return map;
    }

    @GetMapping("/userinfo") //토큰 가지고 들어와야 한다
    public HashMap<String, Object> userinfo(UserDto userDto) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        UserDto resultDto = userService.getUserInfoByName(userDto);
        if(resultDto==null)
        {
            map.put("result", "fail");
            map.put("username", userDto.getUsername());
            map.put("message", "사용자를 찾을 수 없습니다");
            return map;  //데이터 없을때 여기서 끝난다
        }
        //존재한다
        map.put("result", "success");
        map.put("userinfo", resultDto);
        return map;
    }

}













