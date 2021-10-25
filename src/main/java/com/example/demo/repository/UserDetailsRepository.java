package com.example.demo.repository;

import com.example.demo.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Long> {
    Optional<UserDetailsEntity> findByUsername(String username);
    Optional<UserDetailsEntity> findByEmail(String email);
}
