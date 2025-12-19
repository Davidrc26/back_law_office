package com.example.back_law_office.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class CreateAdministratorDTO {
    // Campos comunes de User
    private String username;
    private String password;
    private String email;
    private String phone;
    private Set<Long> roleIds; // Debería incluir el rol ADMIN
    
    // Campos específicos de AdministratorProfile
    private String adminLevel;
    private String department;
    private LocalDate hireDate;
    private String position;
    private String responsibilities;
}
