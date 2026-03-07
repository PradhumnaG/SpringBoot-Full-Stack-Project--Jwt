package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // [00:23:52] Created to retrieve a user by email (for login and profile details)
    Optional<UserEntity> findByEmail(String email);

    // [00:31:52] Created to check if an email already exists during registration
    boolean existsByEmail(String email);
/*    Optional<UserEntity> findByUserId(String email);*/

}
