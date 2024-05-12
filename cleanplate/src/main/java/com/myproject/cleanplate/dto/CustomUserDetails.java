package com.myproject.cleanplate.dto;

import com.myproject.cleanplate.domain.UserAccount;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record CustomUserDetails(String username,
                                String password,
                                Collection<? extends GrantedAuthority> authorities,
                                String nickname,
                                String email,
                                String address) implements UserDetails {

    public static CustomUserDetails of(String username, String password, String nickname, String email, String address){
        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new CustomUserDetails(
                username,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                nickname,
                email,
                address
        );
    }

    public static CustomUserDetails fromDto(UserAccountDto dto){
        return CustomUserDetails.of(
                dto.username(),
                dto.password(),
                dto.nickName(),
                dto.email(),
                dto.address()
        );
    }

    public static CustomUserDetails fromEntity(UserAccount entity){
        return CustomUserDetails.of(
                entity.getUsername(),
                entity.getPassword(),
                entity.getNickName(),
                entity.getEmail(),
                entity.getAddress()
        );
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {return authorities;}

    @Override
    public String getPassword() {return password;}

    @Override
    public String getUsername() {return username;}

    @Override
    public boolean isAccountNonExpired() {return true;}

    @Override
    public boolean isAccountNonLocked() {return true;}

    @Override
    public boolean isCredentialsNonExpired() {return true;}

    @Override
    public boolean isEnabled() {return true;}

    public enum RoleType {
        USER("ROLE_USER");

        @Getter
        private final String name;

        RoleType(String name) {
            this.name = name;
        }
    }
}
