package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final UserAccount userAccount;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userAccount.getRole();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {

        return userAccount.getPassword();
    }

    @Override
    public String getUsername() {

        return userAccount.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    @Override
    public boolean isAccountNonLocked() {

        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}
