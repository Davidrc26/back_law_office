package com.example.back_law_office.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.back_law_office.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = :email")
    Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);

}
