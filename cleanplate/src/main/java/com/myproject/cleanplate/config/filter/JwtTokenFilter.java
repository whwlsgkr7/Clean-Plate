package com.myproject.cleanplate.config.filter;

import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.dto.CustomUserDetails;
import com.myproject.cleanplate.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;

    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/users/alarm/subscribe");


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String token;


        if(TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())){
            token = request.getQueryString().split("=")[1].trim();
        }
        else{
            //request에서 Authorization 헤더를 찾음
            String authorization= request.getHeader("Authorization");

            //Authorization 헤더 검증
            if (authorization == null || !authorization.startsWith("Bearer ")) {

                System.out.println("token null");
                filterChain.doFilter(request, response);

                //조건이 해당되면 메소드 종료 (필수)
                return;
            }
            //Bearer 부분 제거 후 순수 토큰만 획득
            token = authorization.split(" ")[1];

            System.out.println("authorization now");
        }



        //토큰 소멸 시간 검증
        if (jwtTokenUtils.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtTokenUtils.getUsername(token);
        String role = jwtTokenUtils.getRole(token);

        //userEntity를 생성하여 값 set
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername(username);
        userAccount.setPassword("temppassword");
        userAccount.setRole(role);

        //UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = CustomUserDetails.fromEntity(userAccount);

        //스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}