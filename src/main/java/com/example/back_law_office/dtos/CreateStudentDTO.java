package com.example.back_law_office.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class CreateStudentDTO {
    // Campos comunes de User
    private String username;
    private String password;
    private String email;
    private String phone;
    private Set<Long> roleIds; // Debería incluir el rol STUDENT
    
    // Campos específicos de StudentProfile
    private String studentCode;
    private Integer semester;
    private String major;
    private LocalDate enrollmentDate;
    private String university;
    private String academicStatus;
}
