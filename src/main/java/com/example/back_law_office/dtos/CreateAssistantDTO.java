package com.example.back_law_office.dtos;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class CreateAssistantDTO {
    // Campos comunes de User
    private String username;
    private String password;
    private String email;
    private String phone;
    private Set<Long> roleIds; // Debería incluir el rol ASSISTANT
    
    // Campos específicos de AssistantProfile
    private String assignedDepartment;
    private String shift;
    private LocalDate hireDate;
    private Long supervisorId;
    private String workArea;
}
