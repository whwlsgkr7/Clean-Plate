package com.myproject.cleanplate.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Entity
public class Restaurant {
    @Id
    private String address;

//    @Setter @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name = "username") @JsonBackReference
//    private UserAccount userAccount;

    @Setter @Column(nullable = false, length=50)
    private String restaurantName;

    @Setter @Column(nullable = false, length=20)
    private String phoneNumber;

    @Setter @Column(nullable = false, length=20)
    private String sanitaryGrade;

    @Setter @Column(nullable = false, length = 20)
    private String assignYMD;

    @Setter @Column(nullable = false, length = 50)
    private String presidentName;

    protected Restaurant(){}

    private Restaurant(String address, String restaurantName, String phoneNumber, String sanitaryGrade, String assignYMD, String presidentName) {
        this.address = address;
//        this.userAccount = userAccount;
        this.restaurantName = restaurantName;
        this.phoneNumber = phoneNumber;
        this.sanitaryGrade = sanitaryGrade;
        this.assignYMD = assignYMD;
        this.presidentName = presidentName;
    }

    public  static Restaurant of(String address, String restaurantName, String phoneNumber, String sanitaryGrade, String assignYMD, String presidentName){
        return new Restaurant(address, restaurantName, phoneNumber, sanitaryGrade, assignYMD, presidentName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant that)) return false;
        return address != null && address.equals(that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
