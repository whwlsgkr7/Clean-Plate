package com.myproject.cleanplate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration // Bean을 만들때 싱글톤으로 만든다.
public class JpaConfig {
    // 어떤 테이블에 데이터를 삽입할 때 누가 삽입하는건지(Auditing) 설정(createdBy, modifiedBy)
    @Bean
    public AuditorAware<String> auditorAware(){ // AuditorAware는 Spring Data JPA에서 현재 사용자 또는 시스템을 감사하는 데 사용되는 인터페이스
        return () -> Optional.of("jinhak");
    }
}