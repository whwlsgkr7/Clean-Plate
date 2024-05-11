package com.myproject.cleanplate.repository;

import com.myproject.cleanplate.domain.Alarm;
import com.myproject.cleanplate.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
    List<Alarm> findAllByUserAccountUsername(String username);

}
