package com.example.back_law_office.repositories;

import com.example.back_law_office.models.AdministratorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdministratorProfileRepository extends JpaRepository<AdministratorProfile, Long> {
    Optional<AdministratorProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
