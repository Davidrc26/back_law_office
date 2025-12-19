package com.example.back_law_office.repositories;

import com.example.back_law_office.models.AssistantProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AssistantProfileRepository extends JpaRepository<AssistantProfile, Long> {
    Optional<AssistantProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
