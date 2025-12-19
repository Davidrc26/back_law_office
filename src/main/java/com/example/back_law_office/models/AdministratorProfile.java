package com.example.back_law_office.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "administrator_profiles")
public class AdministratorProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Column(name = "admin_level")
    private String adminLevel; // SUPER_ADMIN, MANAGER, COORDINATOR
    
    @Column(name = "department")
    private String department;
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    @Column(name = "position")
    private String position;
    
    @Column(name = "responsibilities", length = 1000)
    private String responsibilities;
}
