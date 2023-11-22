package com.likelion.myapp.jwt;

import com.likelion.myapp.entity.TokenDto;
import com.likelion.myapp.util.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

//토큰을 공급한다 - 토큰을 발행하고 토큰의 형식이 맞는지 체크한다 
@Component
public class TokenProvider implements InitializingBean {

	//로그확인하기 
   private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
   private static final String AUTHORITIES_KEY = "auth";
   private final String secret;
   private final long tokenValidityInMilliseconds;
   private final String secret2;
   private final long tokenValidityInMilliseconds2;
   private Key key;

   //
   public TokenProvider(  //생성자
           //application.properyteis 에 만료시간 암호화 키값등을 저장해놓고 값들을 읽어온다.
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds, 
	  @Value("${jwt.secret2}") String secret2,
	  @Value("${jwt.token-validity-in-seconds2}") long tokenValidityInSeconds2) 

   {
      this.secret = secret;
      this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
      this.secret2 = secret2;
      this.tokenValidityInMilliseconds2 = tokenValidityInSeconds2 * 2000;
   }

   //암호화 알고리즘 셋팅
   @Override
   public void afterPropertiesSet() {
      byte[] keyBytes = Decoders.BASE64.decode(secret);
      this.key = Keys.hmacShaKeyFor(keyBytes);   //
   }

   //토큰을 생성한다. => 알고리즘대로
   public TokenDto createToken(Authentication authentication) {
      String authorities = authentication.getAuthorities().stream()
         .map(GrantedAuthority::getAuthority)
         .collect(Collectors.joining(","));


      long now = (new Date()).getTime();
      Date validity = new Date(now + this.tokenValidityInMilliseconds);
      Date validity2 = new Date(now + this.tokenValidityInMilliseconds2);
      
      String access_token = Jwts.builder()
    	         .setSubject(authentication.getName())
    	         .claim(AUTHORITIES_KEY, authorities)
    	         .signWith(key, SignatureAlgorithm.HS512)
    	         .setExpiration(validity)
    	         .compact();
      
      String refresh_token = Jwts.builder()
 	         .setSubject(authentication.getName())
 	         .claim(AUTHORITIES_KEY, authorities)
 	         .signWith(key, SignatureAlgorithm.HS512)
 	         .setExpiration(validity2)
 	         .compact();
      
      //access_token은 사용자에게 보내고 
      //refresh_token은 디비에 저장하고 나중에 써야 한다 
      
      return new TokenDto(access_token, refresh_token);
      

   }

   //토큰으로 부터 사용자 데이터를 가져오는 함수이다.
   public Authentication getAuthentication(String token) {
      Claims claims = Jwts
              .parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token)
              .getBody();

      Collection<? extends GrantedAuthority> authorities =
         Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

      System.out.println("********************************");
      System.out.println(claims.getSubject());
      System.out.println("********************************");
  
      User principal = new User(claims.getSubject(), "", authorities);
      return new UsernamePasswordAuthenticationToken(principal, token, authorities);
   }

   //토큰이 맞는지 확인한다 => 원래는 true 나 false만보냈는데 Map 으로ㅓ 만들어서 자세한 정ㅂ
   public HashMap<String, String> validateToken(String token) {
	   HashMap<String, String> map = new HashMap<String, String>();
	   
      try {
         Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         map.put("result", "SUCCESS");
         map.put("msg", "인증성공");
      } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
         logger.info("잘못된 JWT 서명입니다.");
         map.put("result", "FAIL");
         map.put("errorcode", ErrorCode.MALFORMED_TOKEN_EXCEPTION+"");
         map.put("msg", "잘못된 JWT 서명입니다");
       
      } catch (ExpiredJwtException e) {
         logger.info("만료된 JWT 토큰입니다.");
         map.put("result", "FAIL");
         map.put("msg", "만료된 JWT 토큰입니다.");
        
      } catch (UnsupportedJwtException e) {
    	 map.put("result", "FAIL");
         map.put("msg", "지원되지 않는 JWT 토큰입니다");
         logger.info("지원되지 않는 JWT 토큰입니다.");
      } catch (IllegalArgumentException e) {
         logger.info("JWT 토큰이 잘못되었습니다.");
         map.put("result", "FAIL");
         map.put("msg", "JWT 토큰이 잘못되었습니다.");
      }
      return map;
   }
}
