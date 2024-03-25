package com.myproject.cleanplate.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "sanitaryGrade"),
        @Index(columnList = "exemplaryRestaurant"),
        @Index(columnList = "safeRestaurant"),
        @Index(columnList = "address")
})
@Entity
public class Restaurant {
    @Id @Column(length = 50)
    private String address;

    @Setter @ManyToOne @JoinColumn(name = " userId")
    private UserAccount userAccount;

    @Setter @Column(nullable = false, length=50)
    private String restaurantName;

    @Setter @Column(nullable = false, length=20)
    private String phoneNumber;

    @Setter @Column(nullable = false, length=20)
    private String sanitaryGrade;

    @Setter @Column(nullable = false, length = 20)
    private String assignYMD;

    @Setter @Column(nullable = false, length = 20)
    private String presidentName;

    protected Restaurant(){}

    private Restaurant(String address, UserAccount userAccount, String restaurantName, String phoneNumber, String sanitaryGrade, String assignYMD, String presidentName) {
        this.address = address;
        this.userAccount = userAccount;
        this.restaurantName = restaurantName;
        this.phoneNumber = phoneNumber;
        this.sanitaryGrade = sanitaryGrade;
        this.assignYMD = assignYMD;
        this.presidentName = presidentName;
    }

    public  static Restaurant of(String address, UserAccount userAccount, String restaurantName, String phoneNumber, String sanitaryGrade, String assignYMD, String presidentName){
        return new Restaurant(address, userAccount, restaurantName, phoneNumber, sanitaryGrade, assignYMD, presidentName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Restaurant that)) return false;
        return address != null &&address.equals(that.getAddress());
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
