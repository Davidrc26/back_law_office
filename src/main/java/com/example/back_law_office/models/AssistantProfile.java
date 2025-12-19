package com.example.back_law_office.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "assistant_profiles")
public class AssistantProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    @Column(name = "assigned_department")
    private String assignedDepartment;
    
    @Column(name = "shift")
    private String shift; // MORNING, AFTERNOON, EVENING
    
    @Column(name = "hire_date")
    private LocalDate hireDate;
    
    @Column(name = "supervisor_id")
    private Long supervisorId;
    
    @Column(name = "work_area")
    private String workArea; // RECEPTION, ADMINISTRATIVE, LEGAL_SUPPORT
}
