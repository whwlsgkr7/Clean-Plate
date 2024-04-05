package com.myproject.cleanplate.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ToString(callSuper = true)
@Getter
@Entity
public class UserAccount extends AuditingFields{
    @Id @Setter
    private String username;

    @ToString.Exclude
    @JsonManagedReference
    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL)
    private List<Food> foodList = new ArrayList<>();

//    @ToString.Exclude
//    @OrderBy("address")
//    @JsonManagedReference
//    @OneToMany(mappedBy = "userAccount", cascade = CascadeType.ALL)
//    private List<Restaurant> restaurantList = new ArrayList<>();

    @Setter @Column(nullable = false)
    private String password;

    @Column(nullable = false) @Setter
    private String role;

    @Setter @Column(nullable = false)
    private String nickName;

    @Setter @Column(nullable = false)
    private String email;

    @Setter
    private String address;

    public UserAccount(){}
    private UserAccount(String username, String password, String role, String nickName, String email, String address) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.nickName = nickName;
        this.email = email;
        this.address = address;
    }

    public static UserAccount of(String username, String password, String role, String nickName, String email, String address){
        return new UserAccount(username, password, role, nickName, email, address);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return username != null && username.equals(that.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
