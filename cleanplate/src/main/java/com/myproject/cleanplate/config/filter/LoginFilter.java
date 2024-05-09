package com.myproject.cleanplate.config.filter;

import com.myproject.cleanplate.dto.CustomUserDetails;
import com.myproject.cleanplate.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

//        System.out.println(username);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        String username = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();


        String token = jwtTokenUtils.createJwt(username, role, 2592000000L);

        response.addHeader("Authorization", "Bearer " + token);
    }


    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); 

        // JSON 형태의 오류 메시지 작성, \ 는 "를 문자열의 경계를 의미하는 것이 아니라, 문자열의 일부임을 알리는 역할
        String errorMessage = "{\"error\": \"id 또는 비밀번호가 일치하지 않습니다.\"}";

        // 응답 본문에 오류 메시지 쓰기
        try {
            ServletOutputStream out = response.getOutputStream();
            out.write(errorMessage.getBytes("UTF-8"));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
