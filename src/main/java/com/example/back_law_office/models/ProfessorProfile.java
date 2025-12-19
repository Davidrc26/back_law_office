package com.example.back_law_office.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "professor_profiles")
public class ProfessorProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "specialization")
    private String specialization;
    
    @Column(name = "office_number")
    private String officeNumber;
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    @Column(name = "title")
    private String title; // Dr., Mg., Lic.
    
    @Column(name = "research_area")
    private String researchArea;
    
    @Column(name = "employment_type")
    private String employmentType; // FULL_TIME, PART_TIME, ADJUNCT
}
