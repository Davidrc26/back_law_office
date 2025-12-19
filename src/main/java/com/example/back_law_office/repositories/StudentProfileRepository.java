package com.example.back_law_office.repositories;

import com.example.back_law_office.models.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
    Optional<StudentProfile> findByUserId(Long userId);
    Optional<StudentProfile> findByStudentCode(String studentCode);
    boolean existsByUserId(Long userId);
}
