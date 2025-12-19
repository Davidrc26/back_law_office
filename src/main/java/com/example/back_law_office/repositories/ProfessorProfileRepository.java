package com.example.back_law_office.repositories;

import com.example.back_law_office.models.ProfessorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfessorProfileRepository extends JpaRepository<ProfessorProfile, Long> {
    Optional<ProfessorProfile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
