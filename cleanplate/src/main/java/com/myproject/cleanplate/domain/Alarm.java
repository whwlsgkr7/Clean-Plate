package com.myproject.cleanplate.domain;

import com.myproject.cleanplate.domain.constant.AlarmType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@Entity
//@SQLDelete(sql = "UPDATE \'alarm\' SET removed_at = NOW() WHERE id=?")
//@Where(clause = "removed_at is NULL")
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알람을 받은 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    private UserAccount userAccount;

    // 어떤 알람인지 문자열로 저장, EnumType.ORDINAL로 설정할 경우 AlarmType에 선언되어 있는 순서가 저장
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column(length = 2000)
    private String content;


    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Column(name = "removed_at")
    private LocalDateTime removedAt;

    protected Alarm(){}

    private Alarm(UserAccount userAccount, AlarmType alarmType, String content) {
        this.userAccount = userAccount;
        this.alarmType = alarmType;
        this.content = content;
    }

    public static Alarm of(UserAccount username, AlarmType alarmType, String content) {
        return new Alarm(username, alarmType, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Alarm alarm)) return false;
        return id != null && id.equals(alarm.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userAccount, alarmType);
    }
}