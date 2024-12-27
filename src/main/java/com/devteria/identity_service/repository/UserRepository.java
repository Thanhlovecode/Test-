package com.devteria.identity_service.repository;

import com.devteria.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByUsernameIgnoreCase(String username);
    Optional<User> findByUsername(String username);
}
