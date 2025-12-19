package com.example.back_law_office.dtos;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String secondName;
    private String secondLastName;
    private String documentNumber;
    private Set<RoleDTO> roles;
    private String phone;
    
    // Perfiles específicos (solo uno estará presente según el rol)
    private StudentProfileDTO studentProfile;
    private ProfessorProfileDTO professorProfile;
    private AdministratorProfileDTO administratorProfile;
    private AssistantProfileDTO assistantProfile;
}