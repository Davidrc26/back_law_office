package com.example.back_law_office.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class CreateProfessorDTO {
    // Campos comunes de User
    private String username;
    private String password;
    private String email;
    private String phone;
    private Set<Long> roleIds; // Debería incluir el rol PROFESSOR
    
    // Campos específicos de ProfessorProfile
    private String department;
    private String specialization;
    private String officeNumber;
    private LocalDate hireDate;
    private String title;
    private String researchArea;
    private String employmentType;
}
