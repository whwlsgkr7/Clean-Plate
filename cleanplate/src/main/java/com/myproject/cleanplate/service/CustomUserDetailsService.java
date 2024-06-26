package com.myproject.cleanplate.service;

import com.myproject.cleanplate.domain.UserAccount;
import com.myproject.cleanplate.dto.CustomUserDetails;
import com.myproject.cleanplate.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB에서 조회
        UserAccount userData = userAccountRepository.findByUsername(username);

        if (userData != null) {

            return CustomUserDetails.fromEntity(userData);
        }

        return null;
    }
}
