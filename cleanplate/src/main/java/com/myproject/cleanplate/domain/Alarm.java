package com.myproject.cleanplate.domain;

import com.myproject.cleanplate.domain.constant.AlarmArgs;
import com.myproject.cleanplate.domain.constant.AlarmType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Entity
//@Table(name = "\"alarm\"", indexes = {
//        @Index(name = "user_id_idx", columnList = "user_id")
//})
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@SQLDelete(sql = "UPDATE \"alarm\" SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
@NoArgsConstructor
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;

    // 알람을 받은 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private UserAccount username;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

//    @Type(type = "json")
//    @Column(columnDefinition = "json")
//    private AlarmArgs args;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "modified_at")
    private Timestamp modifiedAt;

    @Column(name = "removed_at")
    private Timestamp removedAt;


    @PrePersist
    void createAt() {
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void modifiedAt() {this.modifiedAt = Timestamp.from(Instant.now());}

    public static Alarm of(AlarmType alarmType, UserAccount username) {
        Alarm entity = new Alarm();
        entity.setAlarmType(alarmType);
//        entity.setArgs(args);
        entity.setUsername(username);
        return entity;
    }
}