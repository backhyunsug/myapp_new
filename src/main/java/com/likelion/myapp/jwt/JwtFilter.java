package com.likelion.myapp.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

//Spring boot 시스템이 제공하는 필터가 아니다. 사용자가 필터를 재정의 해서 까워넣기
//GenericFilterBean :  사용자가 필터를 만들고 싶다면 기본 필터로 작업을 해야 한다
public class JwtFilter extends GenericFilterBean {

   private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
   public static final String AUTHORIZATION_HEADER = "Authorization";
   private TokenProvider tokenProvider;

   //생성자를 통해 tokenProvider를 제공한다
   public JwtFilter(TokenProvider tokenProvider) {
      this.tokenProvider = tokenProvider;
   }

   //jwt토큰에 대한 처리가 이뤄진다. ==JWTFilter
    //필터로 동작하려면 doFilter 를 반드시 오버라이딩해서 니가 알아서 하고 나머지는 우리가 알아서 한다
   @Override
   public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, 
		   FilterChain filterChain) throws IOException, ServletException {
      HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
      //jwt 토큰은 header 에 
      //Authorization Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMDEiLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjk5ODM2MDE4fQ.QxyRcAWX72LOXPJ7ldInyspVpu-DViTyzyE_KWaEqSQHbpgL8haWJohE0JuCkqC9nerFvvT7TX0HEYFjao0T-Q
      //형태로 값을 전달한다

      String jwt = resolveToken(httpServletRequest); //header에서 인증 토큰을 추출한다, access 토큰을 추출한다
      String requestURI = httpServletRequest.getRequestURI();  //현재 url

      // System.out.println(jwt);

      if (StringUtils.hasText(jwt))  //내가 추출했다. 헤더에 있었다. Authorization
      {
    	  Map<String, String> resultMap = tokenProvider.validateToken(jwt);//토큰검증을 해서

          if(resultMap.get("result").equals("SUCCESS"))  {
    		  Authentication authentication = tokenProvider.getAuthentication(jwt);//jwt	토큰으로 부터 사용자 정보를 가져온다
              // 유효한 토큰을 SecurityContextHolder 에 둔다
	          SecurityContextHolder.getContext().setAuthentication(authentication);
              //내부의 세션객체 (SecurityContext에 저장된ㄷ 토큰을 가져온다
	          logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}",
	         		 authentication.getName(), requestURI);
	       } else {
	    	   servletRequest.setAttribute("tokenexception",  resultMap);
	     	  //access token이 없을 경우에 
	     	  //토큰에 대한 정보를 저장해서 보내야 한다 
	           logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
	          
	       }

      }
      else  //아예 인증정보 없이 왔을때도 별도의 에러처리가 필요할 수도 있다 
      {
    	  HashMap<String, String> map = new HashMap<String, String>();  //헤더에 인증정보가 없으니까
    	  map.put("result", "FAIL");
          map.put("msg", "인증정보가 없습니다");
          servletRequest.setAttribute("tokenexception",  map);  //토큰에러 입니다
      }
    	 
  
      filterChain.doFilter(servletRequest, servletResponse);  //마지막에 반드시 호출해야 한다.다음체인으로 이동하라
  
   }

   //토큰을 분리한다 
   private String resolveToken(HttpServletRequest request) {
      String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

      if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
         return bearerToken.substring(7);
      }

      return null;
   }
   
   
}
