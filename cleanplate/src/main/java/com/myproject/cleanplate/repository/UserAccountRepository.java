package com.myproject.cleanplate.repository;

import com.myproject.cleanplate.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {

    UserAccount findByUsername(String useraname);
}
