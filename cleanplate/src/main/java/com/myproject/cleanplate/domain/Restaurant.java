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
    @Id
    private String address;

    @Setter @Column(nullable = false)
    private String restaurantName;

    @Setter @Column(nullable = false)
    private String phoneNumber;

    @Setter @Column(nullable = false)
    private String sanitaryGrade;

    @Setter @Column(nullable = false)
    private String exemplaryRestaurant;

    @Setter @Column(nullable = false)
    private String safeRestaurant;

    protected Restaurant(){}

    private Restaurant(String address, String restaurantName, String phoneNumber, String sanitaryGrade, String exemplaryRestaurant, String safeRestaurant) {
        this.address = address;
        this.restaurantName = restaurantName;
        this.phoneNumber = phoneNumber;
        this.sanitaryGrade = sanitaryGrade;
        this.exemplaryRestaurant = exemplaryRestaurant;
        this.safeRestaurant = safeRestaurant;
    }

    public  static Restaurant of(String address, String restaurantName, String phoneNumber, String sanitaryGrade, String exemplaryRestaurant, String safeRestaurant){
        return new Restaurant(address, restaurantName, phoneNumber, sanitaryGrade, exemplaryRestaurant, safeRestaurant);
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
