package com.myproject.cleanplate.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @Setter @ManyToOne(optional=false, fetch=FetchType.EAGER) @JoinColumn(name = "userId") @JsonBackReference
    private UserAccount userAccount;

    @Setter @Column(nullable = false)
    private Integer quantity;

    @Setter @Column(nullable = false)
    private String category;

    @Setter @Column(nullable = false)
    private String storage;

    @Setter @Column(nullable = false)
    private Date expiration;

    protected Food(){} // 모든 jpa entity는 기본 생성자를 가지고 있어야함.

    private Food(String foodName, UserAccount userAccount, int quantity, String category, String storage, Date expiration) {
        this.foodName = foodName;
        this.userAccount = userAccount;
        this.quantity = quantity;
        this.category = category;
        this.storage = storage;
        this.expiration = expiration;
    }

    public static Food of(String foodName, UserAccount userAccount, int quantity, String category, String storage, Date expiration){
        return new Food(foodName, userAccount, quantity, category, storage, expiration);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Food that)) return false;
        return foodName != null && foodName.equals(that.getFoodName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(foodName);
    }
}
