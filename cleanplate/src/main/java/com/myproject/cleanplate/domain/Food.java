package com.myproject.cleanplate.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@ToString(callSuper = true)
@Getter
@Entity
public class Food extends AuditingFields{
    @Id
    private String foodName;

    @Setter @ManyToOne(optional = false) @JoinColumn(name = "userId")
    private UserAccount userAccount;

    @Setter @Column(nullable = false)
    private String category;

    @Setter @Column(nullable = false)
    private String storage;

    @Setter @Column(nullable = false)
    private String expiration;

    protected Food(){}

    private Food(String foodName, UserAccount userAccount, String category, String storage, String expiration) {
        this.foodName = foodName;
        this.userAccount = userAccount;
        this.category = category;
        this.storage = storage;
        this.expiration = expiration;
    }

    public static Food of(String foodName, UserAccount userAccount, String category, String storage, String expiration){
        return new Food(foodName, userAccount, category, storage, expiration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food food)) return false;
        return Objects.equals(foodName, food.foodName) && Objects.equals(userAccount, food.userAccount) && Objects.equals(category, food.category) && Objects.equals(storage, food.storage) && Objects.equals(expiration, food.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodName, userAccount, category, storage, expiration);
    }
}
