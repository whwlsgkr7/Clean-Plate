package com.myproject.cleanplate.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@ToString(callSuper = true)
@Getter
@Entity
public class UserAccount extends AuditingFields{
    @Id
    private String userId;

    @Setter @Column(nullable = false)
    private String pwd;

    @Setter @Column(nullable = false)
    private String nickName;

    @Setter @Column(nullable = false)
    private String email;

    @Setter
    private String address;

    protected UserAccount(){}
    private UserAccount(String userId, String pwd, String nickName, String email, String address) {
        this.userId = userId;
        this.pwd = pwd;
        this.nickName = nickName;
        this.email = email;
        this.address = address;
    }

    public static UserAccount of(String userId, String pwd, String nickName, String email, String address){
        return new UserAccount(userId, pwd, nickName, email, address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }
}
