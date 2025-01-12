package com.devteria.identity_service.repository;

import com.devteria.identity_service.entity.User;
import com.devteria.identity_service.repository.custom.SearchRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long>, SearchRepositoryCustom, JpaSpecificationExecutor<User> {
    boolean existsByUsernameIgnoreCase(String username);
    Optional<User> findByUsername(String username);
}
