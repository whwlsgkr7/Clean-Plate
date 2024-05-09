package com.myproject.cleanplate.service;

import com.myproject.cleanplate.CleanplateApplication;
//import com.myproject.cleanplate.domain.Alarm;
import com.myproject.cleanplate.domain.Food;
import com.myproject.cleanplate.domain.UserAccount;
//import com.myproject.cleanplate.dto.AlarmDto;
import com.myproject.cleanplate.dto.FoodDto;
import com.myproject.cleanplate.dto.UserAccountDto;
import com.myproject.cleanplate.dto.response.UserJoinResponse;
//import com.myproject.cleanplate.repository.AlarmRepository;
import com.myproject.cleanplate.repository.UserAccountRepository;
import com.myproject.cleanplate.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
//    private final AlarmRepository alarmRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expired-time-ms}")
    private Long expiredTimeMs;


    public UserJoinResponse join(String username, String password, String role, String nickName, String email, String address) throws Exception {
        // 회원가입하려는 userId로 회원가입된 user가 있는지
        if (userAccountRepository.findByUsername(username) != null) {
            throw new Exception("이미 존재하는 Id입니다.");
        }

        // 회원가입 진행
        UserAccount userAccount = userAccountRepository.save(UserAccount.of(username, encoder.encode(password), role, nickName, email, address));
        return UserJoinResponse.from(userAccount);
    }

//    public List<AlarmDto> alarmList(String username) throws Exception{
//        UserAccount userAccount = userAccountRepository.findByUsername(username);
//
//        return alarmRepository.findAllByUsername(userAccount).stream().map(AlarmDto::fromEntity).collect(Collectors.toList());
//    }

//    public String login(String username, String password) throws Exception{
//        // 회원가입 여부 체크
//        UserAccount userAccount = userAccountRepository.findByUsername(username);
//
//        // 비밀번호 체크
//        if(!encoder.matches(pwd, userAccount.getPwd())){
//            throw new Exception("비밀번호가 다름");
//        }
//
//        return JwtTokenUtils.generateToken(username, secretKey, expiredTimeMs);
//    }



}
